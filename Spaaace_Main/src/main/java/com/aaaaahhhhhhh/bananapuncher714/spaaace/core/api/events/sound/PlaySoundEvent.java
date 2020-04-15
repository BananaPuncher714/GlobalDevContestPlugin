package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.sound;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.sound.SoundWave;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.SpaaaceUtil;

public class PlaySoundEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	
	protected final Location location;
	protected SoundWave sound;
	protected double distance;
	protected boolean cancelled = false;

	public PlaySoundEvent( Location location, double distance, SoundWave wave ) {
		this.location = location;
		this.distance = distance;
		sound = wave;
	}
	
	public Location getLocation() {
		return location;
	}

	public SoundWave getSound() {
		return sound;
	}

	public void setSound( SoundWave wave ) {
		sound = wave;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance( double distance ) {
		this.distance = distance;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled( boolean cancelled ) {
		this.cancelled = cancelled;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public void callEvent() {
		SpaaaceUtil.callEventSync( this );
	}
}
