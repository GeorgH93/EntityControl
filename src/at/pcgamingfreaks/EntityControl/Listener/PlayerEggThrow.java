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

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;

import at.pcgamingfreaks.EntityControl.EntityControl;

public class PlayerEggThrow implements Listener
{
	private boolean blockCreativeOnly;
	private String messageNoChickenEgg;
	private HashSet<String> ignoredWorlds;
	
	public PlayerEggThrow(EntityControl plugin)
	{
		blockCreativeOnly = plugin.config.getEggBlockCreativeOnly();
		messageNoChickenEgg = EntityControl.lang.get("Egg.ChickenEgg");
		ignoredWorlds = plugin.config.getEggsIgnoreWorlds();
	}
	
	@EventHandler
	public void onEggThrow(PlayerEggThrowEvent event)
	{
		//noinspection SpellCheckingInspection
		if(!event.getPlayer().hasPermission("entitycontrol.chickenegg"))
		{
			if(ignoredWorlds.contains(event.getPlayer().getLocation().getWorld().getName().toLowerCase()))
			{
				return;
			}
			if(!blockCreativeOnly || event.getPlayer().getGameMode() == GameMode.CREATIVE)
			{
				event.setHatching(false);
				event.getPlayer().sendMessage(messageNoChickenEgg);
			}
		}
	}
}