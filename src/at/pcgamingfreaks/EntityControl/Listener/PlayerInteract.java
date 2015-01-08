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

package at.pcgamingfreaks.EntityControl.Listener;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import at.pcgamingfreaks.EntityControl.EntityControl;
import at.pcgamingfreaks.EntityControl.MobType;

public class PlayerInteract implements Listener
{
	private EntityControl plugin;
	
	private boolean BlockCreativeOnly, Timed;
	private long interval, cleanInterval;
	private int maxperday;
	private Date lastClean;
	private HashSet<Integer> AllowedIDs = new HashSet<Integer>();
	private HashMap<UUID, Integer> count = new HashMap<UUID, Integer>();
	private HashMap<UUID, Date> last = new HashMap<UUID, Date>();
	private HashSet<String> IgnoreWorlds;
	private String Message_SpawnEgg, Message_Cooldown, Message_DayMax;
	
	public PlayerInteract(EntityControl ec)
	{
		plugin = ec;
		
		BlockCreativeOnly = plugin.config.GetEggBlockCreativeOnly();
		for(Object o : plugin.config.GetSpawnEggAllowedIDs())
		{
			try
			{
				AllowedIDs.add(Integer.parseInt(o.toString(), 10));
			}
			catch(Exception e) {}
		}
		Timed = plugin.config.GetSpawnEggTimedEnabled();
		interval = plugin.config.GetSpawnEggTimedInterval() * 1000L;
		cleanInterval = plugin.config.GetSpawnEggTimedCleanInterval() * 20L;
		maxperday = plugin.config.GetSpawnEggTimedMexPerDay();
		if(Timed)
		{
			lastClean = new Date();
			plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,
				new Runnable()
				{
					@Override
					public void run()
					{
						if(lastClean.getTime() / 86400000L != (new Date()).getTime() / 86400000L)
						{
							lastClean = new Date();
							count.clear();
						}
					}
				}, cleanInterval, cleanInterval);
		}
		IgnoreWorlds = plugin.config.GetLimiterIgnoreWorlds();
		Message_SpawnEgg = EntityControl.lang.Get("Egg.SpawnEgg");
		Message_Cooldown = EntityControl.lang.Get("Egg.TimedCooldown");
		Message_DayMax = EntityControl.lang.Get("Egg.TimedMaxPerDay");
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		if(IgnoreWorlds.contains(event.getPlayer().getLocation().getWorld().getName().toLowerCase()))
		{
			return;
		}
		Player p = event.getPlayer();
		if(!BlockCreativeOnly || p.getGameMode() == GameMode.CREATIVE)
		{
			if(p.getItemInHand().getType() == Material.MONSTER_EGG || p.getItemInHand().getType() == Material.MONSTER_EGGS)
			{
				MobType type = MobType.getMobTypeFromId(p.getItemInHand().getDurability());
				if(AllowedIDs.contains(type.getEntityId()))
				{
					// Timed Check
					if(Timed)
					{
						Integer ct = count.get(p.getUniqueId());
						int c = 0;
						if(ct != null)
						{
							c = ct.intValue();
						}
						if(c >= maxperday && !p.hasPermission("entitycontrol.egg.timed.nolimit"))
						{
							event.setCancelled(true);
							p.sendMessage(Message_DayMax);
						}
						else
						{
							Date temp = last.get(p.getUniqueId());
							if(temp == null || temp.getTime() < (new Date().getTime() - interval) || p.hasPermission("entitycontrol.egg.timed.nocd"))
							{
								c++;
								count.put(p.getUniqueId(), new Integer(c));
								last.put(p.getUniqueId(), new Date());
							}
							else
							{
								event.setCancelled(true);
								p.sendMessage(Message_Cooldown);
							}
						}
					}
				}
				else
				{
					if(!p.hasPermission("entitycontrol.egg." + type.getCategory().name().toLowerCase() + "." + type.getPermName()))
					{
						event.setCancelled(true);
						p.sendMessage(String.format(Message_SpawnEgg, type.getName()));
					}
				}
			}
		}
	}
}