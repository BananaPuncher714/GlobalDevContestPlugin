package com.aaaaahhhhhhh.bananapuncher714.space.implementation;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.EnumEventResult;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.block.VanillaMaterialData;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.block.GunsmokeBlockDamageEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.LeftClickBlockEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.GunsmokeItem;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.GunsmokeItemInteractable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.ItemSlotEquipment;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.SpaceUtil;

public class DebugStick extends GunsmokeItemInteractable {
	@Override
	public EnumEventResult onClick( GunsmokeBlockDamageEvent event ) {
		event.setDamage( 0 );
		return EnumEventResult.PROCESSED;
	}
	
	@Override
	public EnumEventResult onClick( LeftClickBlockEvent event ) {
		if ( slot instanceof ItemSlotEquipment && ( ( ItemSlotEquipment ) slot ).getSlot() == EquipmentSlot.HAND ) {
			Location loc = event.getLocation();
			VanillaMaterialData data = SpaceUtil.getPlugin().getHandler().getVanillaMaterialDataFor( loc.getBlock().getType() );
			if ( holder instanceof GunsmokeEntity ) {
				( ( GunsmokeEntity ) holder ).sendMessage( "Hardness: " + data.getStrength() );
			}
		}
		return EnumEventResult.SKIPPED;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack( Material.STICK );
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName( ChatColor.WHITE + "Debug Stick" );
		item.setItemMeta( meta );
		
		return GunsmokeItem.markAsGunsmokeItem( item, getUUID() );
	}
}