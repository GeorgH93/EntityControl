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

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

import at.pcgamingfreaks.EntityControl.EntityControl;

public class BlockDispense implements Listener
{
	private boolean checkChickenEgg, checkSpawnEgg, dispenseSnowball, checkEXP_Bottle, checkFireCharge, checkPumpkin;
	private HashSet<String> ignoreWorlds;
	
	public BlockDispense(EntityControl plugin)
	{
		checkChickenEgg  = plugin.config.GetDispenserBlock("ChickenEgg");
		checkSpawnEgg    = plugin.config.GetDispenserBlock("SpawnEgg");
		dispenseSnowball = plugin.config.GetDispenserBlock("dispenseSnowball");
		checkEXP_Bottle  = plugin.config.GetDispenserBlock("EXP_Bottle");
		checkFireCharge  = plugin.config.GetDispenserBlock("Fire");
		checkPumpkin     = plugin.config.GetDispenserBlock("Pumpkin");
		ignoreWorlds     = plugin.config.GetDispenserIgnoreWorlds();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
	public void onDispense(BlockDispenseEvent event)
	{
		if(ignoreWorlds.contains(event.getBlock().getLocation().getWorld().getName().toLowerCase()))
		{
			return;
		}
		if((checkChickenEgg && event.getItem().getType() == Material.EGG) || (checkSpawnEgg && event.getItem().getType() == Material.MONSTER_EGG))
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
		else if((checkEXP_Bottle && event.getItem().getType() == Material.EXP_BOTTLE) || (checkFireCharge && (event.getItem().getType() == Material.FIRE || event.getItem().getType() == Material.FIREBALL)) ||
				(checkPumpkin && (event.getItem().getType() == Material.PUMPKIN || event.getItem().getType() == Material.JACK_O_LANTERN)))
		{
			event.setCancelled(true);
		}
	}
}