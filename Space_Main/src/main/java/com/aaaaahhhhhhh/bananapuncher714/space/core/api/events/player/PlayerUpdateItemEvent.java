package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player;

import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeInteractive;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.ItemSlot;

public class PlayerUpdateItemEvent extends InteractiveEntityEvent {
	private static final HandlerList handlers = new HandlerList();
	protected ItemSlot slot;
	protected ItemStack itemSnapshot;
	
	public PlayerUpdateItemEvent( GunsmokeInteractive updater, ItemStack item, ItemSlot slot ) {
		super( updater );
		this.slot = slot;
		if ( item == null ) {
			itemSnapshot = new ItemStack( Material.AIR );
		} else {
			itemSnapshot = item.clone();
		}
	}
	
	public ItemSlot getSlot() {
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
