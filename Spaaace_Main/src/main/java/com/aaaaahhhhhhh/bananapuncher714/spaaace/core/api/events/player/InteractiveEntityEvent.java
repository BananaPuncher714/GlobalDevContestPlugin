package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player;

import org.bukkit.event.Event;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.SpaaaceUtil;

public abstract class InteractiveEntityEvent extends Event {
	protected GunsmokeInteractive entity;
	
	public InteractiveEntityEvent( GunsmokeInteractive entity ) {
		this.entity = entity;
	}

	public GunsmokeInteractive getEntity() {
		return entity;
	}
	
	public void callEvent() {
		SpaaaceUtil.callEventSync( this );
	}
}
