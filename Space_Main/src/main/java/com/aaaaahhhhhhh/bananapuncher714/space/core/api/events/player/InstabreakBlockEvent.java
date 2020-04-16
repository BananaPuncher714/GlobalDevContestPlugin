package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeInteractive;

public class InstabreakBlockEvent extends InteractiveEntityEvent {
	private static final HandlerList handlers = new HandlerList();
	private final Location location;
	
	public InstabreakBlockEvent( GunsmokeInteractive player, Location location ) {
		super( player );
		this.location = location.clone();
	}

	public Location getLocation() {
		return location.clone();
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
