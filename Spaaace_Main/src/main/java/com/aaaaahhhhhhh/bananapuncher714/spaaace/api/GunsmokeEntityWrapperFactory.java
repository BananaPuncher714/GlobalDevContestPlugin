package com.aaaaahhhhhhh.bananapuncher714.spaaace.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.entity.bukkit.GunsmokeEntityWrapperLivingEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.entity.bukkit.GunsmokeEntityWrapperPlayer;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.entity.bukkit.GunsmokeEntityWrapperProjectile;

public final class GunsmokeEntityWrapperFactory {
	public final static GunsmokeEntityWrapper wrap( Entity entity ) {
		if ( entity instanceof LivingEntity ) {
			if ( entity instanceof Player ) {
				return new GunsmokeEntityWrapperPlayer( ( Player ) entity );
			} else {
				return new GunsmokeEntityWrapperLivingEntity( ( LivingEntity ) entity );
			}
		} else if ( entity instanceof Projectile ) {
			return new GunsmokeEntityWrapperProjectile( ( Projectile ) entity );
		} else {
			return new GunsmokeEntityWrapper( entity );
		}
	}
}
