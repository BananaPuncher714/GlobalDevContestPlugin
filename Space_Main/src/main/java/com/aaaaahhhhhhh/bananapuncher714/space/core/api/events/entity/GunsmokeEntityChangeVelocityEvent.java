package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeEntity;

public class GunsmokeEntityChangeVelocityEvent extends GunsmokeEntityEvent {
	private static final HandlerList handlers = new HandlerList();
	private Vector prevVector;
	private Vector newVector;
	
	public GunsmokeEntityChangeVelocityEvent( GunsmokeEntity entity, Vector old, Vector toChange ) {
		super( entity );
		this.prevVector = old.clone();
		this.newVector = toChange.clone();
	}
	
	public Vector getOldVector() {
		return prevVector.clone();
	}

	public Vector getNewVector() {
		return newVector.clone();
	}

	public void setNewVector( Vector newVector ) {
		this.newVector = newVector.clone();
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
