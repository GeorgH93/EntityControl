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

package at.pcgamingfreaks.EntityControl;

import at.pcgamingfreaks.Bukkit.Configuration;
import at.pcgamingfreaks.YamlFileManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Config extends Configuration
{
	private static final int CONFIG_VERSION = 2;
	
	public Config(EntityControl plugin)
	{
		super(plugin, CONFIG_VERSION, CONFIG_VERSION);
		languageKey = "Language.Language";
		languageUpdateKey = "Language.UpdateMode";
	}

	@Override
	protected void doUpdate()
	{
		// Nothing to update yet
	}

	@Override
	protected void doUpgrade(YamlFileManager oldConfig)
	{
		super.doUpgrade(oldConfig);
	}
	
	public boolean getBuildEnabled()
	{
		return getConfig().getBoolean("Build.Enable", true);
	}
	
	public boolean getBuild(String str)
	{
		return getConfig().getBoolean("Build." + str, true);
	}
	
	public boolean getSpawnEggEnabled()
	{
		return getConfig().getBoolean("Eggs.Spawn.Enable", true);
	}
	
	public Collection<Integer> getSpawnEggAllowedIDs()
	{
		Collection<Integer> allowedIds = new LinkedList<>();
		for(String id : getConfig().getStringList("Eggs.Spawn.AllowedIDs", new LinkedList<>()))
		{
			allowedIds.add(Integer.parseInt(id));
		}
		return allowedIds;
	}
	
	public boolean getSpawnEggTimedEnabled()
	{
		return getConfig().getBoolean("Eggs.Spawn.Timed.Enable", true);
	}
	
	public long getSpawnEggTimedInterval()
	{
		return getConfig().getLong("Eggs.Spawn.Timed.Interval", 60);
	}
	
	public int getSpawnEggTimedMexPerDay()
	{
		return getConfig().getInt("Eggs.Spawn.Timed.MaxPerDay", 3);
	}
	
	public long getSpawnEggTimedCleanInterval()
	{
		return getConfig().getLong("Eggs.Spawn.Timed.CleanInterval", 3600);
	}
	
	public boolean getChickenEggEnabled()
	{
		return getConfig().getBoolean("Eggs.Chicken.Enable", true);
	}
	
	public boolean getEggBlockCreativeOnly()
	{
		return getConfig().getBoolean("Eggs.BlockCreativeOnly", true);
	}
	
	public boolean getDispenserEnabled()
	{
		return getConfig().getBoolean("Dispenser.Enable", true);
	}
	
	public boolean getDispenserBlock(String str)
	{
		return getConfig().getBoolean("Dispenser.Block." + str, true);
	}
	
	public boolean getLimiterEnabled()
	{
		return getConfig().getBoolean("Limiter.Enable", true);
	}
	
	public boolean getLimiterEnabledOnSpawn()
	{
		return getConfig().getBoolean("Limiter.EnableOnSpawn", true);
	}
	
	public boolean getLimiterEnabledOnChunkLoad()
	{
		return getConfig().getBoolean("Limiter.EnableOnChunkLoad", true);
	}
	
	public boolean getLimiterSpawnReason(String str)
	{
		return getConfig().getBoolean("Limiter.spawn-reasons." + str, true);
	}
	
	public boolean getLimiterNotifyPlayers()
	{
		return getConfig().getBoolean("Limiter.NotifyPlayers", false);
	}
	
	public boolean getLimiterMaxEntitiesContains(String str)
	{
		return getConfig().getInt("Limiter.Entities." + str, -1) >= 0;
	}
	
	public int getLimiterMaxEntities(String str)
	{
		return getConfig().getInt("Limiter.Entities." + str, 10);
	}
	
	public int getLimiterCheckSurroundingChunks()
	{
		return getConfig().getInt("Limiter.CheckSurroundingChunks", 1);
	}
	
	public boolean getLimiterChunkRecheck()
	{
		return getConfig().getBoolean("Limiter.ChunkRecheck", false);
	}
	
	public long getLimiterChunkRecheckInterval()
	{
		return getConfig().getLong("Limiter.ChunkRecheckInterval", 300L);
	}
	
	private HashSet<String> strListToLowerCaseHashSet(List<String> sl)
	{
		HashSet<String> hs = new HashSet<>();
		for(String s : sl)
		{
			hs.add(s.toLowerCase());
		}
		return hs;
	}
	
	public HashSet<String> getLimiterIgnoreWorlds()
	{
		return strListToLowerCaseHashSet(getConfig().getStringList("Limiter.IgnoreWorlds", new LinkedList<>()));
	}
	
	public HashSet<String> getBuildIgnoreWorlds()
	{
		return strListToLowerCaseHashSet(getConfig().getStringList("Build.IgnoreWorlds", new LinkedList<>()));
	}
	
	public HashSet<String> getDispenserIgnoreWorlds()
	{
		return strListToLowerCaseHashSet(getConfig().getStringList("Dispenser.IgnoreWorlds", new LinkedList<>()));
	}
	
	public HashSet<String> getEggsIgnoreWorlds()
	{
		return strListToLowerCaseHashSet(getConfig().getStringList("Eggs.IgnoreWorlds", new LinkedList<>()));
	}
}