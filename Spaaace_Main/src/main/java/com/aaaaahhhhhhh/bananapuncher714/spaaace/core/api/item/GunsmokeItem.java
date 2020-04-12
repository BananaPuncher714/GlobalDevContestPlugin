package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;

import io.github.bananapuncher714.nbteditor.NBTEditor;

public abstract class GunsmokeItem extends GunsmokeRepresentable {
	private final static Object[] CUSTOM = { "io", "github", "bananapuncher714", "operation", "gunsmoke", "item", "id" };
	
	protected GunsmokeInteractive gunsmokeHolder;
	protected ItemSlot slot;
	protected boolean isEquipped = false;
	
	public void onEquip( GunsmokeInteractive gunsmokeEntity, ItemSlot slot ) {
		this.gunsmokeHolder = gunsmokeEntity;
		this.slot = slot;
		isEquipped = true;
	}
	
	public void onUnequip() {
		gunsmokeHolder = null;
		slot = null;
		isEquipped = false;
	}
	
	public boolean isEquipped() {
		return isEquipped;
	}
	
	public void dualWield( GunsmokeItem other ) {
	}
	
	public boolean canDualWieldWith( GunsmokeItem other ) {
		return true;
	}
	
	@Override
	public void remove() {
		if ( isEquipped ) {
			onUnequip();
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
