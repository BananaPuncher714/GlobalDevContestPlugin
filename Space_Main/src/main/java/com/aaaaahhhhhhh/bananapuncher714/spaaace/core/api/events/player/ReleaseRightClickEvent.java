package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player;

import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;

public class ReleaseRightClickEvent extends InteractiveEntityEvent {
	private static final HandlerList handlers = new HandlerList();

	public ReleaseRightClickEvent( GunsmokeInteractive player ) {
		super( player );
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
