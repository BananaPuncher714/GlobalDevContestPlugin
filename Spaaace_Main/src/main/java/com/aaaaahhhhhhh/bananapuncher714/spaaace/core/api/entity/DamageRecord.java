package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.SpaaaceUtil;

public class DamageRecord {
	protected Map< DamageCause, DamageInstance > invincibilityTicksRemaining = new HashMap< DamageCause, DamageInstance >();
	protected Map< UUID, DamageInstance > separateInvincibility = new HashMap< UUID, DamageInstance >();
	
	public DamageInstance getTicksRemainingFor( DamageCause cause ) {
		if ( invincibilityTicksRemaining.containsKey( cause ) ) {
			DamageInstance instance = invincibilityTicksRemaining.get( cause );
			int remaining = instance.tickTime - SpaaaceUtil.getCurrentTick();
			if ( remaining <= 0 ) {
				invincibilityTicksRemaining.remove( cause );
				return null;
			}
			return instance;
		}
		return null;
	}
	
	public DamageInstance getTicksRemainingFor( UUID attacker ) {
		if ( separateInvincibility.containsKey( attacker ) ) {
			DamageInstance instance = separateInvincibility.get( attacker );
			int remaining = instance.tickTime - SpaaaceUtil.getCurrentTick();
			if ( remaining <= 0 ) {
				separateInvincibility.remove( attacker );
				return null;
			}
			return instance;
		}
		return null;
	}
	
	public boolean setTicksRemainingFor( DamageCause cause, double damage, int ticks ) {
		DamageInstance previous = getTicksRemainingFor( cause );
		if ( previous == null || damage > previous.damage ) {
			int currentTick = SpaaaceUtil.getCurrentTick();
			invincibilityTicksRemaining.put( cause, new DamageInstance( currentTick + ticks, damage ) );
			return true;
		}
		return false;
	}
	
	public boolean setTicksRemainingFor( UUID attacker, double damage, int ticks ) {
		DamageInstance previous = getTicksRemainingFor( attacker );
		if ( previous == null || damage > previous.damage ) {
			int currentTick = SpaaaceUtil.getCurrentTick();
			separateInvincibility.put( attacker, new DamageInstance( currentTick + ticks, damage ) );
			return true;
		}
		return false;
	}
	
	public class DamageInstance {
		public final int tickTime;
		public final double damage;
		
		protected DamageInstance( int time, double damage ) {
			this.tickTime = time;
			this.damage = damage;
		}
	}
}
