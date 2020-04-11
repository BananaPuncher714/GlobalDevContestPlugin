package com.aaaaahhhhhhh.bananapuncher714.spaaace;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.entity.GunsmokeEntityDeathEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.entity.GunsmokeEntityDespawnEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player.PlayerUpdateItemEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.util.BukkitUtil;

public class PlayerListener implements Listener {
	public static final long PRONE_DELAY = 500;
	
	private Spaaace plugin;
	
	private Map< UUID, Integer > interactLastCalled = new HashMap< UUID, Integer >();
	
	protected PlayerListener( Spaaace plugin ) {
		this.plugin = plugin;
	}
	
	@EventHandler
	private void onPlayerDeathEvent( PlayerDeathEvent event ) {
		plugin.getPlayerManager().remove( event.getEntity() );
		if ( !plugin.getHandler().isRealPlayer( event.getEntity() ) ) {
			Bukkit.getScheduler().runTaskLater( plugin, () -> {
				plugin.getNPCManager().remove( event.getEntity().getUniqueId() );
			}, 40 );
		} else {
			GunsmokeEntityDeathEvent deathEvent = new GunsmokeEntityDeathEvent( plugin.getItemManager().getEntityWrapper( event.getEntity() ) );
			deathEvent.callEvent();
		}
	}
	
	@EventHandler
	private void onPlayerRespawnEvent( PlayerRespawnEvent event ) {
		plugin.getPlayerManager().remove( event.getPlayer() );
	}
	
	@EventHandler
	private void onPlayerTeleportEvent( PlayerTeleportEvent event ) {
		plugin.getPlayerManager().setHolding( event.getPlayer(), false );
	}
	
	@EventHandler
	private void onPlayerOpenInventoryEvent( InventoryOpenEvent event ) {
		plugin.getPlayerManager().setHolding( ( Player ) event.getPlayer(), false );
	}
	
	@EventHandler
	private void onPlayerSwitchItem( PlayerUpdateItemEvent event ) {
		plugin.getPlayerManager().setHolding( event.getEntity(), false );
	}
	
	@EventHandler
	private void onPlayerQuitEvent( PlayerQuitEvent event ) {
		plugin.getPlayerManager().remove( event.getPlayer() );
		GunsmokeEntityDespawnEvent despawnEvent = new GunsmokeEntityDespawnEvent( plugin.getItemManager().getEntityWrapper( event.getPlayer() ) );
		despawnEvent.callEvent();
	}

	// This is because bukkit has an abyssmal player left click detection system for adventure mode people
	@EventHandler( ignoreCancelled = false )
	private void onPlayerAnimationEvent( PlayerAnimationEvent event ) {
		if ( event.getPlayer().getGameMode() == GameMode.ADVENTURE ) {
			if ( callInteract( event.getPlayer() ) ) {
				plugin.getPlayerManager().leftClick( event.getPlayer(), event );
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
				plugin.getPlayerManager().leftClickEntity( player, event.getEntity(), event );
			} else {
				event.setCancelled( true );
			}
		}
	}

	@EventHandler
	private void onPlayerInteractEntityEvent( PlayerInteractEntityEvent event ) {
		if ( event.getHand() == EquipmentSlot.HAND && !plugin.getPlayerManager().isHolding( event.getPlayer() ) ) {
			plugin.getPlayerManager().rightClickEntity( event.getPlayer(), event.getRightClicked(), event );
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
		if ( action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK ) {
			if ( callInteract( player ) ) {
				plugin.getPlayerManager().leftClick( player, event );
			} else {
				event.setCancelled( true );
			}
		} else if ( action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK ) {
			if ( !BukkitUtil.isRightClickable( player.getEquipment().getItemInMainHand().getType() ) ) {
				plugin.getPlayerManager().rightClick( player, event );
			}
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
}
