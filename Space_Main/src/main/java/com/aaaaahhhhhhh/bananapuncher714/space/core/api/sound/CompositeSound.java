package com.aaaaahhhhhhh.bananapuncher714.space.core.api.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Sound;

public class CompositeSound {
	protected Map< SoundWave, Long > sounds = new HashMap< SoundWave, Long >();
	
	public CompositeSound() {
	}
	
	public CompositeSound( SoundWave wave ) {
		sounds.put( wave, 0L );
	}
	
	public CompositeSound add( SoundWave sound, long delay ) {
		sounds.put( sound, delay );
		return this;
	}
	
	public CompositeSound add( SoundWave sound ) {
		return add( sound, 0 );
	}
	
	public CompositeSound add( Sound sound, double volume, double pitch, long delay ) {
		return add( new SoundWave( sound, volume, pitch ), delay );
	}
	
	public CompositeSound add( Sound sound, double volume, double pitch ) {
		return add( sound, volume, pitch, 0 );
	}
	
	public Map< SoundWave, Long > getSounds() {
		return sounds;
	}
	
	public CompositeSound copyOf() {
		CompositeSound sound = new CompositeSound();
		for ( Entry< SoundWave, Long > entry : sounds.entrySet() ) {
			sound.add( entry.getKey().copyOf(), entry.getValue() );
		}
		return sound;
	}
}
