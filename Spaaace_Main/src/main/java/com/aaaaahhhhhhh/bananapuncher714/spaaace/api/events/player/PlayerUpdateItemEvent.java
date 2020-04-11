package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerUpdateItemEvent extends HumanEntityEvent {
	private static final HandlerList handlers = new HandlerList();
	protected EquipmentSlot slot;
	protected ItemStack itemSnapshot;
	
	public PlayerUpdateItemEvent( HumanEntity updater, ItemStack item, EquipmentSlot slot ) {
		super( updater );
		this.slot = slot;
		if ( item == null ) {
			itemSnapshot = new ItemStack( Material.AIR );
		} else {
			itemSnapshot = item.clone();
		}
	}
	
	public EquipmentSlot getSlot() {
		return slot;
	}
	
	public ItemStack getItem() {
		return itemSnapshot;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
