/*
 *   Copyright (C) 2014-2016 GeorgH93
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

package at.pcgamingfreaks.EntityControl;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;

public class Language
{
	private EntityControl plugin;
	private FileConfiguration lang;
	private static final int LANG_VERSION = 1;

	public Language(EntityControl plugin)
	{
		this.plugin = plugin;
		loadFile();
	}
	
	public String get(String Option)
	{
		return lang.getString("Language." + Option);
	}
	
	private void loadFile()
	{
		File file = new File(plugin.getDataFolder() + File.separator + "Lang", plugin.config.getLanguage()+".yml");
		if(!file.exists())
		{
			extractLangFile(file);
		}
		lang = YamlConfiguration.loadConfiguration(file);
		updateLangFile(file);
	}
	
	private void extractLangFile(File Target)
	{
		try
		{
			plugin.saveResource("Lang" + File.separator + plugin.config.getLanguage() + ".yml", true);
		}
		catch(Exception ex)
		{
			try
			{
				File file_en = new File(plugin.getDataFolder() + File.separator + "Lang", "en.yml");
				if(!file_en.exists())
				{
					plugin.saveResource("Lang" + File.separator + "en.yml", true);
				}
				Files.copy(file_en, Target);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private boolean updateLangFile(File file)
	{
		if(lang.getInt("Version") != LANG_VERSION)
		{
			if(plugin.config.getLanguageUpdateMode().equalsIgnoreCase("overwrite"))
			{
				extractLangFile(file);
				loadFile();
				plugin.log.info(get("Console.LangUpdated"));
				return true;
			}
			else
			{
				switch(lang.getInt("Version"))
				{
					case 0:
						break;
					default: plugin.log.warning("Language File Version newer than expected!"); return false;
				}
				lang.set("Version", LANG_VERSION);
				try 
				{
					lang.save(file);
					plugin.log.info(get("Console.LangUpdated"));
				}
		  	  	catch (IOException e) 
		  	  	{
		  	  		e.printStackTrace();
		  	  	}
				return true;
			}
		}
		return false;
	}
}
