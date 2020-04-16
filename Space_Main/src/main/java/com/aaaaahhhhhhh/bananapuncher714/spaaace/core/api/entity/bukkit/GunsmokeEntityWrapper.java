package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.EnumEventResult;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.EnumTickResult;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.Gravitable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.Nameable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityChangeVelocityEvent;

public class GunsmokeEntityWrapper extends GunsmokeEntity implements Nameable, Gravitable {
	protected Entity entity;
	private double gravity = 1;
	
	public GunsmokeEntityWrapper( Entity entity ) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	@Override
	public Location getLocation() {
		return entity.getLocation();
	}
	
	@Override
	public void setLocation( Location location ) {
		entity.teleport( location );
	}
	
	@Override
	public Vector getVelocity() {
		return entity.getVelocity();
	}
	
	@Override
	public void setVelocity( Vector vector ) {
		GunsmokeEntityChangeVelocityEvent event = new GunsmokeEntityChangeVelocityEvent( this, entity.getVelocity(), vector );
		event.callEvent();
		
		vector = event.getNewVector();
		
		entity.setVelocity( vector );
	}

	@Override
	public double getSpeed() {
		return entity.getVelocity().length();
	}
	
	@Override
	public UUID getUUID() {
		return entity.getUniqueId();
	}

	public void despawn() {
	}
	
	public void unload() {
	}
	
	@Override
	public void remove() {
	}

	@Override
	public EnumTickResult tick() {

		return super.tick();
	}
	
	@Override
	public boolean isValid() {
		return entity.isValid();
	}
	
	public EnumEventResult onEvent( EntityDamageEvent event ) {
		return EnumEventResult.SKIPPED;
	}

	@Override
	public String getName() {
		return entity.getCustomName() == null ? entity.getName() : entity.getCustomName();
	}

	@Override
	public void setName( String name ) {
		entity.setCustomName( name );
	}
	
	
	@Override
	public double getGravity() {
		return gravity;
	}

	@Override
	public void setGravity( double gravity ) {
		this.gravity = gravity;
	}
}
