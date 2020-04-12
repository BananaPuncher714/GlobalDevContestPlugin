package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.Encapsulator;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.Storeable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityDamageEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item.GunsmokeItem;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item.ItemSlot;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item.ItemSlotEquipment;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.SpaaaceUtil;

public class GunsmokeEntityWrapperLivingEntity extends GunsmokeEntityWrapper implements GunsmokeInteractive, Storeable {
	protected LivingEntity entity;
	protected int holdRightClickStart = 0;
	protected boolean holdingRightClick = false;
	protected int miningTimeStart = 0;
	protected Location mining;
	
	private Map< EquipmentSlot, GunsmokeItem > items = new HashMap< EquipmentSlot, GunsmokeItem >();
	
	public GunsmokeEntityWrapperLivingEntity( LivingEntity entity ) {
		super( entity );
		this.entity = entity;
		this.health = entity.getHealth();
		this.maxHealth = entity.getMaxHealth();
	}

	@Override
	public LivingEntity getEntity() {
		return entity;
	}
	
	@Override
	public boolean isInvincible() {
		return super.isInvincible() || entity.isInvulnerable();
	}
	
	@Override
	public double getHealth() {
		return health;
	}

	@Override
	public void setHealth( double health ) {
		super.setHealth( health );
		double clientHp = ( health * entity.getMaxHealth() ) / maxHealth;
		if ( clientHp == 0 && health != 0 ) {
			clientHp = 1;
		}
		entity.setHealth( clientHp );
	}
	
	@Override
	public void damage( GunsmokeEntityDamageEvent event ) {
		super.damage( event );
		
		SpaaaceUtil.playHurtAnimationFor( entity );
	}
	
	@Override
	public boolean isHoldingRightClick() {
		return holdingRightClick;
	}
	
	@Override
	public void setIsHoldingRightClick( boolean holding ) {
		if ( !holdingRightClick && holding ) {
			holdRightClickStart = SpaaaceUtil.getCurrentTick();
		}
		holdingRightClick = holding;
	}
	
	@Override
	public int getTicksSinceRightClick() {
		return SpaaaceUtil.getCurrentTick() - holdRightClickStart;
	}
	
	@Override
	public boolean isMiningBlock() {
		return mining != null;
	}
	
	@Override
	public void setMining( Location location  ) {
		if ( mining == null && location != null ) {
			miningTimeStart = SpaaaceUtil.getCurrentTick();
			mining = location.clone();
		} else {
			mining = null;
		}
	}
	
	@Override
	public int getTicksSinceMineStart() {
		return SpaaaceUtil.getCurrentTick() - miningTimeStart;
	}
	
	@Override
	public void stopMining() {
		mining = null;
	}
	
	@Override
	public ItemSlot[] getSlots() {
		ItemSlot[] slots = new ItemSlot[ EquipmentSlot.values().length ];
		
		int i = 0;
		for ( EquipmentSlot slot : SpaaaceUtil.getEquipmentSlotOrdering() ) {
			ItemSlotEquipment iSlot = new ItemSlotEquipment( this, slot );
			Encapsulator< GunsmokeItem > encapsulator = new Encapsulator< GunsmokeItem >() {
				@Override
				public GunsmokeItem get() {
					return items.get( slot );
				}

				@Override
				public void set( GunsmokeItem obj ) {
					if ( obj == null ) {
						items.remove( slot );
					} else {
						items.put( slot,  obj );
					}
				}
			};
			iSlot.setEncapsulator( encapsulator );
			
			slots[ i++ ] = iSlot;
		}
		
		return slots;
	}
}
