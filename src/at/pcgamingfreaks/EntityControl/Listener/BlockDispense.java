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

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

import at.pcgamingfreaks.EntityControl.EntityControl;

public class BlockDispense implements Listener
{
	private EntityControl plugin;
	
	private boolean ChickenEgg, SpawnEgg, dispenseSnowball, EXP_Bottle, FireCharge, Pumpkin;
	private HashSet<String> IgnoreWorlds;
	
	public BlockDispense(EntityControl ec)
	{
		plugin = ec;
		
		ChickenEgg		 = plugin.config.GetDispenserBlock("ChickenEgg");
		SpawnEgg		 = plugin.config.GetDispenserBlock("SpawnEgg");
		dispenseSnowball = plugin.config.GetDispenserBlock("dispenseSnowball");
		EXP_Bottle		 = plugin.config.GetDispenserBlock("EXP_Bottle");
		FireCharge		 = plugin.config.GetDispenserBlock("Fire");
		Pumpkin          = plugin.config.GetDispenserBlock("Pumpkin");
		IgnoreWorlds	 = plugin.config.GetDispenserIgnoreWorlds();
	}
	
	@EventHandler
	public void onDispense(BlockDispenseEvent event)
	{
		if(IgnoreWorlds.contains(event.getBlock().getLocation().getWorld().getName().toLowerCase()))
		{
			return;
		}
		if((ChickenEgg && event.getItem().getType() == Material.EGG) || (SpawnEgg && event.getItem().getType() == Material.MONSTER_EGG))
		{
			if(dispenseSnowball)
			{
				event.setItem(new ItemStack(Material.SNOW_BALL));
			}
			else
			{
				event.setCancelled(true);
			}
		}
		else if((EXP_Bottle && event.getItem().getType() == Material.EXP_BOTTLE) || (FireCharge && (event.getItem().getType() == Material.FIRE || event.getItem().getType() == Material.FIREBALL)) ||
				(Pumpkin && (event.getItem().getType() == Material.PUMPKIN || event.getItem().getType() == Material.JACK_O_LANTERN)))
		{
			event.setCancelled(true);
		}
	}
}