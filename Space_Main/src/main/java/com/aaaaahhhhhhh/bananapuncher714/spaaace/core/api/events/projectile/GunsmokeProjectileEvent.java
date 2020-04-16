package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.projectile;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.projectile.GunsmokeProjectile;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityEvent;

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
