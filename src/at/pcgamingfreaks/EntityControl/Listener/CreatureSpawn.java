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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import at.pcgamingfreaks.EntityControl.EntityControl;

public class CreatureSpawn implements Listener
{
	private EntityControl plugin;
	
	private HashSet<String> IgnoreWorlds;
	
	public CreatureSpawn(EntityControl ec)
	{
		plugin = ec;
		IgnoreWorlds = plugin.config.getLimiterIgnoreWorlds();
	}
	
	@EventHandler
	public void onCreatureSpawnEvent(CreatureSpawnEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(IgnoreWorlds.contains(event.getLocation().getWorld().getName().toLowerCase()))
		{
			return;
		}
		String reason = event.getSpawnReason().toString();
		if(!plugin.config.getLimiterSpawnReason(reason))
		{
		    return;
		}
		plugin.checkChunks(event.getLocation());
	}
}