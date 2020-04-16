package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player;

import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;

public class HoldRightClickEvent extends InteractiveEntityEvent {
	private static final HandlerList handlers = new HandlerList();
	protected int ticks;

	public HoldRightClickEvent( GunsmokeInteractive player, int ticks ) {
		super( player );
		this.ticks = ticks;
	}
	
	public int getHeldTime() {
		return ticks;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
