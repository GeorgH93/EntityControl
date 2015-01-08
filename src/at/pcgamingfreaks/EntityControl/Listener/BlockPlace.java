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
	private EntityControl plugin;
	
	private String IronGolem, SnowGolem, Wither;
	private boolean ironGolem, snowGolem, wither;
	private HashSet<String> IgnoreWorlds;
	
	public BlockPlace(EntityControl ec)
	{
		plugin = ec;
		
		IronGolem = EntityControl.lang.Get("Build.IronGolem");
		SnowGolem = EntityControl.lang.Get("Build.SnowGolem");
		Wither    = EntityControl.lang.Get("Build.Wither");
		ironGolem = plugin.config.GetBuild("IronGolem");
		snowGolem = plugin.config.GetBuild("SnowGolem");
		wither = plugin.config.GetBuild("Wither");
		IgnoreWorlds = plugin.config.GetBuildIgnoreWorlds();
	}
	
	@EventHandler
	public void OnBlockPlace(BlockPlaceEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		Player player = event.getPlayer();
	    Location Loc = event.getBlock().getLocation();
	    if(IgnoreWorlds.contains(Loc.getWorld().getName().toLowerCase()))
		{
			return;
		}
	    if(event.getBlock().getType() == Material.PUMPKIN || event.getBlock().getType() == Material.JACK_O_LANTERN)
	    {
		    if (ironGolem && !player.hasPermission("entitycontrol.build.irongolem"))
		    {
		    	if(Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()-1, Loc.getBlockZ()).getType() == Material.IRON_BLOCK
		    		&& Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()-2, Loc.getBlockZ()).getType() == Material.IRON_BLOCK)
		    	{
		    		event.setCancelled(true);
		    		player.sendMessage(IronGolem);
		    	}
		    }
		    if(snowGolem && !player.hasPermission("entitycontrol.build.snowgolem"))
		    {
		    	if(Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()-1, Loc.getBlockZ()).getType() == Material.SNOW_BLOCK
			    		&& Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()-2, Loc.getBlockZ()).getType() == Material.SNOW_BLOCK)
		    	{
		    		event.setCancelled(true);
		    		player.sendMessage(SnowGolem);
		    	}
		    }
	    }
	    if(wither && !player.hasPermission("entitycontrol.build.wither"))
	    {
	    	if(((event.getBlock().getType() == Material.SKULL || event.getBlock().getType() == Material.SKULL_ITEM)
	    		&& Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY() - 1, Loc.getBlockZ()).getType() == Material.SOUL_SAND)
	    		|| (event.getBlock().getType() == Material.SOUL_SAND
	    		&& (Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()+1, Loc.getBlockZ()).getType() == Material.SKULL
	    		|| Loc.getWorld().getBlockAt(Loc.getBlockX(), Loc.getBlockY()+1, Loc.getBlockZ()).getType() == Material.SKULL_ITEM)))
	    	{
	    		event.setCancelled(true);
	    		player.sendMessage(Wither);
	    	}
	    }
	}
}