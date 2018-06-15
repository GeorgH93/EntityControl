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

import at.pcgamingfreaks.EntityControl.EntityControl;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public class BlockDispense implements Listener
{
	private final boolean dispenseSnowball;
	private final HashSet<String> ignoredWorlds;
	private final HashSet<Material> blockedMaterials = new HashSet<>();

	public BlockDispense(EntityControl plugin)
	{
		dispenseSnowball = plugin.getConfiguration().getDispenserBlock("DispenseSnowball");
		if(plugin.getConfiguration().getDispenserBlock("ChickenEgg")) blockedMaterials.add(Material.EGG);
		if(plugin.getConfiguration().getDispenserBlock("SpawnEgg")) blockedMaterials.add(Material.MONSTER_EGG);
		if(plugin.getConfiguration().getDispenserBlock("EXP_Bottle")) blockedMaterials.add(Material.EXP_BOTTLE);
		if(plugin.getConfiguration().getDispenserBlock("Fire"))
		{
			blockedMaterials.add(Material.FIRE);
			blockedMaterials.add(Material.FIREBALL);
		}
		if(plugin.getConfiguration().getDispenserBlock("Pumpkin"))
		{
			blockedMaterials.add(Material.PUMPKIN);
			blockedMaterials.add(Material.JACK_O_LANTERN);
		}
		ignoredWorlds = plugin.getConfiguration().getDispenserIgnoreWorlds();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDispense(BlockDispenseEvent event)
	{
		if(ignoredWorlds.contains(event.getBlock().getLocation().getWorld().getName().toLowerCase())) return;
		if(blockedMaterials.contains(event.getItem().getType()))
		{
			if(dispenseSnowball && (event.getItem().getType() == Material.EGG || event.getItem().getType() == Material.MONSTER_EGG))
			{
				event.setItem(new ItemStack(Material.SNOW_BALL));
			}
			else
			{
				event.setCancelled(true);
			}
		}
	}
}