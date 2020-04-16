package com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;

public class GunsmokeEntityWrapperPlayer extends GunsmokeEntityWrapperLivingEntity {
	protected Player entity;
	
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
}