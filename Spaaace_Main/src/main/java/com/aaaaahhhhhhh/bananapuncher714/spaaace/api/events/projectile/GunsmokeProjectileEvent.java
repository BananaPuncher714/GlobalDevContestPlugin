package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.projectile;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.entity.projectile.GunsmokeProjectile;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.entity.GunsmokeEntityEvent;

public abstract class GunsmokeProjectileEvent extends GunsmokeEntityEvent {
protected GunsmokeProjectile entity;
	
	public GunsmokeProjectileEvent( GunsmokeProjectile entity ) {
		super( entity );
		this.entity = entity;
	}

	@Override
	public GunsmokeProjectile getRepresentable() {
		return entity;
	}
}
