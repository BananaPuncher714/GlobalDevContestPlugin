package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events;

import org.bukkit.event.Event;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.SpaceUtil;

public abstract class GunsmokeEvent extends Event {
	protected GunsmokeRepresentable item;
	
	public GunsmokeEvent( GunsmokeRepresentable item ) {
		this.item = item;
	}

	public GunsmokeRepresentable getRepresentable() {
		return item;
	}
	
	public void callEvent() {
		SpaceUtil.callEventSync( this );
	}
}
