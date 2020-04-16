package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player;

import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeInteractive;

public class AdvancementOpenEvent extends InteractiveEntityEvent {
	private static final HandlerList handlers = new HandlerList();
	private final String tab;
	
	public AdvancementOpenEvent( GunsmokeInteractive who, String tab ) {
		super( who );
		this.tab = tab;
	}
	
	public String getTab() {
		return tab;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
