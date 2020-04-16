package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.util;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.projectile.GunsmokeProjectile;

public class ProjectileTargetEntity extends ProjectileTarget {
	protected CollisionResultEntity collision;
	
	public ProjectileTargetEntity( GunsmokeProjectile projectile, CollisionResultEntity intersection ) {
		super( projectile, intersection );
		this.collision = intersection;
	}

	@Override
	public CollisionResultEntity getIntersection() {
		return collision;
	}
	
	public GunsmokeEntity getEntity() {
		return collision.getEntity();
	}
}
