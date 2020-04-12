package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;

public class LeftClickEntityEvent extends LeftClickEvent {
	private static final HandlerList handlers = new HandlerList();
	private final Entity hitEntity;
	
	public LeftClickEntityEvent( GunsmokeInteractive player, Entity entity ) {
		super( player );
		this.hitEntity = entity;
	}

	public Entity getHitEntity() {
		return hitEntity;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
