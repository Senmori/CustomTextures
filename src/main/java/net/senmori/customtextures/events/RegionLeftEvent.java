package net.senmori.customtextures.events;

import net.senmori.customtextures.util.MovementType;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionLeftEvent extends RegionEvent
{
    /**
     * creates a new RegionLeftEvent
     * @param region the region the player has left
     * @param player the player who triggered the event
     * @param movement the type of movement how the player left the region
     */
    public RegionLeftEvent(ProtectedRegion region, Player player, MovementType movement, PlayerEvent parent)
    {
        super(player, region, parent, movement);
    }
}