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

package at.pcgamingfreaks.EntityControl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config
{
	private FileConfiguration config;
	private EntityControl EC;
	private static final int CONFIG_VERSION = 1;
	
	public Config(EntityControl ec)
	{
		EC = ec;
		LoadConfig();
	}
	
	public void Reload()
	{
		LoadConfig();
	}
	
	private void LoadConfig()
	{
		File file = new File(EC.getDataFolder(), "config.yml");
		if(!file.exists())
		{
			NewConfig(file);
		}
		else
		{
			config = YamlConfiguration.loadConfiguration(file);
			UpdateConfig(file);
		}
	}
	
	private void NewConfig(File file)
	{
		config = new YamlConfiguration();
		
		config.set("Language", "en");
		config.set("LanguageUpdateMode", "Overwrite");
		
		config.set("Eggs.Chicken.Enable",				true);
		config.set("Eggs.BlockCreativeOnly",			true);
		config.set("Eggs.Spawn.Enable",					true);
		config.set("Eggs.Spawn.AllowedIDs",				new ArrayList<Integer>());
		config.set("Eggs.Spawn.Timed.Enable",			false);
		config.set("Eggs.Spawn.Timed.Interval",			60); // In seconds
		config.set("Eggs.Spawn.Timed.MaxPerDay",		3);
		config.set("Eggs.Spawn.Timed.CleanInterval",	3600); // In seconds
		config.set("Eggs.IgnoreWorlds",					new ArrayList<String>());
		config.set("Build.Enable",						true);
		config.set("Build.Wither",						true);
		config.set("Build.SnowGolem",					true);
		config.set("Build.IronGolem",					true);
		config.set("Build.IgnoreWorlds",				new ArrayList<String>());
		config.set("Dispenser.Enable",					true);
		config.set("Dispenser.Block.ChickenEgg",		true);
		config.set("Dispenser.Block.SpawnEgg",			true);
		config.set("Dispenser.Block.dispenseSnowball",	true);
		config.set("Dispenser.Block.Fire",				true);
		config.set("Dispenser.Block.EXP_Bottle",		true);
		config.set("Dispenser.IgnoreWorlds",			new ArrayList<String>());
		config.set("Limiter.Enable",					true);
		config.set("Limiter.EnableOnSpawn",				true);
		config.set("Limiter.CheckSurroundingChunks",	1);
		config.set("Limiter.EnableOnChunkLoad",			true);
		config.set("Limiter.ChunkRecheck",				true);
		config.set("Limiter.ChunkRecheckInterval",		300); // In seconds
		config.set("Limiter.NotifyPlayers",				false);
		config.set("Limiter.spawn-reasons.NATURAL",		true);
		config.set("Limiter.spawn-reasons.JOCKEY",		true);
		config.set("Limiter.spawn-reasons.CHUNK_GEN",	true);
		config.set("Limiter.spawn-reasons.SPAWNER",		true);
		config.set("Limiter.spawn-reasons.EGG",			true);
		config.set("Limiter.spawn-reasons.SPAWNER_EGG",	true);
		config.set("Limiter.spawn-reasons.LIGHTNING",	true);
		config.set("Limiter.spawn-reasons.BED",			true); // Deprecated Spawn Reason
		config.set("Limiter.spawn-reasons.BUILD_SNOWMAN",	true); // Deprecated Spawn Reason
		config.set("Limiter.spawn-reasons.BUILD_IRONGOLEM",	true); // Deprecated Spawn Reason
		config.set("Limiter.spawn-reasons.BUILD_WITHER",	true); // Deprecated Spawn Reason
		config.set("Limiter.spawn-reasons.VILLAGE_DEFENSE",	true); // Deprecated Spawn Reason
		config.set("Limiter.spawn-reasons.VILLAGE_INVASION",true); // Deprecated Spawn Reason
		config.set("Limiter.spawn-reasons.BREEDING",		true); // Deprecated Spawn Reason
		config.set("Limiter.spawn-reasons.SLIME_SPLIT",		true); // Deprecated Spawn Reason
		config.set("Limiter.spawn-reasons.REINFORCEMENTS",	true); // Deprecated Spawn Reason
		config.set("Limiter.spawn-reasons.CUSTOM",		true); // Deprecated Spawn Reason
		config.set("Limiter.spawn-reasons.DEFAULT",		true); // Deprecated Spawn Reason
		config.set("Limiter.Entities.ANIMAL",			10);
		config.set("Limiter.Entities.MONSTER",			10);
		config.set("Limiter.Entities.WATER_MOB",		10);
		config.set("Limiter.Entities.AMBIENT",			10);
		config.set("Limiter.Entities.NPC",				10);
		config.set("Limiter.Entities.OTHER",			10);
		config.set("Limiter.IgnoreWorlds",				new ArrayList<String>());
		
		config.set("Version",CONFIG_VERSION);
		
		try
		{
			config.save(file);
			EC.log.info("Config File has been generated.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private boolean UpdateConfig(File file)
	{
		switch(config.getInt("Version"))
		{
			case 0: break;
			case CONFIG_VERSION: return false;
			default: EC.log.info("Config File Version newer than expected!"); return false;
		}
		config.set("Version", CONFIG_VERSION);
		try
		{
			config.save(file);
			EC.log.info("Config File has been updated.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String GetLanguage()
	{
		return config.getString("Language");
	}
	
	public String GetLanguageUpdateMode()
	{
		return config.getString("LanguageUpdateMode");
	}
	
	public boolean GetBuildEnabled()
	{
		return config.getBoolean("Build.Enable");
	}
	
	public boolean GetBuild(String str)
	{
		return config.getBoolean("Build." + str);
	}
	
	public boolean GetSpawnEggEnabled()
	{
		return config.getBoolean("Eggs.Spawn.Enable");
	}
	
	public List<?> GetSpawnEggAllowedIDs()
	{
		return config.getList("Eggs.Spawn.AllowedIDs");
	}
	
	public boolean GetSpawnEggTimedEnabled()
	{
		return config.getBoolean("Eggs.Spawn.Timed.Enable");
	}
	
	public long GetSpawnEggTimedInterval()
	{
		return config.getLong("Eggs.Spawn.Timed.Interval");
	}
	
	public int GetSpawnEggTimedMexPerDay()
	{
		return config.getInt("Eggs.Spawn.Timed.MaxPerDay");
	}
	
	public long GetSpawnEggTimedCleanInterval()
	{
		return config.getLong("Eggs.Spawn.Timed.CleanInterval");
	}
	
	public boolean GetChickenEggEnabled()
	{
		return config.getBoolean("Eggs.Chicken.Enable");
	}
	
	public boolean GetEggBlockCreativeOnly()
	{
		return config.getBoolean("Eggs.BlockCreativeOnly");
	}
	
	public boolean GetDispenserEnabled()
	{
		return config.getBoolean("Dispenser.Enable");
	}
	
	public boolean GetDispenserBlock(String str)
	{
		return config.getBoolean("Dispenser.Block." + str);
	}
	
	public boolean GetLimiterEnabled()
	{
		return config.getBoolean("Limiter.Enable");
	}
	
	public boolean GetLimiterEnabledOnSpawn()
	{
		return config.getBoolean("Limiter.EnableOnSpawn");
	}
	
	public boolean GetLimiterEnabledOnChunkLoad()
	{
		return config.getBoolean("Limiter.EnableOnChunkLoad");
	}
	
	public boolean GetLimiterSpawnReason(String str)
	{
		return config.getBoolean("Limiter.spawn-reasons." + str);
	}
	
	public boolean GetLimiterNotifyPlayers()
	{
		return config.getBoolean("Limiter.NotifyPlayers", false);
	}
	
	public boolean GetLimiterMaxEntitiesContains(String str)
	{
		return config.contains("Limiter.Entities." + str);
	}
	
	public int GetLimiterMaxEntities(String str)
	{
		return config.getInt("Limiter.Entities." + str);
	}
	
	public int GetLimiterCheckSurroundingChunks()
	{
		return config.getInt("Limiter.CheckSurroundingChunks", 1);
	}
	
	public boolean GetLimiterChunkRecheck()
	{
		return config.getBoolean("Limiter.ChunkRecheck", false);
	}
	
	public long GetLimiterChunkRecheckInterval()
	{
		return config.getLong("Limiter.ChunkRecheckInterval", 300L);
	}
	
	private HashSet<String> StrListToLowerCaseHashSet(List<String> sl)
	{
		HashSet<String> hs = new HashSet<String>();
		for(String s : sl)
		{
			hs.add(s.toLowerCase());
		}
		return hs;
	}
	
	public HashSet<String> GetLimiterIgnoreWorlds()
	{
		return StrListToLowerCaseHashSet(config.getStringList("Limiter.IgnoreWorlds"));
	}
	
	public HashSet<String> GetBuildIgnoreWorlds()
	{
		return StrListToLowerCaseHashSet(config.getStringList("Build.IgnoreWorlds"));
	}
	
	public HashSet<String> GetDispenserIgnoreWorlds()
	{
		return StrListToLowerCaseHashSet(config.getStringList("Dispenser.IgnoreWorlds"));
	}
	
	public HashSet<String> GetEggsIgnoreWorlds()
	{
		return StrListToLowerCaseHashSet(config.getStringList("Eggs.IgnoreWorlds"));
	}
}