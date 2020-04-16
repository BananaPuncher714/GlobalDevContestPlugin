package com.aaaaahhhhhhh.bananapuncher714.space.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeInteractive;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperLivingEntity;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.GunsmokeAirChangeEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity.GunsmokeEntityDeathEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity.GunsmokeEntityDespawnEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.PlayerUpdateItemEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.BukkitUtil;

public class PlayerListener implements Listener {
	public static final long PRONE_DELAY = 500;
	
	private SpaceCore plugin;
	
	private Map< UUID, Integer > interactLastCalled = new HashMap< UUID, Integer >();
	private Map< UUID, Integer > instabreakLastCalled = new HashMap< UUID, Integer >();
	
	// Allow us to specify when to breaking blocks is allowed
	private boolean allowBlockBreak = false;
	
	protected PlayerListener( SpaceCore plugin ) {
		this.plugin = plugin;
	}
	
	@EventHandler
	private void onPlayerDeathEvent( PlayerDeathEvent event ) {
		GunsmokeRepresentable representable = plugin.getItemManager().get( event.getEntity().getUniqueId() );
		
		if ( representable instanceof GunsmokeInteractive ) {
			plugin.getInteractiveManager().remove( ( GunsmokeInteractive ) representable );
			if ( !plugin.getHandler().isRealPlayer( event.getEntity() ) ) {
				Bukkit.getScheduler().runTaskLater( plugin, () -> {
					plugin.getNPCManager().remove( event.getEntity().getUniqueId() );
				}, 40 );
			} else {
				GunsmokeEntityDeathEvent deathEvent = new GunsmokeEntityDeathEvent( plugin.getItemManager().getEntityWrapper( event.getEntity() ) );
				deathEvent.callEvent();
			}
		}
	}
	
	@EventHandler
	private void onPlayerRespawnEvent( PlayerRespawnEvent event ) {
		GunsmokeRepresentable representable = plugin.getItemManager().get( event.getPlayer().getUniqueId() );
		if ( representable instanceof GunsmokeInteractive ) {
			plugin.getInteractiveManager().remove( ( GunsmokeInteractive ) representable );
		}
	}
	
	@EventHandler
	private void onPlayerTeleportEvent( PlayerTeleportEvent event ) {
		GunsmokeRepresentable representable = plugin.getItemManager().get( event.getPlayer().getUniqueId() );
		if ( representable instanceof GunsmokeInteractive ) {
			plugin.getInteractiveManager().setHolding( ( GunsmokeInteractive ) representable, false );
		}
	}
	
	@EventHandler
	private void onPlayerOpenInventoryEvent( InventoryOpenEvent event ) {
		GunsmokeRepresentable representable = plugin.getItemManager().get( event.getPlayer().getUniqueId() );
		if ( representable instanceof GunsmokeInteractive ) {
			plugin.getInteractiveManager().setHolding( ( GunsmokeInteractive ) representable, false );
		}
	}
	
	@EventHandler
	private void onPlayerSwitchItem( PlayerUpdateItemEvent event ) {
		plugin.getInteractiveManager().setHolding( event.getEntity(), false );
	}
	
	@EventHandler
	private void onPlayerQuitEvent( PlayerQuitEvent event ) {
		GunsmokeRepresentable representable = plugin.getItemManager().get( event.getPlayer().getUniqueId() );
		if ( representable instanceof GunsmokeInteractive ) {
			plugin.getInteractiveManager().remove( ( GunsmokeInteractive ) representable );
		}
		GunsmokeEntityDespawnEvent despawnEvent = new GunsmokeEntityDespawnEvent( plugin.getItemManager().getEntityWrapper( event.getPlayer() ) );
		despawnEvent.callEvent();
	}

	// This is because bukkit has an abyssmal player left click detection system for adventure mode people
	@EventHandler( ignoreCancelled = false )
	private void onPlayerAnimationEvent( PlayerAnimationEvent event ) {
		if ( event.getPlayer().getGameMode() == GameMode.ADVENTURE ) {
			if ( callInteract( event.getPlayer() ) ) {
				GunsmokeRepresentable representable = plugin.getItemManager().get( event.getPlayer().getUniqueId() );
				if ( representable instanceof GunsmokeInteractive ) {
					plugin.getInteractiveManager().leftClick( ( GunsmokeInteractive ) representable, event );
				}
			} else {
				event.setCancelled( true );
			}
		}
	}
	
	@EventHandler
	private void onPlayerLeftClickEntityEvent( EntityDamageByEntityEvent event ) {
		if ( event.getDamager() instanceof Player ) {
			Player player = ( Player ) event.getDamager();
			if ( callInteract( player ) ) {
				GunsmokeRepresentable representable = plugin.getItemManager().get( player.getUniqueId() );
				if ( representable instanceof GunsmokeInteractive ) {
					plugin.getInteractiveManager().leftClickEntity( ( GunsmokeInteractive ) representable, event.getEntity(), event );
				}
			} else {
				event.setCancelled( true );
			}
		}
	}

	@EventHandler
	private void onPlayerInteractEntityEvent( PlayerInteractEntityEvent event ) {
		if ( event.getHand() == EquipmentSlot.HAND && !plugin.getInteractiveManager().isHolding( event.getPlayer() ) ) {
			GunsmokeRepresentable representable = plugin.getItemManager().get( event.getPlayer().getUniqueId() );
			if ( representable instanceof GunsmokeInteractive ) {
				plugin.getInteractiveManager().rightClickEntity( ( GunsmokeInteractive ) representable, event.getRightClicked(), event );
			}
		}
	}
	
	@EventHandler
	private void onPlayerInteractEvent( PlayerInteractEvent event ) {
		if ( event.getHand() != EquipmentSlot.HAND ) {
			return;
		}
		// This gets called twice if a player breaks a block without anything behind it
		// It occurs when opening a door and there is nothing behind it
		// Does not happen if the BlockBreakEvent is cancelled accordingly
		
		Player player = event.getPlayer();
		Action action = event.getAction();
		
		GunsmokeRepresentable representable = plugin.getItemManager().get( player.getUniqueId() );
		
		if ( action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK ) {
			if ( callInteract( player ) ) {
				if ( representable instanceof GunsmokeInteractive ) {
					plugin.getInteractiveManager().leftClick( ( GunsmokeInteractive ) representable, event );
				}
			} else {
				event.setCancelled( true );
			}
		} else if ( action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK ) {
			if ( !BukkitUtil.isRightClickable( player.getEquipment().getItemInMainHand().getType() ) ) {
				
				if ( representable instanceof GunsmokeInteractive ) {
					plugin.getInteractiveManager().rightClick( ( GunsmokeInteractive ) representable, event );
				}
			}
		}
	}
	
	@EventHandler( ignoreCancelled = true )
	private void onBlockDamageEvent( BlockDamageEvent event ) {
		Player player = event.getPlayer();
		GunsmokeRepresentable representable = plugin.getItemManager().get( player.getUniqueId() );
		if ( representable instanceof GunsmokeInteractive ) {
			GunsmokeInteractive interactive = ( GunsmokeInteractive ) representable;
			if ( event.getInstaBreak() ) {
				int tick = plugin.getHandler().getServerTick();
				int prevTick = instabreakLastCalled.getOrDefault( player.getUniqueId(), 0 );
				instabreakLastCalled.put( player.getUniqueId(), tick );
				
				if ( tick != prevTick ) {
					Bukkit.getScheduler().runTaskLater( plugin, () -> {
						plugin.getInteractiveManager().instabreak( interactive, event.getBlock().getLocation() );
					}, 1 );
				}
			} else {
				Bukkit.getScheduler().runTaskLater( plugin, () -> {
					plugin.getInteractiveManager().setMining( interactive, event.getBlock().getLocation() );
				}, 1 );
			}
		}
	}
	
	@EventHandler
	private void onBlockBreakEvent( BlockBreakEvent event ) {
		if ( allowBlockBreak ) {
			return;
		}
		
		// Allow creative players to do ground breaking things
		if ( event.getPlayer().getGameMode() == GameMode.CREATIVE ) {
			plugin.getBlockManager().destroy( event.getBlock().getLocation() );
		} else {
			Inventory inventory = Bukkit.createInventory( null, 9 );
			event.getPlayer().openInventory( inventory );
			event.getPlayer().closeInventory();
		
			event.setCancelled( true );
		}
	}
	
	@EventHandler
	private void onAirChangeEvent( EntityAirChangeEvent event ) {
		Entity ent = event.getEntity();
		if ( ent instanceof LivingEntity ) {
			GunsmokeEntityWrapperLivingEntity wrapper = ( GunsmokeEntityWrapperLivingEntity ) plugin.getItemManager().getEntityWrapper( ent );

			GunsmokeAirChangeEvent airEvent = new GunsmokeAirChangeEvent( wrapper, ( ( LivingEntity ) ent ).getRemainingAir() - event.getAmount() );
			airEvent.callEvent();
			
			event.setAmount( ( ( LivingEntity ) ent ).getRemainingAir() - event.getAmount() );
			event.setCancelled( airEvent.isCancelled() );
		}
	}
	
	/*
	 * This should fix the annoying left click bugs, as well as give a reliable left click situation
	 */
	private boolean callInteract( Player player ) {
		int currentTick = plugin.getHandler().getServerTick();
		
		boolean canCall = true;
		
		if ( interactLastCalled.containsKey( player.getUniqueId() ) ) {
			int tick = interactLastCalled.get( player.getUniqueId() );
			canCall = currentTick - tick > 1;
		}
		interactLastCalled.put( player.getUniqueId(), currentTick );
		
		return canCall;
	}

	public boolean isAllowBlockBreak() {
		return allowBlockBreak;
	}

	public void setAllowBlockBreak( boolean allowBlockBreak ) {
		this.allowBlockBreak = allowBlockBreak;
	}
}
