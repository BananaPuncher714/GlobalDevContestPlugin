package com.aaaaahhhhhhh.bananapuncher714.space.implementation;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.DamageType;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.EnumEventResult;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.HoldLeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.LeftClickBlockEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.LeftClickEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.ReleaseLeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.GunsmokeItemInteractable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.SpaceUtil;

public class TestItem extends GunsmokeItemInteractable {
	protected double damage = 0;
	
	public TestItem( double damage ) {
		this.damage = damage;
	}
	
	public double getDamage() {
		return damage;
	}
	
	@Override
	public EnumEventResult onClick( LeftClickEntityEvent event ) {
		event.setCancelled( true );
		Entity entity = event.getHitEntity();
		
		SpaceUtil.damage( SpaceUtil.getEntity( entity ), DamageType.TRUE, damage, DamageCause.CUSTOM );
		
		return super.onClick( event );
	}
	
	@Override
	public EnumEventResult onClick( LeftClickBlockEvent event ) {
		if ( holder instanceof GunsmokeEntityWrapper ) {
			( ( GunsmokeEntityWrapper ) holder ).sendMessage( "LEFT CLICK BLOCK" );
		}
		return super.onClick( event );
	}
	
	@Override
	public EnumEventResult onClick( HoldLeftClickEvent event ) {
		if ( holder instanceof GunsmokeEntityWrapper ) {
			( ( GunsmokeEntityWrapper ) holder ).sendMessage( "HOLDING LEFT CLICK" );
		}
		return super.onClick( event );
	}
	
	@Override
	public EnumEventResult onClick( ReleaseLeftClickEvent event ) {
		if ( holder instanceof GunsmokeEntityWrapper ) {
			( ( GunsmokeEntityWrapper ) holder ).sendMessage( "RELEASE BLOCK" );
		}
		return super.onClick( event );
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack( Material.OAK_SAPLING );
		
		ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable( true );
		meta.setDisplayName( "Test item" );
		item.setItemMeta( meta );
		
		return markAsGunsmokeItem( item, getUUID() );
	}
}
