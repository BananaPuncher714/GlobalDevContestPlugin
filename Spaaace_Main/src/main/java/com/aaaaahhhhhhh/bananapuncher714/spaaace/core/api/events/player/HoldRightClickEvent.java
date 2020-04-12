package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player;

import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;

public class HoldRightClickEvent extends InteractiveEntityEvent {
	private static final HandlerList handlers = new HandlerList();
	protected long ms;

	public HoldRightClickEvent( GunsmokeInteractive player, long ms ) {
		super( player );
		this.ms = ms;
	}
	
	public long getHeldTime() {
		return ms;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
