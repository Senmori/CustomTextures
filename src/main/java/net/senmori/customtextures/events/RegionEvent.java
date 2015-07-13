package net.senmori.customtextures.events;

import net.senmori.customtextures.util.MovementType;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionEvent extends PlayerEvent
{	
	private static final HandlerList handlerList = new HandlerList();
	
	private ProtectedRegion region;
	private MovementType movementType;
	public PlayerEvent superEvent;
	
	public RegionEvent(Player player, ProtectedRegion region, PlayerEvent parent, MovementType moveType)
	{
		super(player);
		this.region = region;
		this.movementType = moveType;
		this.superEvent = parent;
	}

	public ProtectedRegion getRegion()
	{
		return region;
	}
	
	public MovementType getMovementType()
	{	
		return movementType;
	}
	
	public PlayerEvent getParentEvent()
	{
		return superEvent;
	}
	
	@Override
	public HandlerList getHandlers() 
	{
		return handlerList;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlerList;
	}
}
