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

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import at.pcgamingfreaks.EntityControl.EntityControl;

public class ChunkLoad implements Listener
{
	private EntityControl plugin;
	
	private HashMap<Chunk, Integer> chunkTasks = new HashMap<>();
	private long interval;
	private boolean chunkRecheck, onLoadCheck;
	private HashSet<String> IgnoreWorlds;
	
	public ChunkLoad(EntityControl ec)
	{
		plugin = ec;
		chunkRecheck = plugin.config.GetLimiterChunkRecheck();
		interval = plugin.config.GetLimiterChunkRecheckInterval() * 20L;
		onLoadCheck = plugin.config.GetLimiterEnabledOnChunkLoad();
		IgnoreWorlds = plugin.config.GetLimiterIgnoreWorlds();
	}
	
	@EventHandler
	public void onChunkLoadEvent(ChunkLoadEvent event)
	{
		if(IgnoreWorlds.contains(event.getWorld().getName().toLowerCase()))
		{
			return;
		}
		if(chunkRecheck)
	    {
			inspectTask task = new inspectTask(event.getChunk());
			int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, task, interval, interval);
			task.setId(taskID);
			chunkTasks.put(event.getChunk(), taskID);
	    }
		if(onLoadCheck)
		{
			plugin.CheckChunk(event.getChunk());
		}
	}
	
	@EventHandler
	public void onChunkUnloadEvent(ChunkUnloadEvent e)
	{
		if(chunkTasks.containsKey(e.getChunk()))
	    {
			plugin.getServer().getScheduler().cancelTask(chunkTasks.get(e.getChunk()));
			chunkTasks.remove(e.getChunk());
	    }
	}
	
	class inspectTask extends BukkitRunnable
	{
		Chunk c;
		int taskID;
		
		public inspectTask(Chunk C)
		{
			c = C;
		}
    
		public void run()
		{
			if(!c.isLoaded())
			{
				plugin.getServer().getScheduler().cancelTask(taskID);
				return;
			}
			plugin.CheckChunk(c);
		}
    
		public void setId(int TaskID)
		{
			taskID = TaskID;
		}
	}
}