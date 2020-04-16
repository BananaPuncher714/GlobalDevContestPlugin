package com.aaaaahhhhhhh.bananapuncher714.space.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.DamageType;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.RegenType;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.DamageRecord;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity.GunsmokeEntityDamageByEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity.GunsmokeEntityDamageEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity.GunsmokeEntityRegenEvent;

/**
 * Manage damage records and invincibility times.
 * 
 * @author BananaPuncher714
 */
public class DamageManager implements Listener {
	protected static final Map< DamageCause, Integer > DEFAULT_INVINCIBILITY;
	
	protected SpaceCore plugin;
	protected Map< UUID, DamageRecord > records = new HashMap< UUID, DamageRecord >();
	
	static {
		DEFAULT_INVINCIBILITY = new HashMap< DamageCause, Integer >();
		for ( DamageCause cause : DamageCause.values() ) {
			DEFAULT_INVINCIBILITY.put( cause, 10 );
		}
		
		// Set the invincibility ticks here
		DEFAULT_INVINCIBILITY.put( DamageCause.ENTITY_EXPLOSION, 0 );
		DEFAULT_INVINCIBILITY.put( DamageCause.BLOCK_EXPLOSION, 0 );
		DEFAULT_INVINCIBILITY.put( DamageCause.LIGHTNING, 0 );
		DEFAULT_INVINCIBILITY.put( DamageCause.FALL, 0 );
		DEFAULT_INVINCIBILITY.put( DamageCause.DROWNING, 0 );
		DEFAULT_INVINCIBILITY.put( DamageCause.WITHER, 5 );
		DEFAULT_INVINCIBILITY.put( DamageCause.VOID, 0 );
		DEFAULT_INVINCIBILITY.put( DamageCause.THORNS, 0 );
		DEFAULT_INVINCIBILITY.put( DamageCause.FALLING_BLOCK, 0 );
		DEFAULT_INVINCIBILITY.put( DamageCause.HOT_FLOOR, 5 );
		DEFAULT_INVINCIBILITY.put( DamageCause.FIRE, 5 );
		DEFAULT_INVINCIBILITY.put( DamageCause.POISON, 5 );
		DEFAULT_INVINCIBILITY.put( DamageCause.PROJECTILE, 0 );
		DEFAULT_INVINCIBILITY.put( DamageCause.ENTITY_ATTACK, 5 );
		DEFAULT_INVINCIBILITY.put( DamageCause.ENTITY_SWEEP_ATTACK, 5 );
		DEFAULT_INVINCIBILITY.put( DamageCause.SUFFOCATION, 5 );
		DEFAULT_INVINCIBILITY.put( DamageCause.CUSTOM, 0 );
		DEFAULT_INVINCIBILITY.put( DamageCause.CONTACT, 5 );
	}
	
	public DamageManager( SpaceCore plugin ) {
		this.plugin = plugin;
	}
	
	public DamageRecord getDamageRecord( UUID uuid ) {
		DamageRecord record = records.get( uuid );
		if ( record == null ) {
			record = new DamageRecord();
			records.put( uuid, record );
		}
		return record;
	}
	
	public boolean damage( GunsmokeEntity entity, double damage, DamageType type, DamageCause cause ) {
		// These 3 should get handled with damage by entity
		if ( entity.isInvincible() ) {
			return false;
		}
		if ( cause == DamageCause.PROJECTILE || cause == DamageCause.ENTITY_ATTACK || cause == DamageCause.ENTITY_SWEEP_ATTACK ) {
			return false;
		}
		if ( !getDamageRecord( entity.getUUID() ).setTicksRemainingFor( cause, damage, DEFAULT_INVINCIBILITY.get( cause ) ) ) {
			return false;
		}
		
		if ( damage <= 0 ) {
			return false;
		}
		
		GunsmokeEntityDamageEvent event = new GunsmokeEntityDamageEvent( entity, type, damage, cause );
		
		event.callEvent();
		
		if ( event.getDamage() <= 0 ) {
			return false;
		}
		
		if ( !event.isCancelled() ) {
			entity.damage( event );
			return true;
		}
		return false;
	}
	
	public boolean damage( GunsmokeEntity entity, double damage, DamageType type, GunsmokeEntity damager ) {
		if ( entity.isInvincible() ) {
			return false;
		}
		if ( !getDamageRecord( entity.getUUID() ).setTicksRemainingFor( damager.getUUID(), damage, DEFAULT_INVINCIBILITY.get( DamageCause.ENTITY_ATTACK ) ) ) {
			return false;
		}
		
		if ( damage <= 0 ) {
			return false;
		}
		
		GunsmokeEntityDamageByEntityEvent event = new GunsmokeEntityDamageByEntityEvent( entity, type, damage, damager );
		
		event.callEvent();
		
		if ( event.getDamage() <= 0 ) {
			return false;
		}
		
		if ( !event.isCancelled() ) {
			entity.damage( event );
			return true;
		}
		return false;
	}
	
	public boolean regen( GunsmokeEntity entity, double amount, RegenType type ) {
		return regen( entity, amount, type, RegainReason.CUSTOM );
	}
	
	public boolean regen( GunsmokeEntity entity, double amount, RegenType type, RegainReason reason ) {
		if ( amount <= 0 ) {
			return false;
		}
		
		GunsmokeEntityRegenEvent event = new GunsmokeEntityRegenEvent( entity, amount, type, reason );
		
		event.callEvent();
		
		if ( event.getAmount() <= 0 ) {
			return false;
		}
		
		if ( !event.isCancelled() ) {
			entity.regen( event );
			return true;
		}
		
		return false;
	}
}
