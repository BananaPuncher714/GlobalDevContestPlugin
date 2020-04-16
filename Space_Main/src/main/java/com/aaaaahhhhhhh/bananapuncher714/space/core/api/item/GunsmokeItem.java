package com.aaaaahhhhhhh.bananapuncher714.space.core.api.item;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.GunsmokeRepresentable;

import io.github.bananapuncher714.nbteditor.NBTEditor;

public abstract class GunsmokeItem extends GunsmokeRepresentable {
	private final static Object[] CUSTOM = { "io", "github", "bananapuncher714", "operation", "gunsmoke", "item", "id" };
	
	protected GunsmokeRepresentable holder;
	protected ItemSlot slot;
	protected boolean isEquipped = false;
	
	public void equip( GunsmokeRepresentable gunsmokeEntity, ItemSlot slot ) {
		this.holder = gunsmokeEntity;
		this.slot = slot;
		isEquipped = true;
	}
	
	public void unequip() {
		holder = null;
		slot = null;
		isEquipped = false;
	}
	
	public boolean isEquipped() {
		return isEquipped;
	}
	
	public boolean compatibleWith( ItemSlot slot ) {
		return true;
	}
	
	@Override
	public void remove() {
		if ( isEquipped ) {
			unequip();
		}
	}
	
	public void updateItem() {
		if ( slot != null ) {
			slot.setItem( getItem() );
		}
	}
	
	public abstract ItemStack getItem();
	
	protected static ItemStack markAsGunsmokeItem( ItemStack item, UUID uuid ) {
		return NBTEditor.set( item, uuid.toString(), CUSTOM );
	}
	
	public static UUID getUUID( ItemStack item ) {
		String value = NBTEditor.getString( item, CUSTOM );
		if ( value != null ) {
			return UUID.fromString( value );
		}
		return null;
	}
}
