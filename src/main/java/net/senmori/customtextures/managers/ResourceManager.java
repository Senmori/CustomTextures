package net.senmori.customtextures.managers;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.senmori.customtextures.CustomTextures;
import net.senmori.customtextures.util.LogHandler;
import net.senmori.customtextures.util.Reference.Permissions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class ResourceManager 
{
	public ResourceManager() {}
	
	
	/*
	 * Main method, just call this and 
	 */
	public void loadResourcePack(String url, String uuid)
	{
		Player player = Bukkit.getPlayer(UUID.fromString(uuid));
		if( !(player instanceof Player) || player == null ) 
		{
			LogHandler.warning("Cannot set a resource pack on a non-player entity!");
			return;
		}
		
		if(doLoadPack(player) && isURL(url))
		{
			player.setResourcePack(url);
		}
	}
	
	/*
	 * Check if a resource pack should be loaded for a player
	 */
	public boolean doLoadPack(Player player)
	{
		// check if the player has disabled loading of resource packs
		List<String> disabledPlayers = CustomTextures.getInstance().config.getStringList("do-not-load");
		if(disabledPlayers.contains(player.getUniqueId().toString())) return false;
		if(player.hasPermission(Permissions.doNotLoadPermission)) return false;
		
		// check worlds
		if(isResourcedWorld(player.getWorld().getName()) && player.hasPermission(Permissions.worldResourcePack + player.getWorld().toString().toLowerCase()))
		{
			return true;
		}
		
		// check regions
		if(isResourcedRegion(player.getLocation()) && player.hasPermission(Permissions.regionResourcePack + getRegion(player.getLocation())))
		{
			return true;
		}		
		return false;
	}
	
	/*
	 * Check if a world has a resource pack attached
	 */
	public boolean isResourcedWorld(String world)
	{
		List<World> worlds = Bukkit.getWorlds();
		List<String> node = CustomTextures.getInstance().config.getStringList("worlds");
		int max = worlds.size() > node.size() ? worlds.size() : node.size();
		
		if(node.isEmpty() || node == null) return false;
		
		for(int i = 0; i <= max; i++)
		{
			if(i > worlds.size() || i > node.size()) return false;
			if(worlds.get(i) == null || node.get(i) == null) continue; // skip current iteration if the world or node is null/corrupt
			
			if(worlds.get(i).getName().replace(" ", "_").equalsIgnoreCase(node.get(i)))
			{
				return true;
			}
		}
		worlds.clear(); // because I'm paranoid
		return false;
	}
	
	/*
	 * Check if a region has a resource pack attached
	 */
	public boolean isResourcedRegion(Location loc)
	{
		String region = getRegion(loc);
		return CustomTextures.getInstance().config.getString("regions." + region) != null ? true : false;
	}
	
	/*
	 * Get the region at a location
	 */
	public String getRegion(Location loc)
	{
		RegionManager regionManager = CustomTextures.getInstance().getWG().getRegionManager(loc.getWorld());
		ApplicableRegionSet appSet = regionManager.getApplicableRegions(loc);
		LinkedList<String> parentNames = new LinkedList<String>();
		LinkedList<String> regions = new LinkedList<String>();
		
		for(ProtectedRegion r : appSet)
		{
			String id = r.getId();
			regions.add(id);
			ProtectedRegion parent = r.getParent();
			
			while(parent != null)
			{
				parentNames.add(parent.getId());
				parent = parent.getParent();
			}
			
			for(String name : parentNames)
			{
				regions.remove(name);
			}		
		}
		return regions.toString();	
	}
	
	/*
	 * Check if a url(from config) is valid
	 */
	public boolean isURL(String url)
	{
		return ((url != null) && (url.contains("http://") || url.contains("https://")) && url.contains(".zip"));
	}
	
	/*
	 * Get the applicable url for a world/region
	 */
	public String getUrl(Location loc)
	{
		String url = null;
		if(isResourcedRegion(loc))
		{
			url = CustomTextures.getInstance().config.getString("regions." + getRegion(loc));
		}
		
		if(isResourcedWorld(loc.getWorld().getName()))
		{
			url = CustomTextures.getInstance().config.getString("worlds." + loc.getWorld().getName().replace(" ", "_"));
		}
		
		// return default url if custom url is not valid or null/empty
		if(!isURL(url) || url == null)
		{
			url = CustomTextures.getInstance().defaultResourcePackURL;
		}
		return url;
	}
}
