package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.projectile;

import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.projectile.GunsmokeProjectile;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.util.CollisionResultBlock;

public class GunsmokeProjectileHitBlockEvent extends GunsmokeProjectileHitEvent {
	private static final HandlerList handlers = new HandlerList();
	protected CollisionResultBlock hitBlock;
	
	public GunsmokeProjectileHitBlockEvent( GunsmokeProjectile entity, CollisionResultBlock result ) {
		super( entity, result );
		hitBlock = result;
	}
	
	@Override
	public CollisionResultBlock getCollisionResult() {
		return hitBlock;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
