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

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitTask;

import at.pcgamingfreaks.EntityControl.EntityControl;

public class ChunkLoad implements Listener
{
	private EntityControl plugin;
	
	private HashMap<Chunk, BukkitTask> chunkTasks = new HashMap<>();
	private long interval;
	private boolean chunkRecheck, onLoadCheck;
	private HashSet<String> ignoreWorlds;
	
	public ChunkLoad(EntityControl plugin)
	{
		this.plugin = plugin;
		chunkRecheck = plugin.getConfiguration().getLimiterChunkRecheck();
		interval = plugin.getConfiguration().getLimiterChunkRecheckInterval() * 20L;
		onLoadCheck = plugin.getConfiguration().getLimiterEnabledOnChunkLoad();
		ignoreWorlds = plugin.getConfiguration().getLimiterIgnoreWorlds();
	}
	
	@EventHandler
	public void onChunkLoadEvent(ChunkLoadEvent event)
	{
		if(ignoreWorlds.contains(event.getWorld().getName().toLowerCase()))
		{
			return;
		}
		if(chunkRecheck)
	    {
			chunkTasks.put(event.getChunk(), plugin.getServer().getScheduler().runTaskTimer(plugin, new Checker(event.getChunk()), interval, interval));
	    }
		if(onLoadCheck)
		{
			plugin.checkChunk(event.getChunk());
		}
	}
	
	@EventHandler
	public void onChunkUnloadEvent(ChunkUnloadEvent e)
	{
		if(chunkTasks.containsKey(e.getChunk()))
	    {
		    chunkTasks.get(e.getChunk()).cancel();
			chunkTasks.remove(e.getChunk());
	    }
	}
	
	class Checker implements Runnable
	{
		private Chunk chunk;
		
		public Checker(Chunk chunk)
		{
			this.chunk = chunk;
		}

		@Override
		public void run()
		{
			if(!chunk.isLoaded())
			{
				chunkTasks.get(chunk).cancel();
				chunkTasks.remove(chunk);
				return;
			}
			plugin.checkChunk(chunk);
		}
	}
}