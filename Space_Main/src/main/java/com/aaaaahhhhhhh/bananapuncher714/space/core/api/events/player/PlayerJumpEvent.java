package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player;

import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeInteractive;

public class PlayerJumpEvent extends InteractiveEntityEvent {
	private static final HandlerList handlers = new HandlerList();

	public PlayerJumpEvent( GunsmokeInteractive player ) {
		super( player );
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
