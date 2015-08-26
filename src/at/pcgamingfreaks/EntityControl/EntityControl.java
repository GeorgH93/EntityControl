/*
* Copyright (C) 2014-2015 GeorgH93
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package at.pcgamingfreaks.EntityControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.WaterMob;
import org.bukkit.plugin.java.JavaPlugin;

import at.pcgamingfreaks.EntityControl.Listener.*;

public class EntityControl extends JavaPlugin
{
	public Logger log;
	public Config config;
	public static Language lang;

	public void onEnable()
	{
		log = getLogger();
		config = new Config(this);
		lang = new Language(this);
		
		
		NotifyPlayers = config.GetLimiterNotifyPlayers();
		SurroundingChunks = config.GetLimiterCheckSurroundingChunks();
		Message_RemovedEntites = lang.Get("RemovedEntites");
		
		RegisterEvents();
		
		log.info("Entity Control finished loading and is now watching all the entitys");
	}

	public void RegisterEvents()
	{
		if(config.GetBuildEnabled())
		{
			getServer().getPluginManager().registerEvents(new BlockPlace(this), this);
		}
		if(config.GetDispenserEnabled())
		{
			getServer().getPluginManager().registerEvents(new BlockDispense(this), this);
		}
		if(config.GetChickenEggEnabled())
		{
			getServer().getPluginManager().registerEvents(new PlayerEggThrow(this), this);
		}
		if(config.GetSpawnEggEnabled())
		{
			getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
		}
		if(config.GetLimiterEnabled())
		{
			if(config.GetLimiterEnabledOnSpawn())
			{
				getServer().getPluginManager().registerEvents(new CreatureSpawn(this), this);
			}
			getServer().getPluginManager().registerEvents(new ChunkLoad(this), this);
		}
	}
	
	public void onDisable()
	{
	    getServer().getScheduler().cancelTasks(this);
	    log.info("All running tasks have been stopped. Plugin disabled.");
	}
	
	
	
	private boolean NotifyPlayers;
	private int SurroundingChunks;
	private String Message_RemovedEntites;
	
	public void CheckChunks(Location loc)
	{
		Chunk c = loc.getChunk();
		CheckChunk(c);
		if(SurroundingChunks > 0)
		{
			World w = loc.getWorld();
		    for(int x = c.getX() + SurroundingChunks; x >= c.getX() - SurroundingChunks; x--)
		    {
		    	for(int z = c.getZ() + SurroundingChunks; z >= c.getZ() - SurroundingChunks; z--)
		    	{
		    		CheckChunk(w.getChunkAt(x, z));
		        }
		    }
		}
	}
	
	public void CheckChunk(Chunk c)
	{
		if(!c.isLoaded())
		{
			return;
		}
		Entity[] ents = c.getEntities();
		HashMap<String, ArrayList<Entity>> types = new HashMap<String, ArrayList<Entity>>();
		EntityType t;
	    for(int i = ents.length - 1; i >= 0; i--)
	    {
	    	if(ents[i] instanceof Player)
	    	{
	    		continue;
	    	}
	    	t = ents[i].getType();
		    String eType = t.toString();
		    String eGroup = getMobGroup(ents[i]);
		    if(config.GetLimiterMaxEntitiesContains(eType))
		    {
		    	if (!types.containsKey(eType))
		    	{
		    		types.put(eType, new ArrayList<Entity>());
		        }
		        types.get(eType).add(ents[i]);
		    }
		    if(config.GetLimiterMaxEntitiesContains(eGroup))
		    {
		    	if (!types.containsKey(eGroup))
		    	{
		    		types.put(eGroup, new ArrayList<Entity>());
		        }
		        types.get(eGroup).add(ents[i]);
		    }
	    }
		for(Map.Entry<String, ArrayList<Entity>> entry : types.entrySet())
		{
			String eType = (String)entry.getKey();
		    int limit = config.GetLimiterMaxEntities(eType);
		    if(entry.getValue().size() > limit)
		    {
		        if(NotifyPlayers)
		        {
		        	for(int i = ents.length - 1; i >= 0; i--)
		        	{
		        		if(ents[i] instanceof Player)
		        		{
		        			Player p = (Player)ents[i];
		        			p.sendMessage(String.format(Message_RemovedEntites, entry.getValue().size() - limit, eType));
		        		}
		        	}
		        }
		        for(int i = entry.getValue().size() - 1; i >= limit; i--)
		        {
		        	entry.getValue().get(i).remove();
		        }
		    }
		}
	}
	
	public static String getMobGroup(Entity entity)
	{
		if (entity instanceof Animals)
		{
			return "ANIMAL";
		}
		if (entity instanceof Monster)
		{
			return "MONSTER";
		}
		if (entity instanceof Ambient)
		{
			return "AMBIENT";
		}
		if (entity instanceof WaterMob)
		{
			return "WATER_MOB";
		}
		if (entity instanceof NPC)
		{
			return "NPC";
		}
		return "OTHER";
	}
}