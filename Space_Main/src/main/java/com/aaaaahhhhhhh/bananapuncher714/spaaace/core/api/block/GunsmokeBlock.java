package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.block;

import org.bukkit.Location;
import org.bukkit.Material;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.DamageType;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.BukkitUtil;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.SpaceUtil;

public class GunsmokeBlock extends GunsmokeRepresentable {
	protected double health;
	protected double maxHealth;
	protected Location location;
	
	public GunsmokeBlock( Location location, double health ) {
		this.location = BukkitUtil.getBlockLocation( location );
		this.health = health;
		this.maxHealth = health;
	}
	
	public void damage( double damage, DamageType type ) {
		health = Math.max( health - damage, 0 );
	}
	
	public void destroy() {
		location.getBlock().setType( Material.AIR, false );
		SpaceUtil.setBlockStage( location, -1 );
	}
	
	public boolean contains( Location location ) {
		return this.location.equals( BukkitUtil.getBlockLocation( location ) );
	}

	public double getHealth() {
		return health;
	}

	public void setHealth( double health ) {
		this.health = health;
	}
	
	public double getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth( double maxHealth ) {
		this.maxHealth = maxHealth;
	}
	
	public boolean isInvincible() {
		return maxHealth == -1;
	}
	
	public Location getLocation() {
		return location.clone();
	}

	public void updateBlockStage() {
		double percent = health / maxHealth;
		int stage = 9 - ( int ) ( percent * 10 );
		SpaceUtil.setBlockStage( location, stage );
	}
}