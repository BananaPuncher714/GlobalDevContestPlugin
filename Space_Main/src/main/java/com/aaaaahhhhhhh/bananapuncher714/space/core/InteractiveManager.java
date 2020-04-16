package com.aaaaahhhhhhh.bananapuncher714.space.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Breathable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeInteractive;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperLivingEntity;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperPlayer;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.GunsmokeAirChangeEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.HoldLeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.HoldRightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.InstabreakBlockEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.LeftClickBlockEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.LeftClickEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.LeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.PlayerUpdateItemEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.ReleaseLeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.ReleaseRightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.RightClickEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.RightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.GunsmokeItem;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.ItemSlotEquipment;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.BukkitUtil;

/**
 * Manage what GunsmokeInteractive objects are performing what actions.
 * 
 * @author BananaPuncher714
 */
public class InteractiveManager {
	private final Set< UUID > holding = new HashSet< UUID >();
	private final Set< UUID > mining = new HashSet< UUID >();
	private final Map< UUID, ItemStack[] > heldItems = new HashMap< UUID, ItemStack[] >();
	private SpaceCore plugin;
	
	protected InteractiveManager( SpaceCore plugin ) {
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
				if ( interactive.isHoldingRightClick() ) {
					new HoldRightClickEvent( interactive, interactive.getTicksSinceRightClick() ).callEvent();
				} else {
					it.remove();
					new ReleaseRightClickEvent( interactive ).callEvent();
				}
			} else {
				it.remove();
			}
		}
		
		for ( Iterator< UUID > it = mining.iterator(); it.hasNext(); ) {
			UUID uuid = it.next();
			GunsmokeRepresentable representable = plugin.getItemManager().get( uuid );
			if ( representable instanceof GunsmokeEntityWrapperPlayer ) {
				GunsmokeEntityWrapperPlayer wrapper = ( GunsmokeEntityWrapperPlayer ) representable;
				Player player = wrapper.getEntity();
				// If the player isn't actually mining anything
				if ( !plugin.getHandler().isMiningBlock( player ) ) {
					// First check if their wrapper has updated
					if ( !wrapper.isMiningBlock() ) {
						// If so, then set it
						new ReleaseLeftClickEvent( wrapper ).callEvent();
					}

					it.remove();
					continue;
				} else if ( wrapper.isMiningBlock() ) {
					new HoldLeftClickEvent( wrapper, wrapper.getTicksSinceMineStart() ).callEvent();
				}
				
			} else if ( representable instanceof GunsmokeInteractive ) {
				GunsmokeInteractive interactive = ( GunsmokeInteractive ) representable;
				
				if ( !interactive.isHoldingRightClick() ) {
					interactive.setIsHoldingRightClick( false );
					new HoldLeftClickEvent( interactive, interactive.getTicksSinceMineStart() ).callEvent();
				} else {
					it.remove();
					new ReleaseLeftClickEvent( interactive ).callEvent();
				}
			} else {
				it.remove();
			}
		}
		
		// Detect left clicking and call appropriate events
		
		// Update the holding for all the players
		// If it's not a player, then it'll have to be done manually by whatever other implementation
		for ( Player player : Bukkit.getOnlinePlayers() ) {
			if ( player.isDead() ) {
				heldItems.remove( player.getUniqueId() );
				continue;
			}
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
		
		// Also, apply fast digging and slow digging
		for ( Player player : Bukkit.getOnlinePlayers() ) {
			boolean foundHaste = false;
			boolean foundFatigue = false;
			for ( PotionEffect effect : player.getActivePotionEffects() ) {
				if ( effect.getType() == PotionEffectType.FAST_DIGGING && effect.getAmplifier() == 255 ) {
					foundHaste = true;
				}
				if ( effect.getType() == PotionEffectType.FAST_DIGGING && effect.getAmplifier() == 8 ) {
					foundFatigue = true;
				}
			}
			
			if ( !foundHaste ) {
				player.addPotionEffect( new PotionEffect( PotionEffectType.FAST_DIGGING, 20000000, 255, true, false, false ), true );
			}
			if ( !foundFatigue ) {
				player.addPotionEffect( new PotionEffect( PotionEffectType.SLOW_DIGGING, 20000000, 8, true, false, false ), true );
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
					gItem.unequip();
				}
			}
		}
	}
	
	public void setMining( GunsmokeInteractive interactive, Location mining ) {
		boolean wasHolding = interactive.isMiningBlock();
		if ( wasHolding ^ ( mining != null ) ) {
			if ( mining != null ) {
				new LeftClickBlockEvent( interactive, mining ).callEvent();
			} else {
				new ReleaseLeftClickEvent( interactive ).callEvent();
			}
		}

		if ( mining != null ) {
			this.mining.add( interactive.getUUID() );
		} else {
			this.mining.remove( interactive.getUUID() );
		}
		interactive.setMining( mining );
	}
	
	public void setHolding( GunsmokeInteractive interactive, boolean isHolding ) {
		boolean wasHolding = interactive.isHoldingRightClick();
		if ( wasHolding ^ isHolding ) {
			if ( isHolding ) {
				new RightClickEvent( interactive ).callEvent();
			} else {
				new ReleaseRightClickEvent( interactive ).callEvent();
			}
		}

		if ( isHolding ) {
			holding.add( interactive.getUUID() );
		} else {
			holding.remove( interactive.getUUID() );
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
	
	public Location getMiningLocation( HumanEntity entity ) {
		GunsmokeEntityWrapper wrapper = plugin.getItemManager().getEntityWrapper( entity );
		if ( wrapper instanceof GunsmokeInteractive ) {
			return ( ( GunsmokeInteractive ) wrapper ).getMiningBlock();
		}
		return null;
	}
	
	public int getTickHoldingTime( HumanEntity player ) {
		GunsmokeEntityWrapper wrapper = plugin.getItemManager().getEntityWrapper( player );
		if ( wrapper instanceof GunsmokeInteractive ) {
			return ( ( GunsmokeInteractive ) wrapper ).getTicksSinceRightClick();
		}
		return 0;
	}
	
	public int getTickMiningTime( HumanEntity player ) {
		GunsmokeEntityWrapper wrapper = plugin.getItemManager().getEntityWrapper( player );
		if ( wrapper instanceof GunsmokeInteractive ) {
			return ( ( GunsmokeInteractive ) wrapper ).getTicksSinceMineStart();
		}
		return 0;
	}
	
	public void instabreak( GunsmokeInteractive player, Location location ) {
		new InstabreakBlockEvent( player, location ).callEvent();
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
	
	public void setAir( Breathable breathable, int amount ) {
		GunsmokeAirChangeEvent event = new GunsmokeAirChangeEvent( breathable, amount );
		event.callEvent();
		
		if ( !event.isCancelled() ) {
			breathable.setAir( event.getAir() );
		}
	}
}
