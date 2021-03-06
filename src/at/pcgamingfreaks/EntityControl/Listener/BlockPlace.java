/*
 *   Copyright (C) 2019 GeorgH93
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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashSet;

public class BlockPlace implements Listener
{
	private final Message messageIronGolem, messageSnowGolem, messageWither;
	private final boolean checkIronGolem, checkSnowGolem, checkWither;
	private final HashSet<String> ignoreWorlds;

	public BlockPlace(EntityControl plugin)
	{
		messageIronGolem = plugin.getLanguage().getMessage("Build.IronGolem");
		messageSnowGolem = plugin.getLanguage().getMessage("Build.SnowGolem");
		messageWither = plugin.getLanguage().getMessage("Build.Wither");
		checkIronGolem = plugin.getConfiguration().getBuild("IronGolem");
		checkSnowGolem = plugin.getConfiguration().getBuild("SnowGolem");
		checkWither = plugin.getConfiguration().getBuild("Wither");
		ignoreWorlds = plugin.getConfiguration().getBuildIgnoreWorlds();
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();
		if(ignoreWorlds.contains(location.getWorld().getName().toLowerCase())) return;
		if(event.getBlock().getType() == Material.PUMPKIN || event.getBlock().getType() == Material.JACK_O_LANTERN)
		{
			if(checkIronGolem && !player.hasPermission("entitycontrol.build.irongolem")) handleGolem(location, Material.IRON_BLOCK, event, messageIronGolem, player);
			if(checkSnowGolem && !player.hasPermission("entitycontrol.build.snowgolem")) handleGolem(location, Material.SNOW_BLOCK, event, messageSnowGolem, player);
		}
		if(checkWither && (event.getBlock().getType() == Material.WITHER_SKELETON_SKULL || event.getBlock().getType() == Material.SKELETON_SKULL) && !player.hasPermission("entitycontrol.build.wither"))
		{
			handleGolem(location, Material.SOUL_SAND, event, messageWither, player);
		}
	}

	private static void handleGolem(Location location, Material material, Cancellable event, Message message, Player player)
	{
		if(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()).getType() == material
				&& location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 2, location.getBlockZ()).getType() == material)
		{
			event.setCancelled(true);
			message.send(player);
		}
	}
}