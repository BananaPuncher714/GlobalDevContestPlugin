package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity;

import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeEntity;

public class GunsmokeEntityDeathEvent extends GunsmokeEntityEvent {
	private static final HandlerList handlers = new HandlerList();

	public GunsmokeEntityDeathEvent( GunsmokeEntity entity ) {
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
