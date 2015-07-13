package net.senmori.customtextures.util;

import java.util.logging.Level;

import net.senmori.customtextures.CustomTextures;

public class LogHandler 
{
	public static void log(Level level, String message)
	{
		CustomTextures.log.log(level, "[" + CustomTextures.name + "] " + message);
	}
	
	public static void all(String message)
	{
		log(Level.ALL, message);
	}
	
	public static void debug(String message)
	{
		if(CustomTextures.getInstance().debug) { log(Level.CONFIG, message); }
	}
	
	public static void dbWarning(String message)
	{
		log(Level.WARNING, "[DB] " + message);
	}
	
	public static void warning(String message)
	{
		log(Level.WARNING, message);
	}
	
	public static void severe(String message)
	{
		log(Level.SEVERE, message);
	}
	
	public static void info(String message)
	{
		log(Level.INFO, message);
	}
	
	public static void fine(String message)
	{
		log(Level.FINE, message);
	}
}