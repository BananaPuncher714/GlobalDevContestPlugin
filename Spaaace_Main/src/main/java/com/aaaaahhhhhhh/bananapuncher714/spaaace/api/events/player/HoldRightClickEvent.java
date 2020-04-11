package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;

public class HoldRightClickEvent extends HumanEntityEvent {
	private static final HandlerList handlers = new HandlerList();
	protected long ms;

	public HoldRightClickEvent( HumanEntity player, long ms ) {
		super( player );
		this.ms = ms;
	}
	
	public long getHeldTime() {
		return ms;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
