package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.entity;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.GunsmokeEvent;

public abstract class GunsmokeEntityEvent extends GunsmokeEvent {
	protected GunsmokeEntity entity;
	
	public GunsmokeEntityEvent( GunsmokeEntity entity ) {
		super( entity );
		this.entity = entity;
	}

	@Override
	public GunsmokeEntity getRepresentable() {
		return entity;
	}
}