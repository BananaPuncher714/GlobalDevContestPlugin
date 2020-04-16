package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Gravitable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.SpaceUtil;

public class GunsmokeGravityChangeEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	protected boolean cancelled = false;
	protected Gravitable entity;
	protected double gravity;
	
	public GunsmokeGravityChangeEvent( Gravitable entity, double newGravity ) {
		this.entity = entity;
		this.gravity = newGravity;
	}

	public Gravitable getGravitable() {
		return entity;
	}
	
	public double getNewGravity() {
		return gravity;
	}
	
	public void setNewGravity( double gravity ) {
		this.gravity = gravity;
	}
	
	public void callEvent() {
		SpaceUtil.callEventSync( this );
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled( boolean arg0 ) {
		cancelled = arg0;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
