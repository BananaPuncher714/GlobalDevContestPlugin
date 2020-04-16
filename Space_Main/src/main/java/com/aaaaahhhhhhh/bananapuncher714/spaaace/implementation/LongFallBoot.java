package com.aaaaahhhhhhh.bananapuncher714.spaaace.implementation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.EnumEventResult;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.InteractableDamage;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapperLivingEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityDamageEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.RightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item.GunsmokeItem;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item.GunsmokeItemInteractable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item.ItemSlotEquipment;

public class LongFallBoot extends GunsmokeItemInteractable implements InteractableDamage {
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
	public EnumEventResult onTakeDamage( GunsmokeEntityDamageEvent event ) {
		if ( event.getCause() == DamageCause.FALL ) {
			event.setCancelled( slot instanceof ItemSlotEquipment && ( ( ItemSlotEquipment ) slot ).getSlot() == EquipmentSlot.FEET );
			return EnumEventResult.COMPLETED;
		}
		return EnumEventResult.SKIPPED;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack( Material.IRON_HORSE_ARMOR );
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName( ChatColor.AQUA + "Long Fall Boots" );
		item.setItemMeta( meta );
		
		return GunsmokeItem.markAsGunsmokeItem( item, getUUID() );
	}
}
