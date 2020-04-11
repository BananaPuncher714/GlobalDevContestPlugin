package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events;

import org.bukkit.event.Event;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.util.SpaaaceUtil;

public abstract class GunsmokeEvent extends Event {
	protected GunsmokeRepresentable item;
	
	public GunsmokeEvent( GunsmokeRepresentable item ) {
		this.item = item;
	}

	public GunsmokeRepresentable getRepresentable() {
		return item;
	}
	
	public void callEvent() {
		SpaaaceUtil.callEventSync( this );
	}
}
