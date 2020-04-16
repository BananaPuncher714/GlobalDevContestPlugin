package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeInteractive;

public class RightClickEntityEvent extends InteractiveEntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Entity clicked ;
	private boolean cancelled = false;
	
	public RightClickEntityEvent( GunsmokeInteractive player, Entity entity ) {
		super( player );
		clicked = entity;
	}
	
	public Entity getClickedEntity() {
		return clicked;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled( boolean arg0 ) {
		cancelled = arg0;
	}

	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
