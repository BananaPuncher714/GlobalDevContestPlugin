package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.item;

import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.util.BukkitUtil;

import io.github.bananapuncher714.nbteditor.NBTEditor;

public abstract class GunsmokeItem extends GunsmokeRepresentable {
	private final static Object[] CUSTOM = { "io", "github", "bananapuncher714", "operation", "gunsmoke", "item", "id" };
	
	protected LivingEntity holder;
	protected GunsmokeEntity gunsmokeHolder;
	protected EquipmentSlot slot;
	protected boolean isEquipped = false;
	
	public void onEquip( LivingEntity entity, GunsmokeEntity gunsmokeEntity, EquipmentSlot slot ) {
		this.holder = entity;
		this.gunsmokeHolder = gunsmokeEntity;
		this.slot = slot;
		isEquipped = true;
	}
	
	public void onUnequip() {
		holder = null;
		gunsmokeHolder = null;
		slot = null;
		isEquipped = false;
	}
	
	public EquipmentSlot getEquippedSlot() {
		return slot;
	}
	
	public LivingEntity getHolder() {
		return holder;
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
		if ( holder != null ) {
			BukkitUtil.setEquipment( holder, getItem(), slot );
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
