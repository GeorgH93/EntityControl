/*
* Copyright (C) 2014-2016 GeorgH93
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

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import at.pcgamingfreaks.EntityControl.EntityControl;

public class BlockPlace implements Listener
{
	private String messageIronGolem, messageSnowGolem, messageWither;
	private boolean checkIronGolem, checkSnowGolem, checkWither;
	private HashSet<String> ignoreWorlds;
	
	public BlockPlace(EntityControl plugin)
	{
		messageIronGolem = EntityControl.lang.get("Build.IronGolem");
		messageSnowGolem = EntityControl.lang.get("Build.SnowGolem");
		messageWither = EntityControl.lang.get("Build.Wither");
		checkIronGolem = plugin.config.getBuild("IronGolem");
		checkSnowGolem = plugin.config.getBuild("SnowGolem");
		checkWither = plugin.config.getBuild("Wither");
		ignoreWorlds = plugin.config.getBuildIgnoreWorlds();
	}
	
	@EventHandler(ignoreCancelled = true)
	public void OnBlockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
	    Location Loc = event.getBlock().getLocation();
	    if(ignoreWorlds.contains(Loc.getWorld().getName().toLowerCase()))
		{
			return;
		}
	    if(event.getBlock().getType() == Material.PUMPKIN || event.getBlock().getType() == Material.JACK_O_LANTERN)
	    {
		    if (checkIronGolem && !player.hasPermission("entitycontrol.build.irongolem"))
		    {
		    	if(Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()-1, Loc.getBlockZ()).getType() == Material.IRON_BLOCK
		    		&& Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()-2, Loc.getBlockZ()).getType() == Material.IRON_BLOCK)
		    	{
		    		event.setCancelled(true);
		    		player.sendMessage(messageIronGolem);
		    	}
		    }
		    if(checkSnowGolem && !player.hasPermission("entitycontrol.build.snowgolem"))
		    {
		    	if(Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()-1, Loc.getBlockZ()).getType() == Material.SNOW_BLOCK
			    		&& Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()-2, Loc.getBlockZ()).getType() == Material.SNOW_BLOCK)
		    	{
		    		event.setCancelled(true);
		    		player.sendMessage(messageSnowGolem);
		    	}
		    }
	    }
	    if(checkWither && !player.hasPermission("entitycontrol.build.wither"))
	    {
	    	if(((event.getBlock().getType() == Material.SKULL || event.getBlock().getType() == Material.SKULL_ITEM)
	    		&& Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY() - 1, Loc.getBlockZ()).getType() == Material.SOUL_SAND)
	    		|| (event.getBlock().getType() == Material.SOUL_SAND
	    		&& (Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()+1, Loc.getBlockZ()).getType() == Material.SKULL
	    		|| Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()+1, Loc.getBlockZ()).getType() == Material.SKULL_ITEM)))
	    	{
	    		event.setCancelled(true);
	    		player.sendMessage(messageWither);
	    	}
	    }
	}
}