package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeInteractive;

public class RightClickEvent extends InteractiveEntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	public RightClickEvent( GunsmokeInteractive player ) {
		super( player );
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled( boolean cancelled ) {
		this.cancelled = cancelled;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
