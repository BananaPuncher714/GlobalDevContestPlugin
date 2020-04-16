package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.projectile;

import org.bukkit.event.Cancellable;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.projectile.GunsmokeProjectile;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.util.CollisionResult;

public abstract class GunsmokeProjectileHitEvent extends GunsmokeProjectileEvent implements Cancellable {
	protected boolean cancelled = false;
	protected CollisionResult result;
	
	public GunsmokeProjectileHitEvent( GunsmokeProjectile entity, CollisionResult result ) {
		super( entity );
		this.result = result;
	}
	
	public CollisionResult getCollisionResult() {
		return result;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled( boolean arg0 ) {
		cancelled = arg0;
	}
}
