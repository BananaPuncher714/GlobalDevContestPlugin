package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.DamageType;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeEntity;

public class GunsmokeEntityDamageByEntityEvent extends GunsmokeEntityDamageEvent {
	protected GunsmokeEntity damager;
	
	public GunsmokeEntityDamageByEntityEvent( GunsmokeEntity entity, DamageType type, double amount, GunsmokeEntity damager ) {
		super( entity, type, amount, DamageCause.ENTITY_ATTACK );
		this.damager = damager;
	}
	
	public GunsmokeEntity getDamager() {
		return damager;
	}
}
