package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.SoundMixer;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.LocationTrackable;

public class SoundSet {
	protected Map< String, CompositeSound > sounds = new HashMap< String, CompositeSound >();
	protected Map< UUID, BukkitRunnable > playing = new HashMap< UUID, BukkitRunnable >();
	
	public void setSound( String id, CompositeSound sound ) {
		if ( sound == null ) {
			sounds.remove( id );
		} else {
			sounds.put( id, sound );
		}
	}
	
	public void setSound( String id, SoundWave sound ) {
		if ( sound == null ) {
			sounds.remove( id );
		} else {
			sounds.put( id, new CompositeSound( sound ) );
		}
	}
	
	public CompositeSound getSound( String id ) {
		return sounds.get( id );
	}
	
	public UUID play( LocationTrackable location, double distance, String id ) {
		if ( sounds.containsKey( id ) ) {
			UUID uuid = UUID.randomUUID();
			playing.put( uuid, SoundMixer.playSound( location, distance, sounds.get( id ), new Runnable() {
				@Override
				public void run() {
					playing.remove( uuid );
				}
			} ) );
			return uuid;
		}
		return null;
	}
	
	public boolean stop( UUID uuid ) {
		if ( playing.containsKey( uuid ) ) {
			playing.remove( uuid ).cancel();
			return true;
		}
		return false;
	}
	
	public void stopAll() {
		for ( BukkitRunnable runnable : playing.values() ) {
			runnable.cancel();
		}
		playing.clear();
	}
}
