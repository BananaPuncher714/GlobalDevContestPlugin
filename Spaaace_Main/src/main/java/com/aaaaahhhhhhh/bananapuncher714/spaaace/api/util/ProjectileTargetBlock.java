package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.util;

import org.bukkit.block.Block;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.entity.projectile.GunsmokeProjectile;

public class ProjectileTargetBlock extends ProjectileTarget {
	protected CollisionResultBlock intersection;
	
	public ProjectileTargetBlock( GunsmokeProjectile projectile, CollisionResultBlock intersection ) {
		super( projectile, intersection );
		this.intersection = intersection;
	}
	
	@Override
	public CollisionResultBlock getIntersection() {
		return intersection;
	}
	
	public Block getBlock() {
		return intersection.getBlock();
	}
}
