package com.aaaaahhhhhhh.bananapuncher714.spaaace.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapperLivingEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapperPlayer;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.HoldRightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.LeftClickEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.LeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.PlayerUpdateItemEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.ReleaseRightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.RightClickEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.RightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item.GunsmokeItem;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item.ItemSlotEquipment;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.BukkitUtil;

/**
 * Manage what GunsmokeInteractive objects are performing what actions.
 * 
 * @author BananaPuncher714
 */
public class InteractiveManager {
	private final Set< UUID > holding = new HashSet< UUID >();
	private final Map< UUID, ItemStack[] > heldItems = new HashMap< UUID, ItemStack[] >();
	private SpaaaceCore plugin;
	
	protected InteractiveManager( SpaaaceCore plugin ) {
		this.plugin = plugin;
	}
	
	// Determines if the player's held item has changed and calls the appropriate event for it
	protected void tick() {
		// Detect right clicking and call appropriate events
		for ( Iterator< UUID > it = holding.iterator(); it.hasNext(); ) {
			UUID uuid = it.next();
			GunsmokeRepresentable representable = plugin.getItemManager().get( uuid );
			if ( representable instanceof GunsmokeInteractive ) {
				GunsmokeInteractive interactive = ( GunsmokeInteractive ) representable;
				new HoldRightClickEvent( interactive, interactive.getTicksSinceRightClick() ).callEvent();
			} else {
				it.remove();
			}
		}
		
		// Update the holding for all the players
		// If it's not a player, then it'll have to be done manually by whatever other implementation
		for ( Player player : Bukkit.getOnlinePlayers() ) {
			GunsmokeEntityWrapperPlayer wrapper = ( GunsmokeEntityWrapperPlayer ) plugin.getItemManager().getEntityWrapper( player );
			ItemStack[] items = heldItems.get( player.getUniqueId() );
			if ( items == null ) {
				items = new ItemStack[] { new ItemStack( Material.AIR ),
						new ItemStack( Material.AIR ),
						new ItemStack( Material.AIR ),
						new ItemStack( Material.AIR ),
						new ItemStack( Material.AIR ),
						new ItemStack( Material.AIR )};
				heldItems.put( player.getUniqueId(), items );
			}
			
			int index = 0;
			for ( EquipmentSlot slot : EquipmentSlot.values() ) {
				ItemStack newItem = BukkitUtil.getEquipment( player, slot );
				if ( newItem != null ) {
					newItem = newItem.clone();
				}
				
				if ( newItem == null && items[ index ] == null ) {
					index++;
					continue;
				} else if ( newItem == null ^ items[ index ] == null ) {
					new PlayerUpdateItemEvent( wrapper, items[ index ], new ItemSlotEquipment( wrapper, slot ) ).callEvent();
				} else if ( newItem.hashCode() != items[ index ].hashCode() ) {
					ItemStack old = items[ index ];
					
					GunsmokeRepresentable oldRep = plugin.getItemManager().getRepresentable( old );
					GunsmokeRepresentable newRep = plugin.getItemManager().getRepresentable( newItem );
					
					// Don't bother calling the events if the item they're holding is the same GunsmokeRepresentable
					if ( oldRep != null && oldRep == newRep ) {
						items[ index++ ] = newItem; 
						continue;
					}
					new PlayerUpdateItemEvent( wrapper, old, new ItemSlotEquipment( wrapper, slot ) ).callEvent();
				}

				items[ index++ ] = newItem; 
			}
		}
	}
	
	protected void remove( GunsmokeInteractive player ) {
		setHolding( player, false );

		heldItems.remove( player.getUUID() );
		
		if ( player instanceof GunsmokeEntityWrapperLivingEntity ) {
			GunsmokeEntityWrapperLivingEntity entity = ( GunsmokeEntityWrapperLivingEntity ) player;
			for ( EquipmentSlot slot : EquipmentSlot.values() ) {
				ItemStack item = BukkitUtil.getEquipment( entity.getEntity(), slot );
				GunsmokeRepresentable rep = plugin.getItemManager().getRepresentable( item );
				if ( rep instanceof GunsmokeItem ) {
					GunsmokeItem gItem = ( GunsmokeItem ) rep;
					gItem.onUnequip();
				}
			}
		}
	}
	
	public void setHolding( GunsmokeInteractive entity, boolean isHolding ) {
		GunsmokeInteractive interactive = ( GunsmokeInteractive ) entity;
		boolean wasHolding = interactive.isHoldingRightClick();
		if ( wasHolding ^ isHolding ) {
			if ( isHolding ) {
				new RightClickEvent( interactive ).callEvent();
			} else {
				new ReleaseRightClickEvent( interactive ).callEvent();
			}
		}

		if ( isHolding ) {
			holding.add( entity.getUUID() );
		} else {
			holding.remove( entity.getUUID() );
		}
		interactive.setIsHoldingRightClick( isHolding );
	}
	
	public boolean isHolding( HumanEntity entity ) {
		GunsmokeEntityWrapper wrapper = plugin.getItemManager().getEntityWrapper( entity );
		if ( wrapper instanceof GunsmokeInteractive ) {
			return ( ( GunsmokeInteractive ) wrapper ).isHoldingRightClick();
		}
		return false;
	}
	
	public int getTickHoldingTime( HumanEntity player ) {
		GunsmokeEntityWrapper wrapper = plugin.getItemManager().getEntityWrapper( player );
		if ( wrapper instanceof GunsmokeInteractive ) {
			return ( ( GunsmokeInteractive ) wrapper ).getTicksSinceRightClick();
		}
		return 0;
	}

	public void rightClick( GunsmokeInteractive player, Cancellable parent ) {
		RightClickEvent event = new RightClickEvent( player );
		event.callEvent();
		if ( event.isCancelled() ) {
			parent.setCancelled( true );
		}
	}
	
	public void leftClick( GunsmokeInteractive player, Cancellable parent ) {
		LeftClickEvent event = new LeftClickEvent( player );
		event.callEvent();
		if ( event.isCancelled() ) {
			parent.setCancelled( true );
		}
	}
	
	public void rightClickEntity( GunsmokeInteractive player, Entity clicked, Cancellable parent ) {
		RightClickEntityEvent event = new RightClickEntityEvent( player, clicked );
		event.callEvent();
		if ( event.isCancelled() ) {
			parent.setCancelled( true );
		}
	}
	
	public void leftClickEntity( GunsmokeInteractive player, Entity clicked, Cancellable parent ) {
		LeftClickEvent event = new LeftClickEntityEvent( player, clicked );
		event.callEvent();
		if ( event.isCancelled() ) {
			parent.setCancelled( true );
		}
	}
}
