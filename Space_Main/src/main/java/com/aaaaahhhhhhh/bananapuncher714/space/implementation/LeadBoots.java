package com.aaaaahhhhhhh.bananapuncher714.space.implementation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.EnumEventResult;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Gravitable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.InteractableDamage;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperLivingEntity;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity.GunsmokeEntityDamageEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.RightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.GunsmokeItem;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.GunsmokeItemInteractable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.ItemSlot;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.ItemSlotEquipment;

public class LeadBoots extends GunsmokeItemInteractable {
	private double oldGrav = 1;
	
	@Override
	public EnumEventResult onClick( RightClickEvent event ) {
		if ( holder instanceof GunsmokeEntityWrapperLivingEntity ) {
			GunsmokeEntityWrapperLivingEntity lEnt = ( GunsmokeEntityWrapperLivingEntity ) holder;
			ItemSlotEquipment boots = new ItemSlotEquipment( lEnt, EquipmentSlot.FEET );
			if ( boots.getItem() == null || boots.getItem().getType() == Material.AIR ) {
				boots.setItem( getItem() );
				slot.setItem( null );
				
				event.setCancelled( true );
				return EnumEventResult.COMPLETED;
			}
		}
		return EnumEventResult.SKIPPED;
	}
	
	@Override
	public void equip( GunsmokeRepresentable gunsmokeEntity, ItemSlot slot ) {
		super.equip( gunsmokeEntity, slot );
		
		if ( holder instanceof Gravitable && slot instanceof ItemSlotEquipment && ( ( ItemSlotEquipment ) slot ).getSlot() == EquipmentSlot.FEET ) {
			Gravitable entity = ( Gravitable ) holder;
			oldGrav = entity.getGravity();
			entity.setGravity( 1 );
		}
	}
	
	@Override
	public void unequip() {
		if ( holder instanceof Gravitable && slot instanceof ItemSlotEquipment && ( ( ItemSlotEquipment ) slot ).getSlot() == EquipmentSlot.FEET ) {
			Gravitable entity = ( Gravitable ) holder;
			entity.setGravity( oldGrav );
		}
		super.unequip();
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack( Material.IRON_INGOT );
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName( ChatColor.DARK_GRAY + "Lead boots" );
		item.setItemMeta( meta );
		
		return GunsmokeItem.markAsGunsmokeItem( item, getUUID() );
	}
}
