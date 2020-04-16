package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events;

import org.bukkit.event.Event;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.SpaceUtil;

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
