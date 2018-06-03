/*
* Copyright (C) 2014-2016, 2018 GeorgH93
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

import at.pcgamingfreaks.Bukkit.Message.Message;
import at.pcgamingfreaks.EntityControl.EntityControl;
import at.pcgamingfreaks.EntityControl.MobType;

public class PlayerInteract implements Listener
{
	private boolean blockCreativeOnly, timed;
	private long interval;
	private int maxPerDay;
	private Date lastClean;
	private HashSet<Integer> allowedIDs = new HashSet<>();
	private HashMap<UUID, Integer> count = new HashMap<>();
	private HashMap<UUID, Date> last = new HashMap<>();
	private HashSet<String> ignoredWorlds;
	private Message messageSpawnEgg, messageCooldown, messageDayMax;
	
	public PlayerInteract(EntityControl plugin)
	{
		blockCreativeOnly = plugin.getConfiguration().getEggBlockCreativeOnly();
		for(Object o : plugin.getConfiguration().getSpawnEggAllowedIDs())
		{
			try
			{
				allowedIDs.add(Integer.parseInt(o.toString(), 10));
			}
			catch(Exception ignored) {}
		}
		timed = plugin.getConfiguration().getSpawnEggTimedEnabled();
		interval = plugin.getConfiguration().getSpawnEggTimedInterval() * 1000L;
		long cleanInterval = plugin.getConfiguration().getSpawnEggTimedCleanInterval() * 20L;
		maxPerDay = plugin.getConfiguration().getSpawnEggTimedMexPerDay();
		if(timed)
		{
			lastClean = new Date();
			plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
				if(lastClean.getTime() / 86400000L != (new Date()).getTime() / 86400000L)
				{
					lastClean = new Date();
					count.clear();
				}
			}, cleanInterval, cleanInterval);
		}
		ignoredWorlds = plugin.getConfiguration().getLimiterIgnoreWorlds();
		messageSpawnEgg = plugin.getLanguage().getMessage("Egg.SpawnEgg");
		messageCooldown = plugin.getLanguage().getMessage("Egg.TimedCooldown");
		messageDayMax = plugin.getLanguage().getMessage("Egg.TimedMaxPerDay");
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(ignoredWorlds.contains(event.getPlayer().getLocation().getWorld().getName().toLowerCase())) return;
		Player p = event.getPlayer();
		if(!blockCreativeOnly || p.getGameMode() == GameMode.CREATIVE)
		{
			if(p.getItemInHand().getType() == Material.MONSTER_EGG || p.getItemInHand().getType() == Material.MONSTER_EGGS)
			{
				MobType type = MobType.getMobTypeFromId(p.getItemInHand().getDurability());
				if(allowedIDs.contains(type.getEntityId()))
				{
					// Timed Check
					if(timed)
					{
						Integer ct = count.get(p.getUniqueId());
						int c = 0;
						if(ct != null)
						{
							c = ct;
						}
						if(c >= maxPerDay && !p.hasPermission("entitycontrol.egg.timed.nolimit"))
						{
							event.setCancelled(true);
							messageDayMax.send(p);
						}
						else
						{
							Date temp = last.get(p.getUniqueId());
							if(temp == null || temp.getTime() < (new Date().getTime() - interval) || p.hasPermission("entitycontrol.egg.timed.nocd"))
							{
								c++;
								count.put(p.getUniqueId(), c);
								last.put(p.getUniqueId(), new Date());
							}
							else
							{
								event.setCancelled(true);
								messageCooldown.send(p);
							}
						}
					}
				}
				else
				{
					if(!p.hasPermission("entitycontrol.egg." + type.getCategory().name().toLowerCase() + "." + type.getPermName()))
					{
						event.setCancelled(true);
						messageSpawnEgg.send(p, type.getName());
					}
				}
			}
		}
	}
}