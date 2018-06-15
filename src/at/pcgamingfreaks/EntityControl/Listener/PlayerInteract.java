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

package at.pcgamingfreaks.EntityControl.Listener;

import at.pcgamingfreaks.Bukkit.Message.Message;
import at.pcgamingfreaks.EntityControl.EntityControl;
import at.pcgamingfreaks.EntityControl.MobType;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class PlayerInteract implements Listener
{
	private final boolean blockCreativeOnly, timed;
	private final long interval;
	private final int maxPerDay;
	private final Collection<Integer> allowedIDs;
	private final HashMap<UUID, Integer> countUsed = new HashMap<>();
	private final HashMap<UUID, Long> lastUsed = new HashMap<>();
	private final HashSet<String> ignoredWorlds;
	private final Message messageSpawnEgg, messageCooldown, messageDayMax;

	public PlayerInteract(EntityControl plugin)
	{
		blockCreativeOnly = plugin.getConfiguration().getEggBlockCreativeOnly();
		allowedIDs = plugin.getConfiguration().getSpawnEggAllowedIDs();
		timed = plugin.getConfiguration().getSpawnEggTimedEnabled();
		interval = plugin.getConfiguration().getSpawnEggTimedInterval() * 1000L;
		maxPerDay = plugin.getConfiguration().getSpawnEggTimedMexPerDay();
		ignoredWorlds = plugin.getConfiguration().getLimiterIgnoreWorlds();
		messageSpawnEgg = plugin.getLanguage().getMessage("Egg.SpawnEgg");
		messageCooldown = plugin.getLanguage().getMessage("Egg.TimedCooldown");
		messageDayMax = plugin.getLanguage().getMessage("Egg.TimedMaxPerDay");

		if(timed) plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, countUsed::clear, plugin.getConfiguration().getSpawnEggTimedCleanInterval(), plugin.getConfiguration().getSpawnEggTimedCleanInterval());
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(ignoredWorlds.contains(event.getPlayer().getLocation().getWorld().getName().toLowerCase())) return;
		final Player player = event.getPlayer();
		if(!blockCreativeOnly || player.getGameMode() == GameMode.CREATIVE)
		{
			if(player.getItemInHand().getType() == Material.MONSTER_EGG || player.getItemInHand().getType() == Material.MONSTER_EGGS)
			{
				MobType type = MobType.getMobTypeFromId(player.getItemInHand().getDurability());
				if(allowedIDs.contains(type.getEntityId()))
				{
					// Timed Check
					if(timed)
					{
						Integer used = countUsed.get(player.getUniqueId());
						int timesUsed = (used != null) ? used : 0;
						if(timesUsed >= maxPerDay && !player.hasPermission("entitycontrol.egg.timed.nolimit"))
						{
							event.setCancelled(true);
							messageDayMax.send(player);
						}
						else
						{
							Long lastUseTime = lastUsed.get(player.getUniqueId());
							if(lastUseTime == null || lastUseTime < (System.currentTimeMillis() - interval) || player.hasPermission("entitycontrol.egg.timed.nocd"))
							{
								timesUsed++;
								countUsed.put(player.getUniqueId(), timesUsed);
								lastUsed.put(player.getUniqueId(), System.currentTimeMillis());
							}
							else
							{
								event.setCancelled(true);
								messageCooldown.send(player);
							}
						}
					}
				}
				else if(!player.hasPermission("entitycontrol.egg." + type.getCategory().name().toLowerCase() + "." + type.getPermName()))
				{
					event.setCancelled(true);
					messageSpawnEgg.send(player, type.getName());
				}
			}
		}
	}
}