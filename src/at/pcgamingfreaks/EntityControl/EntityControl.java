/*
*   Copyright (C) 2014-2016, 2018 GeorgH93
*
*   This program is free software: you can redistribute it and/or modify
*   it under the terms of the GNU General Public License as published by
*   the Free Software Foundation, either version 3 of the License, or
*   (at your option) any later version.
*
*   This program is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
*   GNU General Public License for more details.
*
*   You should have received a copy of the GNU General Public License
*   along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package at.pcgamingfreaks.EntityControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.WaterMob;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import at.pcgamingfreaks.Bukkit.Message.Message;
import at.pcgamingfreaks.EntityControl.Listener.*;

public class EntityControl extends JavaPlugin
{
	private static EntityControl instance;
	private Config config = new Config(this);
	private Language lang;
	private boolean notifyPlayers;
	private int surroundingChunks;
	private Message messageRemovedEntites;

	@Override
	public void onEnable()
	{
		lang = new Language(this);

		notifyPlayers = config.getLimiterNotifyPlayers();
		surroundingChunks = config.getLimiterCheckSurroundingChunks();
		messageRemovedEntites = lang.getMessage("RemovedEntites");
		
		registerEvents();

		instance = this;
		getLogger().info("Entity Control finished loading and is now watching all the entitys");
	}

	public void registerEvents()
	{
		if(config.getBuildEnabled()) getServer().getPluginManager().registerEvents(new BlockPlace(this), this);
		if(config.getDispenserEnabled()) getServer().getPluginManager().registerEvents(new BlockDispense(this), this);
		if(config.getChickenEggEnabled()) getServer().getPluginManager().registerEvents(new PlayerEggThrow(this), this);
		if(config.getSpawnEggEnabled()) getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
		if(config.getLimiterEnabled())
		{
			if(config.getLimiterEnabledOnSpawn()) getServer().getPluginManager().registerEvents(new CreatureSpawn(this), this);
			getServer().getPluginManager().registerEvents(new ChunkLoad(this), this);
		}
	}

	@Override
	public void onDisable()
	{
		HandlerList.unregisterAll(this);
	    getServer().getScheduler().cancelTasks(this);
		getLogger().info("All running tasks have been stopped. Plugin disabled.");
	}

	public Config getConfiguration()
	{
		return config;
	}

	public Language getLanguage()
	{
		return lang;
	}

	public static EntityControl getInstance()
	{
		return instance;
	}

	public void checkChunks(Location location)
	{
		Chunk c = location.getChunk();
		checkChunk(c);
		if(surroundingChunks > 0)
		{
			World w = location.getWorld();
		    for(int x = c.getX() + surroundingChunks; x >= c.getX() - surroundingChunks; x--)
		    {
		    	for(int z = c.getZ() + surroundingChunks; z >= c.getZ() - surroundingChunks; z--)
		    	{
		    		checkChunk(w.getChunkAt(x, z));
		        }
		    }
		}
	}
	
	public void checkChunk(Chunk chunk)
	{
		if(!chunk.isLoaded())
		{
			return;
		}
		Entity[] entities = chunk.getEntities();
		HashMap<String, ArrayList<Entity>> types = new HashMap<>();
	    for(int i = entities.length - 1; i >= 0; i--)
	    {
	    	if(entities[i] instanceof Player)
	    	{
	    		continue;
	    	}
		    String entityType = entities[i].getType().toString();
		    String entityGroupString = getMobGroup(entities[i]);
		    if(config.getLimiterMaxEntitiesContains(entityType))
		    {
		    	if (!types.containsKey(entityType))
		    	{
		    		types.put(entityType, new ArrayList<>());
		        }
		        types.get(entityType).add(entities[i]);
		    }
		    if(config.getLimiterMaxEntitiesContains(entityGroupString))
		    {
		    	if (!types.containsKey(entityGroupString))
		    	{
		    		types.put(entityGroupString, new ArrayList<>());
		        }
		        types.get(entityGroupString).add(entities[i]);
		    }
	    }
		for(Map.Entry<String, ArrayList<Entity>> entry : types.entrySet())
		{
			String eType = entry.getKey();
		    int limit = config.getLimiterMaxEntities(eType);
		    if(entry.getValue().size() > limit)
		    {
		        if(notifyPlayers)
		        {
		        	for(int i = entities.length - 1; i >= 0; i--)
		        	{
		        		if(entities[i] instanceof Player)
		        		{
					        messageRemovedEntites.send(entities[i], entry.getValue().size() - limit, eType);
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
		if (entity instanceof Animals) return "ANIMAL";
		if (entity instanceof Monster) return "MONSTER";
		if (entity instanceof Ambient) return "AMBIENT";
		if (entity instanceof WaterMob) return "WATER_MOB";
		if (entity instanceof NPC) return "NPC";
		return "OTHER";
	}
}