package com.aaaaahhhhhhh.bananapuncher714.space.core.api.sound;

import org.bukkit.Sound;

public class SoundWave {
	protected Sound sound;
	protected double volume;
	protected double pitch;
	
	public SoundWave( Sound sound, double volume, double pitch ) {
		this.sound = sound;
		this.volume = volume;
		this.pitch = pitch;
	}

	public Sound getSound() {
		return sound;
	}

	public double getVolume() {
		return volume;
	}

	public double getPitch() {
		return pitch;
	}
	
	public SoundWave copyOf() {
		return new SoundWave( sound, volume, pitch );
	}
}
