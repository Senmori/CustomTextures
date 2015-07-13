package net.senmori.customtextures.listeners;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.senmori.customtextures.CustomTextures;
import net.senmori.customtextures.events.RegionEnterEvent;
import net.senmori.customtextures.events.RegionEnteredEvent;
import net.senmori.customtextures.events.RegionLeaveEvent;
import net.senmori.customtextures.events.RegionLeftEvent;
import net.senmori.customtextures.util.MovementType;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionListener implements Listener
{	
	private WorldGuardPlugin worldGuard;
	private CustomTextures instance;
	
	private HashMap<String,Set<ProtectedRegion>> playerRegions;
	
	public RegionListener(WorldGuardPlugin worldGuard, CustomTextures plugin)
	{
		this.worldGuard = worldGuard;
		this.instance = plugin;
		
		playerRegions = new HashMap<String, Set<ProtectedRegion>>();
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e)
	{
		Set<ProtectedRegion> r = playerRegions.remove(e.getPlayer().getUniqueId().toString());
        if (r != null)
        {
            for(ProtectedRegion region : r)
            {
                RegionLeaveEvent leaveEvent = new RegionLeaveEvent(region, e.getPlayer(), MovementType.DISCONNECT, e);
                RegionLeftEvent leftEvent = new RegionLeftEvent(region, e.getPlayer(), MovementType.DISCONNECT, e);
                
                instance.getServer().getPluginManager().callEvent(leaveEvent);
                instance.getServer().getPluginManager().callEvent(leftEvent);
            }
        }
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		Set<ProtectedRegion> r = playerRegions.remove(e.getPlayer().getUniqueId().toString());
        if (r != null)
        {
            for(ProtectedRegion region : r)
            {
            	RegionLeaveEvent leaveEvent = new RegionLeaveEvent(region, e.getPlayer(), MovementType.DISCONNECT, e);
                RegionLeftEvent leftEvent = new RegionLeftEvent(region, e.getPlayer(), MovementType.DISCONNECT, e);
                
                instance.getServer().getPluginManager().callEvent(leaveEvent);
                instance.getServer().getPluginManager().callEvent(leftEvent);
            }
        }
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		e.setCancelled(updateRegions(e.getPlayer(), MovementType.MOVE, e.getTo(), e));
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e)
	{
		e.setCancelled(updateRegions(e.getPlayer(), MovementType.TELEPORT, e.getTo(), e));
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		updateRegions(e.getPlayer(), MovementType.SPAWN, e.getPlayer().getLocation(), e);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		updateRegions(e.getPlayer(), MovementType.SPAWN, e.getRespawnLocation(), e);
	}
	

	private synchronized boolean updateRegions(final Player player, final MovementType movement, Location to, final PlayerEvent event)
    {
        Set<ProtectedRegion> regions;
        Set<ProtectedRegion> oldRegions;
        
        if (playerRegions.get(player) == null)
        {
            regions = new HashSet<ProtectedRegion>();
        }
        else
        {
            regions = new HashSet<ProtectedRegion>(playerRegions.get(player.getUniqueId().toString()));
        }
        
        oldRegions = new HashSet<ProtectedRegion>(regions);
        
        RegionManager rm = worldGuard.getRegionManager(to.getWorld());
        
        if (rm == null)
        {
            return false;
        }
        
        ApplicableRegionSet appRegions = rm.getApplicableRegions(to);
        
        for (final ProtectedRegion region : appRegions)
        {
            if (!regions.contains(region))
            {
                RegionEnterEvent e = new RegionEnterEvent(region, player, movement, event);
                
                instance.getServer().getPluginManager().callEvent(e);
                
                if (e.isCancelled())
                {
                    regions.clear();
                    regions.addAll(oldRegions);
                    
                    return true;
                }
                else
                {
                    Bukkit.getScheduler().runTaskLater(instance,new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            {}
                            RegionEnteredEvent e = new RegionEnteredEvent(region, player, movement, event);
                            
                            instance.getServer().getPluginManager().callEvent(e);
                        }
                    }, 1L);
                    regions.add(region);
                }
            }
        }
        
        Collection<ProtectedRegion> app = appRegions.getRegions();
        Iterator<ProtectedRegion> itr = regions.iterator();
        while(itr.hasNext())
        {
            final ProtectedRegion region = itr.next();
            if (!app.contains(region))
            {
                if (rm.getRegion(region.getId()) != region)
                {
                    itr.remove();
                    continue;
                }
                RegionLeaveEvent e = new RegionLeaveEvent(region, player, movement, event);

                instance.getServer().getPluginManager().callEvent(e);

                if (e.isCancelled())
                {
                    regions.clear();
                    regions.addAll(oldRegions);
                    return true;
                }
                else
                {
                    Bukkit.getScheduler().runTaskLater(instance,new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            RegionLeftEvent e = new RegionLeftEvent(region, player, movement, event);
                            
                            instance.getServer().getPluginManager().callEvent(e);
                        }
                    }, 1L);
                    itr.remove();
                }
            }
        }
        playerRegions.put(player.getUniqueId().toString(), regions);
        return false;
    }
}
