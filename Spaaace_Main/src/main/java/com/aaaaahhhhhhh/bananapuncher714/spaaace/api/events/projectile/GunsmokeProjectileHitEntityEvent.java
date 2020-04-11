package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.projectile;

import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.entity.projectile.GunsmokeProjectile;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.util.CollisionResultEntity;

public class GunsmokeProjectileHitEntityEvent extends GunsmokeProjectileHitEvent {
	private static final HandlerList handlers = new HandlerList();
	protected CollisionResultEntity result;
	
	public GunsmokeProjectileHitEntityEvent( GunsmokeProjectile entity, CollisionResultEntity result ) {
		super( entity, result );
		this.result = result;
	}
	
	@Override
	public CollisionResultEntity getCollisionResult() {
		return result;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
