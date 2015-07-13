package net.senmori.customtextures.managers;

import java.io.File;

import net.senmori.customtextures.CustomTextures;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

	public static void init()
	{
		// create plugin directory if it doesn't exist
		if(!CustomTextures.getInstance().getDataFolder().exists())
		{
			CustomTextures.getInstance().getDataFolder().mkdirs();
		}
		
		CustomTextures.getInstance().configFile = new File(CustomTextures.getInstance().getDataFolder(), "config.yml");
		if(!CustomTextures.getInstance().configFile.exists())
		{
			CustomTextures.getInstance().configFile.getParentFile().mkdirs();
			CustomTextures.getInstance().saveDefaultConfig();
			loadDefaults();
		}
		CustomTextures.getInstance().config = YamlConfiguration.loadConfiguration(CustomTextures.getInstance().configFile);
		loadConfig();
	}
	
	public static void loadConfig()
	{
		CustomTextures.getInstance().debug = CustomTextures.getInstance().config.getBoolean("settings.debug");
		CustomTextures.getInstance().showPermissionAfterChange = CustomTextures.getInstance().config.getBoolean("settings.show-permission-after-change");
		CustomTextures.getInstance().defaultResourcePackURL = CustomTextures.getInstance().config.getString("default-resource-pack");
	}
	
	private static void loadDefaults()
	{
		for(World world : Bukkit.getWorlds())
		{
			CustomTextures.getInstance().config.set("worlds." + world.getName().replace(" ", "_"), " ");
		}
	}
}