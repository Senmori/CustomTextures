package net.senmori.customtextures.events;

import net.senmori.customtextures.util.MovementType;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

/**
 * event that is triggered after a player entered a WorldGuard region
 * @author mewin<mewin001@hotmail.de>
 */
public class RegionEnteredEvent extends RegionEvent
{
    /**
     * creates a new RegionEnteredEvent
     * @param region the region the player entered
     * @param player the player who triggered the event
     * @param movement the type of movement how the player entered the region
     */
    public RegionEnteredEvent(ProtectedRegion region, Player player, MovementType movement, PlayerEvent parent)
    {
        super(player, region, parent, movement);
    }
}