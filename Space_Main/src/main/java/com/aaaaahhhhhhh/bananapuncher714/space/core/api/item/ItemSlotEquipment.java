package com.aaaaahhhhhhh.bananapuncher714.space.core.api.item;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Encapsulator;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperLivingEntity;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.BukkitUtil;

public class ItemSlotEquipment extends ItemSlot {
	private GunsmokeEntityWrapperLivingEntity entity;
	private EquipmentSlot slot;
	
	private Encapsulator< GunsmokeItem > encapsulator;
	
	public ItemSlotEquipment( GunsmokeEntityWrapperLivingEntity entity, EquipmentSlot slot ) {
		super( slot.name() );
		Validate.notNull( slot );
		Validate.notNull( entity );
		this.slot = slot;
		this.entity = entity;
	}

	public Encapsulator< GunsmokeItem > getEncapsulator() {
		return encapsulator;
	}

	public void setEncapsulator( Encapsulator< GunsmokeItem > setget ) {
		this.encapsulator = setget;
	}

	@Override
	public void setItem( ItemStack item ) {
		BukkitUtil.setEquipment( entity.getEntity(), item, slot );
	}

	@Override
	public ItemStack getItem() {
		return BukkitUtil.getEquipment( entity.getEntity(), slot );
	}
	
	public EquipmentSlot getSlot() {
		return slot;
	}

	@Override
	public void setRepresentable( GunsmokeItem item ) {
		if ( encapsulator != null ) {
			encapsulator.set( item );
		}
	}

	@Override
	public GunsmokeItem getRepresentable() {
		if ( encapsulator != null ) {
			return encapsulator.get();
		}
		return null;
	}
}
