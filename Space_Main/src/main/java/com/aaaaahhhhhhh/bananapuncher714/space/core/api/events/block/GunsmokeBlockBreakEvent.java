package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.block;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.block.GunsmokeBlock;

public class GunsmokeBlockBreakEvent extends GunsmokeBlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	protected boolean cancelled = false;
	protected GunsmokeRepresentable breaker;
	
	public GunsmokeBlockBreakEvent( GunsmokeBlock block, GunsmokeRepresentable breaker ) {
		super( block );
		this.breaker = breaker;
	}
	
	public GunsmokeRepresentable getBreaker() {
		return breaker;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
