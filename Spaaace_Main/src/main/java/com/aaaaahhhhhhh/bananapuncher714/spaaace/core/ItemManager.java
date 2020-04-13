package com.aaaaahhhhhhh.bananapuncher714.spaaace.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.DamageType;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.EnumEventResult;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.EnumTickResult;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.GunsmokeEntityWrapperFactory;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.InteractableDamage;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.RegenType;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.Storeable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.Tickable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapperPlayer;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapperProjectile;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityDamageEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityDeathEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityDespawnEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityUnloadEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.AdvancementOpenEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.DropItemEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.HoldLeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.HoldRightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.LeftClickBlockEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.LeftClickEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.LeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.PlayerUpdateItemEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.ReleaseLeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.ReleaseRightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.RightClickEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.RightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item.GunsmokeItem;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item.GunsmokeItemInteractable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.item.ItemSlot;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.BukkitUtil;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.SpaaaceUtil;

public class ItemManager implements Listener {
	private SpaaaceCore plugin;
	private Map< UUID, GunsmokeRepresentable > items = new ConcurrentHashMap< UUID, GunsmokeRepresentable >();
	
	public ItemManager( SpaaaceCore plugin ) {
		this.plugin = plugin;
		
		// This is to capture events to pass them onto whatever things are registered
		Bukkit.getPluginManager().registerEvents( this, plugin );
	}
	
	protected void tick() {
		for ( Iterator< Entry< UUID, GunsmokeRepresentable > > iterator = items.entrySet().iterator(); iterator.hasNext(); ) {
			Entry< UUID, GunsmokeRepresentable > entry = iterator.next();
			GunsmokeRepresentable item = entry.getValue();
			if ( item instanceof Tickable ) {
				Tickable tickableItem = ( Tickable ) item;
				if ( tickableItem.tick() == EnumTickResult.CANCEL ) {
					item.remove();
					iterator.remove();
				}
			}
		}
	}
	
	public void register( GunsmokeRepresentable item ) {
		items.put( item.getUUID(), item );
	}
	
	public GunsmokeRepresentable get( UUID uuid ) {
		return items.get( uuid );
	}
	
	public void remove( UUID uuid ) {
		GunsmokeRepresentable item = items.remove( uuid );
		if ( item != null ) {
			item.remove();
		}
	}
	
	public GunsmokeEntityWrapper getEntityWrapper( Entity entity ) {
		UUID uuid = entity.getUniqueId();
		GunsmokeRepresentable representable = get( uuid );
		if ( representable == null ) {
			representable = GunsmokeEntityWrapperFactory.wrap( entity );
			register( representable );
		}
		
		if ( representable instanceof GunsmokeEntityWrapper ) {
			return ( GunsmokeEntityWrapper ) representable;
		} else {
			plugin.getLogger().severe( "UUID Collision detected! " + representable );
			return null;
		}
	}
	
	public GunsmokeRepresentable getRepresentable( LivingEntity entity, EquipmentSlot slot ) {
		ItemStack item = BukkitUtil.getEquipment( entity, slot );
		return getRepresentable( item );
	}
	
	public GunsmokeRepresentable getRepresentable( ItemStack item ) {
		if ( item == null ) {
			return null;
		}
		UUID id = GunsmokeItem.getUUID( item );
		if ( id == null ) {
			return null;
		}
		
		return get( id );
	}
	
	public GunsmokeEntity getEntity( UUID uuid ) {
		GunsmokeRepresentable representable = items.get( uuid );
		if ( representable instanceof GunsmokeEntity ) {
			return ( GunsmokeEntity ) representable;
		}
		return null;
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( PlayerUpdateItemEvent event ) {
		// An item in a certain slot has been updated
		GunsmokeInteractive player = event.getEntity();
		
		// Ignore which slot got updated, just scan through them all right now
		// First, scan through all the slots
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			// TODO Add compatibility check with other slots
			for ( ItemSlot slot : storeable.getSlots() ) {
				ItemStack currentItem = slot.getItem();
				GunsmokeRepresentable current = getRepresentable( currentItem );
				GunsmokeItem previous = slot.getRepresentable();
				
				if ( previous != current ) {
					// Swap out the old one and bring in the new
					// Ignore dual wielding for now, we can add a more universal "compatible with" check later
					if ( previous != null && previous.isEquipped() ) {
						previous.unequip();
					}
					
					// Not null check
					if ( current instanceof GunsmokeItem ) {
						GunsmokeItem itemCurrent = ( GunsmokeItem ) current;

						// Unequip from old and equip to current
						if ( itemCurrent.isEquipped() ) {
							itemCurrent.unequip();
						}
						if ( player instanceof GunsmokeRepresentable ) {
							itemCurrent.equip( ( GunsmokeRepresentable ) player, slot );
						}
						
						slot.setRepresentable( itemCurrent );
					} else {
						slot.setRepresentable( null );
					}
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( AdvancementOpenEvent event ) {
		GunsmokeInteractive player = event.getEntity();
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( DropItemEvent event ) {
		GunsmokeInteractive player = event.getEntity();
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( PlayerSwapHandItemsEvent event ) {
		GunsmokeInteractive player = ( GunsmokeInteractive ) getEntityWrapper( event.getPlayer() );
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( LeftClickEntityEvent event ) {
		GunsmokeInteractive player = event.getEntity();
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( LeftClickBlockEvent event ) {
		GunsmokeInteractive player = event.getEntity();
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( HoldLeftClickEvent event ) {
		GunsmokeInteractive player = event.getEntity();
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( ReleaseLeftClickEvent event ) {
		GunsmokeInteractive player = event.getEntity();
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( LeftClickEvent event ) {
		GunsmokeInteractive player = event.getEntity();
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( RightClickEntityEvent event ) {
		GunsmokeInteractive player = event.getEntity();
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( RightClickEvent event ) {
		GunsmokeInteractive player = event.getEntity();
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( HoldRightClickEvent event ) {
		GunsmokeInteractive player = event.getEntity();
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( ReleaseRightClickEvent event ) {
		GunsmokeInteractive player = event.getEntity();
		EnumEventResult result = EnumEventResult.SKIPPED;
		
		if ( player instanceof Storeable ) {
			Storeable storeable = ( Storeable ) player;
			
			for ( ItemSlot slot : storeable.getSlots() ) {
				GunsmokeRepresentable item = getRepresentable( slot.getItem() );
				if ( item instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) item;
					
					if ( interactable.isEquipped() ) {
						result = interactable.onClick( event );
					}
				}
				
				// Continue if it's skipped or processed
				if ( !( result == EnumEventResult.SKIPPED || result == EnumEventResult.PROCESSED ) ) {
					break;
				}
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( EntityRegainHealthEvent event ) {
		plugin.getDamageManager().regen( getEntityWrapper( event.getEntity() ), event.getAmount(), RegenType.VANILLA, event.getRegainReason() );
		event.setCancelled( true );
	}
	
	@EventHandler( priority = EventPriority.HIGH )
	private void onEvent( EntityDamageEvent event ) {
		plugin.getDamageManager().damage( getEntityWrapper( event.getEntity() ), event.getDamage(), DamageType.VANILLA, event.getCause() );
		event.setCancelled( true );
	}

	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( EntityDamageByEntityEvent event ) {
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		event.setCancelled( true );
		if ( damager instanceof LivingEntity ) {
			LivingEntity entity = ( LivingEntity ) damager;
			GunsmokeRepresentable representable = getRepresentable( entity, EquipmentSlot.HAND );
			if ( representable != null ) {
				return;
			}
		}
			
		plugin.getDamageManager().damage( getEntityWrapper( damaged ), event.getDamage(), DamageType.VANILLA, getEntityWrapper( damager ) );
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( ProjectileHitEvent event ) {
		GunsmokeRepresentable entity = getEntityWrapper( event.getEntity() );
		
		if ( entity instanceof GunsmokeEntityWrapperProjectile ) {
			GunsmokeEntityWrapperProjectile projectile = ( GunsmokeEntityWrapperProjectile ) entity;
			
			projectile.onEvent( event );
		}
	}
	

	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( ProjectileLaunchEvent event ) {
		Projectile projectile = event.getEntity();
		ProjectileSource source = projectile.getShooter();
		if ( source instanceof LivingEntity ) {
			LivingEntity shooter = ( LivingEntity ) source;
			GunsmokeRepresentable main = getRepresentable( shooter, EquipmentSlot.HAND );
			if ( main != null ) {
				event.setCancelled( true );
				return;
			}
			
			GunsmokeRepresentable off = getRepresentable( shooter, EquipmentSlot.OFF_HAND );
			if ( off != null ) {
				event.setCancelled( true );
				return;
			}
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( GunsmokeEntityDamageEvent event ) {
		GunsmokeEntity damagee = event.getRepresentable();
		if ( !( damagee instanceof GunsmokeEntityWrapper ) ) {
			return;
		}
		Entity entity = ( ( GunsmokeEntityWrapper ) damagee ).getEntity();
		if ( entity instanceof LivingEntity ) {
			LivingEntity lEntity = ( LivingEntity ) entity;
			EnumEventResult result = EnumEventResult.SKIPPED;

			for ( EquipmentSlot slot : SpaaaceUtil.getEquipmentSlotOrdering() ) {
				GunsmokeRepresentable representable = getRepresentable( lEntity, slot );

				if ( representable instanceof GunsmokeItemInteractable ) {
					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) representable;

					if ( interactable.isEquipped() ) {
						if ( interactable instanceof InteractableDamage ) {
							InteractableDamage damageInteractable = ( InteractableDamage ) interactable;
							result = damageInteractable.onTakeDamage( event );
						}
					}
				}
				if ( result == EnumEventResult.COMPLETED || result == EnumEventResult.STOPPED ) {
					break;
				}
			}
		}
	}
	
//	@EventHandler( priority = EventPriority.HIGHEST )
//	private void onEvent( GunsmokeEntityDamageByEntityEvent event ) {
//		GunsmokeEntity damagee = event.getDamager();
//		if ( !( damagee instanceof GunsmokeEntityWrapper ) ) {
//			return;
//		}
//		Entity entity = ( ( GunsmokeEntityWrapper ) damagee ).getEntity();
//		if ( entity instanceof LivingEntity ) {
//			LivingEntity lEntity = ( LivingEntity ) entity;
//			EnumEventResult result = EnumEventResult.SKIPPED;
//
//			for ( EquipmentSlot slot : SpaaaceUtil.getEquipmentSlotOrdering() ) {
//				GunsmokeRepresentable representable = getRepresentable( lEntity, slot );
//
//				if ( representable instanceof GunsmokeItemInteractable ) {
//					GunsmokeItemInteractable interactable = ( GunsmokeItemInteractable ) representable;
//
//					if ( interactable.isEquipped() ) {
//						if ( interactable instanceof InteractableDamage ) {
//							InteractableDamage damageInteractable = ( InteractableDamage ) interactable;
//							result = damageInteractable.onTakeDamage( event );
//						}
//					}
//				}
//				if ( result == EnumEventResult.COMPLETED || result == EnumEventResult.STOPPED ) {
//					break;
//				}
//			}
//		}
//	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( BlockPlaceEvent event ) {
		plugin.getBlockManager().unregisterBlock( event.getBlock().getLocation() );
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( BlockFromToEvent event ) {
		plugin.getBlockManager().unregisterBlock( event.getBlock().getLocation() );
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( GunsmokeEntityDeathEvent event ) {
		// TODO do something with this, maybe make a separate gunsmoke entity unregister event?
		GunsmokeEntity entity = event.getRepresentable();
		if ( !( entity instanceof GunsmokeEntityWrapperPlayer ) ) {
			remove( entity.getUUID() );
		}
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( GunsmokeEntityUnloadEvent event ) {
		GunsmokeEntity entity = event.getRepresentable();
		
		// Don't keep an entity loaded if it doesn't have to be
		items.remove( event.getRepresentable().getUUID() );
		
		if ( entity instanceof GunsmokeEntityWrapper ) {
			( ( GunsmokeEntityWrapper ) entity ).unload();
		}
		remove( entity.getUUID() );
	}
	
	@EventHandler( priority = EventPriority.HIGHEST )
	private void onEvent( GunsmokeEntityDespawnEvent event ) {
		GunsmokeEntity entity = event.getRepresentable();
		if ( entity instanceof GunsmokeEntityWrapperPlayer ) {
			if ( entity instanceof GunsmokeEntityWrapper ) {
				( ( GunsmokeEntityWrapper ) entity ).despawn();
			}
			remove( entity.getUUID() );
		}
		remove( entity.getUUID() );
	}
}
