package com.aaaaahhhhhhh.bananapuncher714.spaaace.core;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.LocationTrackable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.sound.PlaySoundEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.sound.CompositeSound;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.sound.SoundWave;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.SpaaaceUtil;

public class SoundMixer {

	/**
	 * Play a SoundWave at a given location for all to hear
	 * 
	 * @param location
	 * The origin
	 * @param distance
	 * The max distance from which the sound can be heard, in blocks
	 * @param sound
	 * The sound itself
	 */
	public static void playSound( Location location, double distance, SoundWave sound ) {
		PlaySoundEvent event = new PlaySoundEvent( location, distance, sound );
		Bukkit.getPluginManager().callEvent( event );
		if ( event.isCancelled() ) {
			return;
		}
		sound = event.getSound();
		distance = event.getDistance();
		double distanceSquared = distance * distance;
		for ( Player player : location.getWorld().getPlayers() ) {
			Location playerLoc = player.getEyeLocation();
			double distSq = location.distanceSquared( playerLoc );
			if ( distSq <= distanceSquared ) {
				player.playSound( location, sound.getSound(), ( float ) sound.getVolume(), ( float ) sound.getPitch() );
			}
		}
	}
	
	public static void playSound( Location location, double distance, Sound sound, double volume, double pitch ) {
		playSound( location, distance, new SoundWave( sound, volume, pitch ) );
	}
	
	public static BukkitRunnable playSound( Location location, double distance, CompositeSound sound ) {
		return playSound( () -> location, distance, sound, null );
	}
	
	public static BukkitRunnable playSound( LocationTrackable location, double distance, CompositeSound sound ) {
		return playSound( location, distance, sound, null );
	}
	
	public static BukkitRunnable playSound( LocationTrackable location, double distance, CompositeSound sound, Runnable runAfter ) {
		BukkitRunnable runnable = new BukkitRunnable() {
			private int tick = 0;
			private CompositeSound sounds = sound.copyOf();
			
			@Override
			public void run() {
				long time = tick++ * 20;
				
				for ( Iterator< Entry< SoundWave, Long > > iterator = sounds.getSounds().entrySet().iterator(); iterator.hasNext(); ) {
					Entry< SoundWave, Long > entry = iterator.next();
					
					if ( entry.getValue() < time ) {
						playSound( location.getLocation(), distance, entry.getKey() );
						iterator.remove();
					}
				}
				
				if ( sounds.getSounds().isEmpty() ) {
					cancel();
					if ( runAfter != null ) {
						runAfter.run();
					}
				}
			}
		};
		
		runnable.runTaskTimer( SpaaaceUtil.getPlugin(), 0, 1 );
		
		return runnable;
	}
}
