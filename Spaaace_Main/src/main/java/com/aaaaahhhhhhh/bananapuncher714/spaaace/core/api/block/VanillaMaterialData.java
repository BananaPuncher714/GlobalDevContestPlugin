package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.block;

import org.bukkit.Material;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.sound.SoundSet;

public class VanillaMaterialData {
	private final Material material;
	private float strength;
	private SoundSet sound;
	
	public VanillaMaterialData( Material material ) {
		this.material = material;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength( float strength ) {
		this.strength = strength;
	}

	public SoundSet getSounds() {
		return sound;
	}

	public void setSounds( SoundSet sound ) {
		this.sound = sound;
	}

	public Material getMaterial() {
		return material;
	}
}
