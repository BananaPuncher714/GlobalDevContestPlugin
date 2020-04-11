package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.block;

import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.block.GunsmokeBlock;

public class GunsmokeBlockCreateEvent extends GunsmokeBlockEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public GunsmokeBlockCreateEvent( GunsmokeBlock block ) {
		super( block );
	}
	
	public void setBlock( GunsmokeBlock block ) {
		this.block = block;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
