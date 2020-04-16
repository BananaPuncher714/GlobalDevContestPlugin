package com.aaaaahhhhhhh.bananapuncher714.spaaace.implementation;

import org.bukkit.Location;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.DamageType;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.EnumTickResult;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.Tickable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.block.GunsmokeBlock;

public class RegeneratingGunsmokeBlock extends GunsmokeBlock implements Tickable {
	protected int regenDelay = 1;
	protected int regenCooldown = 100;
	protected int cooldown;
	
	public RegeneratingGunsmokeBlock( Location location, double health ) {
		super( location, health );
	}

	@Override
	public EnumTickResult tick() {
		if ( health >= maxHealth ) {
			return EnumTickResult.CONTINUE;
		}
		if ( cooldown-- <= 0 ) {
			health++;
			cooldown = regenDelay;
			updateBlockStage();
		}
		
		return EnumTickResult.CONTINUE;
	}

	@Override
	public void damage( double damage, DamageType type ) {
		super.damage( damage, type );
		cooldown = regenCooldown;
	}

	public int getRegenDelay() {
		return regenDelay;
	}

	public void setRegenDelay( int regenDelay ) {
		this.regenDelay = regenDelay;
	}

	public int getRegenCooldown() {
		return regenCooldown;
	}

	public void setRegenCooldown( int regenCooldown ) {
		this.regenCooldown = regenCooldown;
	}
}
