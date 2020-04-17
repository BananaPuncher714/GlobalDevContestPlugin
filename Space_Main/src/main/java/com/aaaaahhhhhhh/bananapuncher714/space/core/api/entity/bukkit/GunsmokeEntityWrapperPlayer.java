package com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;

public class GunsmokeEntityWrapperPlayer extends GunsmokeEntityWrapperLivingEntity {
	protected Player entity;
	protected double leftPercent = -1;
	protected double rightPercent = -1;
	
	public GunsmokeEntityWrapperPlayer( Player entity) {
		super( entity );
		this.entity = entity;
	}

	@Override
	public Player getEntity() {
		return entity;
	}
	
	@Override
	public void remove() {
	}
	
	@Override
	public void sendMessage( BaseComponent message ) {
		entity.spigot().sendMessage( message );
	}
	
	@Override
	public void sendMessage( String message ) {
		entity.sendMessage( message );
	}
	
	@Override
	public boolean isInvincible() {
		return super.isInvincible() || entity.getGameMode() == GameMode.CREATIVE || entity.getGameMode() == GameMode.SPECTATOR;
	}

	public double getLeftPercent() {
		return leftPercent;
	}

	public void setLeftPercent( double leftPercent ) {
		this.leftPercent = leftPercent;
	}

	public double getRightPercent() {
		return rightPercent;
	}

	public void setRightPercent( double rightPercent ) {
		this.rightPercent = rightPercent;
	}
	
	@Override
	public void setAir( int amount ) {
		if ( entity.getGameMode() != GameMode.CREATIVE ) {
			super.setAir( airTicks = amount );
		} else {
			airTicks = Math.max( 1, airTicks );
		}
	}
}
