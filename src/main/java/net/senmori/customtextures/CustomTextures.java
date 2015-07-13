package net.senmori.customtextures;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.senmori.customtextures.listeners.PlayerListener;
import net.senmori.customtextures.listeners.RegionListener;
import net.senmori.customtextures.managers.CommandManager;
import net.senmori.customtextures.managers.ConfigManager;
import net.senmori.customtextures.managers.ResourceManager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;


public class CustomTextures extends JavaPlugin
{	
	// plugin vars
	private static CustomTextures instance;
	public static Logger log;
	private PluginDescriptionFile pdf;
	public static String author;
	public static String name;
	
	// config options
	public File configFile;
	public FileConfiguration config;
	
	// config options
	public boolean debug;
	public boolean showPermissionAfterChange;
	public String defaultResourcePackURL;
	
	
	// WorldGuard
	private static WorldGuardPlugin worldGuard;

	
	//Listeners
	PlayerListener playerListener;
	RegionListener regionListener;
	
	//Managers
	public ResourceManager resourceManager;
	public static CommandManager commandManager;
	
	@Override
	public void onEnable()
	{
		log = getLogger();
		pdf = getDescription();
		name = pdf.getName();
		author = pdf.getAuthors().get(0);
		
		instance = this;
		ConfigManager.init();
		
		// load worldguard
		worldGuard = getWorldGuard();
		
		resourceManager = new ResourceManager();
		commandManager = new CommandManager(getInstance());
		
		// listeners
		playerListener = new PlayerListener();
		regionListener = new RegionListener(getWG(), getInstance());
		
		getServer().getPluginManager().registerEvents(regionListener, getInstance());
		getServer().getPluginManager().registerEvents(playerListener, getInstance());
		
		instance = this;
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	public static CustomTextures getInstance()
	{
		return instance;
	}
	
	public ResourceManager getResourceManager()
	{
		return resourceManager;
	}
	
	public WorldGuardPlugin getWG()
	{
		return worldGuard;
	}
	
	private WorldGuardPlugin getWorldGuard()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
		if(plugin == null || !(plugin instanceof WorldGuardPlugin))
		{
			return null;
		}
		return (WorldGuardPlugin)plugin;
	}
}