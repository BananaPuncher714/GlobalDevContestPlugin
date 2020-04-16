package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item;

import org.bukkit.inventory.ItemStack;

public abstract class ItemSlot {
	private final String id;
	
	public ItemSlot( String id ) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public abstract void setItem( ItemStack item );
	public abstract ItemStack getItem();
	
	public abstract void setRepresentable( GunsmokeItem item );
	public abstract GunsmokeItem getRepresentable();
}
