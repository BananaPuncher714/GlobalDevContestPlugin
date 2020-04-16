package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;

public class LeftClickEvent extends InteractiveEntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	protected boolean cancelled = false;
	
	public LeftClickEvent( GunsmokeInteractive player ) {
		super( player );
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled( boolean arg0 ) {
		cancelled = arg0;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
