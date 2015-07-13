package net.senmori.customtextures.listeners;

import net.senmori.customtextures.CustomTextures;
import net.senmori.customtextures.events.RegionEnterEvent;
import net.senmori.customtextures.events.RegionLeaveEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener
{
	/*
	 * Called when a player enters a worldguard region
	 */
	@EventHandler
	public void onRegionEnter(RegionEnterEvent e)
	{
		String url = CustomTextures.getInstance().getResourceManager().getUrl(e.getPlayer().getLocation());
		//CustomTextures.getInstance().getResourceManager().loadResourcePack(url, e.getPlayer().getUniqueId().toString());
		e.getPlayer().sendMessage("You entered the region " + e.getRegion().getId());
		return;
	}
	
	/*
	 * Called when a player leaves a worldguard region
	 */
	@EventHandler
	public void onRegionLeave(RegionLeaveEvent e)
	{
		String url = CustomTextures.getInstance().getResourceManager().getUrl(e.getPlayer().getLocation());
		//CustomTextures.getInstance().getResourceManager().loadResourcePack(url, e.getPlayer().getUniqueId().toString());
		e.getPlayer().sendMessage("You left the region " + e.getRegion().getId());
	}
	
	/*
	 * When a player changes worlds
	 */
	@EventHandler
	public void onPlayerTeleport(PlayerChangedWorldEvent e)
	{
		
	}
	
	/*
	 * when a player joins the server
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		
	}
}