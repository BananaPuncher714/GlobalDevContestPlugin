package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Breathable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.SpaceUtil;

public class GunsmokeAirChangeEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	protected boolean cancelled = false;
	protected Breathable entity;
	protected int air;
	
	public GunsmokeAirChangeEvent( Breathable entity, int air ) {
		this.entity = entity;
		this.air = air;
	}

	public Breathable getBreathable() {
		return entity;
	}
	
	public int getAir() {
		return air;
	}
	
	public void setAir( int air ) {
		this.air = air;
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
