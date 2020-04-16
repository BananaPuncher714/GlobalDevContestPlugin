package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.world;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.world.GunsmokeExplosion;

public class GunsmokeExplosionPrepareEvent extends GunsmokeExplosionEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	protected boolean cancelled = false;
	
	public GunsmokeExplosionPrepareEvent( GunsmokeExplosion explosion ) {
		super( explosion );
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled( boolean arg0 ) {
		cancelled = arg0;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
