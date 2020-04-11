package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.entity;

import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.entity.GunsmokeEntity;

public class GunsmokeEntityUnloadEvent extends GunsmokeEntityEvent {
	private static final HandlerList handlers = new HandlerList();

	public GunsmokeEntityUnloadEvent( GunsmokeEntity entity ) {
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
