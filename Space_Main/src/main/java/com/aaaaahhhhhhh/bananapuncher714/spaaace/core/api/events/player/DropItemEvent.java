package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;

public class DropItemEvent extends InteractiveEntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	public DropItemEvent( GunsmokeInteractive updater ) {
		super( updater );
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled( boolean cancel ) {
		cancelled = cancel;
	}
}
