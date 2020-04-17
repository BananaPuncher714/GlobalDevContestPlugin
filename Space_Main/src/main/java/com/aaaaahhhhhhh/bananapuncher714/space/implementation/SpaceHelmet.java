package com.aaaaahhhhhhh.bananapuncher714.space.implementation;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Breathable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.EnumEventResult;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.EnumTickResult;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Tickable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperLivingEntity;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperPlayer;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.GunsmokeAirChangeEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.RightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.GunsmokeItem;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.GunsmokeItemInteractable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.ItemSlotEquipment;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.SpaceUtil;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SpaceHelmet extends GunsmokeItemInteractable implements Tickable {
	protected int amount;
	protected int max;
	protected int refillRate = 5;
	
	public SpaceHelmet( int amount, int max ) {
		this.amount = amount;
		this.max = max;
	}
	
	public int getRefillRate() {
		return refillRate;
	}
	
	public void setRefillRate( int amount ) {
		refillRate = amount;
	}
	
	public int getAmount() {
		return amount;
	}

	public void setAmount( int amount ) {
		this.amount = amount;
	}

	public int getMax() {
		return max;
	}

	public void setMax( int max ) {
		this.max = max;
	}

	@Override
	public EnumEventResult onClick( RightClickEvent event ) {
		if ( slot instanceof ItemSlotEquipment && ( ( ( ItemSlotEquipment ) slot ).getSlot() == EquipmentSlot.HAND || ( ( ItemSlotEquipment ) slot ).getSlot() == EquipmentSlot.OFF_HAND ) ) {
			event.setCancelled( true );
		}
		if ( holder instanceof GunsmokeEntityWrapperLivingEntity ) {
			GunsmokeEntityWrapperLivingEntity lEnt = ( GunsmokeEntityWrapperLivingEntity ) holder;
			ItemSlotEquipment helmet = new ItemSlotEquipment( lEnt, EquipmentSlot.HEAD );
			if ( helmet.getItem() == null || helmet.getItem().getType() == Material.AIR ) {
				helmet.setItem( getItem() );
				slot.setItem( null );
				
				return EnumEventResult.COMPLETED;
			}
		}
		return EnumEventResult.SKIPPED;
	}
	
	@Override
	public EnumEventResult onClick( GunsmokeAirChangeEvent event ) {
		if ( holder instanceof Breathable && slot instanceof ItemSlotEquipment && ( ( ItemSlotEquipment ) slot ).getSlot() == EquipmentSlot.HEAD ) {
			if ( event.getAir() < 0 && amount > 0 ) {
				int providedAir = Math.min( amount, Math.abs( event.getAir() ) );
				amount -= providedAir;
				event.setAir( event.getAir() + providedAir );
			}
		}
		return EnumEventResult.SKIPPED;
	}
	
	@Override
	public EnumTickResult tick() {
		if ( isEquipped() ) {
			if ( holder instanceof GunsmokeEntityWrapper ) {
				GunsmokeEntityWrapper pl = ( GunsmokeEntityWrapper ) holder;
				Entity entity = pl.getEntity();
				
				if ( SpaceUtil.getPlugin().getSpaceInstance().canBreath( entity ) ) {
					amount = Math.min( amount + refillRate, max );
				}
			}
			
			if ( holder instanceof GunsmokeEntityWrapperPlayer ) {
				GunsmokeEntityWrapperPlayer pl = ( GunsmokeEntityWrapperPlayer ) holder;
				double percent = amount / ( double ) max;
				pl.setLeftPercent( percent );
			}
			
			if ( holder instanceof Breathable && slot instanceof ItemSlotEquipment && ( ( ItemSlotEquipment ) slot ).getSlot() == EquipmentSlot.HEAD ) {
				Breathable breathable = ( Breathable ) holder;
				// Don't overfill, underfill, lose air, or make the user suffocate
				int airTicks = Math.max( 0, Math.min( breathable.getMaxAir() - breathable.getAir(), Math.min( amount, refillRate ) ) );
				SpaceUtil.getPlugin().getInteractiveManager().addAir( breathable, airTicks );
				amount -= airTicks;
			}
		}
		
		return EnumTickResult.CONTINUE;
	}
	
	@Override
	public void unequip() {
		if ( holder instanceof GunsmokeEntityWrapperPlayer ) {
			GunsmokeEntityWrapperPlayer pl = ( GunsmokeEntityWrapperPlayer ) holder;
			pl.setLeftPercent( -1 );
		}
		super.unequip();
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack( Material.GLASS );
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName( ChatColor.YELLOW + "Space Helmet" );
		item.setItemMeta( meta );
		
		return GunsmokeItem.markAsGunsmokeItem( item, getUUID() );
	}
}
