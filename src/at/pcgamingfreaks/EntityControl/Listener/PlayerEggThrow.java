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

import at.pcgamingfreaks.Bukkit.Message.Message;
import at.pcgamingfreaks.EntityControl.EntityControl;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;

import java.util.HashSet;

public class PlayerEggThrow implements Listener
{
	private final boolean blockCreativeOnly;
	private final Message messageNoChickenEgg;
	private final HashSet<String> ignoredWorlds;

	public PlayerEggThrow(EntityControl plugin)
	{
		blockCreativeOnly = plugin.getConfiguration().getEggBlockCreativeOnly();
		messageNoChickenEgg = plugin.getLanguage().getMessage("Egg.ChickenEgg");
		ignoredWorlds = plugin.getConfiguration().getEggsIgnoreWorlds();
	}

	@EventHandler
	public void onEggThrow(PlayerEggThrowEvent event)
	{
		//noinspection SpellCheckingInspection
		if(!event.getPlayer().hasPermission("entitycontrol.chickenegg"))
		{
			if(ignoredWorlds.contains(event.getPlayer().getLocation().getWorld().getName().toLowerCase())) return;
			if(!blockCreativeOnly || event.getPlayer().getGameMode() == GameMode.CREATIVE)
			{
				event.setHatching(false);
				messageNoChickenEgg.send(event.getPlayer());
			}
		}
	}
}