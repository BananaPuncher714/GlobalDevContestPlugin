package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.entity.EntityEvent;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.util.SpaaaceUtil;

public abstract class HumanEntityEvent extends EntityEvent {
	protected HumanEntity entity;
	
	public HumanEntityEvent( HumanEntity entity ) {
		super( entity );
		this.entity = entity;
	}

	@Override
	public HumanEntity getEntity() {
		return entity;
	}
	
	public void callEvent() {
		SpaaaceUtil.callEventSync( this );
	}
}
