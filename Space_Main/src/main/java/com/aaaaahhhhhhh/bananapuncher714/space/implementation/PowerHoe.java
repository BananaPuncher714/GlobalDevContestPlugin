package com.aaaaahhhhhhh.bananapuncher714.space.implementation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.EnumEventResult;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.block.GunsmokeBlockDamageEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.GunsmokeItemInteractable;

public class PowerHoe extends GunsmokeItemInteractable {
	
	@Override
	public EnumEventResult onClick( GunsmokeBlockDamageEvent event ) {
		double damage = getDamageTo( event.getRepresentable().getLocation().getBlock().getType() );
		event.setDamage( damage );
		return super.onClick( event );
	}
	
	protected double getDamageTo( Material material ) {
		switch ( material ) {
		case GRASS:
		case TALL_GRASS:
		case WHEAT:
		case MELON:
		case PUMPKIN:
		case SUGAR_CANE:
		case CARROT:
		case BEETROOT:
		case POTATO:
			return 10000;
		default: return 0;
		}
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack( Material.WOODEN_HOE );
		
		ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable( true );
		meta.setDisplayName( ChatColor.YELLOW + "Power Hoe" );
		item.setItemMeta( meta );
		
		return markAsGunsmokeItem( item, getUUID() );
	}
}
