package com.aaaaahhhhhhh.bananapuncher714.space.implementation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.EnumEventResult;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperLivingEntity;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.HoldRightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.GunsmokeItemInteractable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.ItemSlotEquipment;

public class PowerThrusters extends GunsmokeItemInteractable {
	@Override
	public EnumEventResult onClick( HoldRightClickEvent event ) {
		if ( slot instanceof ItemSlotEquipment && ( ( ItemSlotEquipment ) slot ).getSlot() == EquipmentSlot.HAND ) {
			if ( holder instanceof GunsmokeEntityWrapperLivingEntity ) {
				GunsmokeEntityWrapperLivingEntity wrapper = ( GunsmokeEntityWrapperLivingEntity ) holder;
				
				Vector facing = wrapper.getEntity().getLocation().getDirection();
				Vector vel = wrapper.getVelocity();
				vel.add( facing.multiply( .5 ) );
				wrapper.setVelocity( vel );
			}
			
			return EnumEventResult.PROCESSED;
		}
		
		return EnumEventResult.SKIPPED;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack( Material.SHIELD );
		
		ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable( true );
		meta.setDisplayName( ChatColor.YELLOW + "Power Gloves" );
		item.setItemMeta( meta );
		
		return markAsGunsmokeItem( item, getUUID() );
	}
}
