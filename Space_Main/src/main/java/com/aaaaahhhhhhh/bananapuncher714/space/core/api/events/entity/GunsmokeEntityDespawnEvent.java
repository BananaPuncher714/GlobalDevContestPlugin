package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity;

import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeEntity;

public class GunsmokeEntityDespawnEvent extends GunsmokeEntityEvent {
	private static final HandlerList handlers = new HandlerList();
	
	public GunsmokeEntityDespawnEvent( GunsmokeEntity entity ) {
		super( entity );
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
