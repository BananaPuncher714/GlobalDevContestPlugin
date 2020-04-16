package com.aaaaahhhhhhh.bananapuncher714.spaaace.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.Pair;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapperPlayer;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityChangeVelocityEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.PlayerJumpEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.BukkitUtil;

public class GravityManager {
	private SpaaaceCore core;

	private Map< UUID, Deque< Pair< Location, Integer > > > positions = new HashMap< UUID, Deque< Pair< Location, Integer > > >();
	private Set< UUID > onGround = new HashSet< UUID >();
	private Map< UUID, Vector > lastVelocity = new HashMap< UUID, Vector >();
	private Map< UUID, Vector > lastPlayerVelocity = new HashMap< UUID, Vector >();

	int currentTick = 0;
	
	public GravityManager( SpaaaceCore core ) {
		this.core = core;

		Bukkit.getScheduler().runTaskTimer( core, this::tick, 0, 1 );
		Bukkit.getPluginManager().registerEvents( new Listener() {
			@EventHandler
			private void onEvent( PlayerTeleportEvent event ) {
				positions.remove( event.getPlayer().getUniqueId() );
			}
			
			@EventHandler
			private void onEvent( PlayerChangedWorldEvent event ) {
				positions.remove( event.getPlayer().getUniqueId() );
			}

			@EventHandler
			private void onEvent( PlayerDeathEvent event ) {
				positions.remove( event.getEntity().getUniqueId() );
			}

			@EventHandler
			private void onEvent( PlayerRespawnEvent event ) {
				positions.remove( event.getPlayer().getUniqueId() );
			}

			@EventHandler
			private void onEvent( PlayerQuitEvent event ) {
				positions.remove( event.getPlayer().getUniqueId() );
			}
			
			@EventHandler
			private void onPlayerChangeVelocityEvent( GunsmokeEntityChangeVelocityEvent event ) {
				GunsmokeEntity entity = event.getRepresentable();
				if ( entity instanceof GunsmokeEntityWrapperPlayer ) {
					lastPlayerVelocity.put( entity.getUUID(), event.getNewVector() );
				}
			}
		}, core );
	}

	private void tick() {
		// Minecraft physics are absolute garbage
		for ( Player player : Bukkit.getOnlinePlayers() ) {
			Deque< Pair< Location, Integer > > locs = positions.getOrDefault( player.getUniqueId(), new ArrayDeque< Pair< Location, Integer > >() );
			positions.put( player.getUniqueId(), locs );
			while ( locs.size() > 5 ) locs.pollLast();

			// Get the most recent location that isn't equal to the current player's location
			Location currentLoc = player.getLocation();
			currentLoc.setYaw( 0 );
			currentLoc.setPitch( 0 );
			Pair< Location, Integer > lastLoc = locs.peekFirst();
			if ( lastLoc != null ) {
				while ( !locs.isEmpty() && currentLoc.equals( lastLoc.getFirst() ) ) {
					locs.pollFirst();
					lastLoc = locs.peekFirst();
				}
			}
			locs.addFirst( new Pair< Location, Integer >( currentLoc.clone(), currentTick ) );
			
			int delay = 1;
			Location previousLocation;
			if ( lastLoc == null ) {
				previousLocation = currentLoc.clone();
			} else {
				previousLocation = lastLoc.getFirst();
				delay = currentTick - lastLoc.getSecond();
			}
			
			Vector vector = currentLoc.clone().subtract( previousLocation ).toVector();
			vector.setX( vector.getX() / delay );
			vector.setY( vector.getY() / delay );
			vector.setZ( vector.getZ() / delay );

			if ( !player.isOnGround() && !core.getHandler().isInFluid( player ) ) {
				onGround.remove( player.getUniqueId() );
				Vector prev = lastPlayerVelocity.get( player.getUniqueId() );
				if ( prev != null ) {
					// Only update the X and Z components if the player bumped into a wall or something
					// Otherwise keep the speed the same
					if ( vector.getX() != 0 ) {
						vector.setX( prev.getX() );
					}

					if ( vector.getZ() != 0 ) {
						vector.setZ( prev.getZ() );
					}
				}
			} else if ( !onGround.contains( player.getUniqueId() ) ) {
				onGround.add( player.getUniqueId() );
				// The player just landed on the ground. Stop them from moving faster somehow?
			}
			
			lastPlayerVelocity.put( player.getUniqueId(), vector );
		}
		currentTick++;

		for ( World world : Bukkit.getWorlds() ) {
			for ( Entity entity : world.getEntities() ) {
				GunsmokeEntityWrapper wrapper = core.getItemManager().getEntityWrapper( entity );
				
				// Don't do anything if the entity is riding something
				if ( entity.isInsideVehicle() ) {
					continue;
				}
				
				if ( core.getHandler().isInFluid( entity ) ) {
					continue;
				}
				
				if ( !entity.isOnGround() && wrapper.getGravity() != 1 ) {
					if ( entity instanceof Player ) {
						Player player = ( Player ) entity;
						if ( player.getGameMode() == GameMode.CREATIVE || player.isFlying() ) {
							continue;
						}
					}

					Vector velocity = entity.getVelocity();
					Vector oldVel = lastVelocity.getOrDefault( entity.getUniqueId(), velocity );

					// Get the current Y acceleration
					double oldY = velocity.getY() - oldVel.getY();

					if ( oldY < 0 ) {
						// Subtract half the acceleration if it's negative
						// This allows the entity to jump with their full strength
						velocity.setY( velocity.getY() - ( oldY * ( 1 - wrapper.getGravity() ) ) );
					}

					if ( entity instanceof Player ) {
						// Keep going at the same velocity
						Vector playerVel = lastPlayerVelocity.getOrDefault( entity.getUniqueId(), velocity );
						velocity.setX( playerVel.getX() );
						velocity.setZ( playerVel.getZ() );

						lastPlayerVelocity.put( entity.getUniqueId(), velocity );
					}

					lastVelocity.put( entity.getUniqueId(), velocity );

					entity.setVelocity( velocity );
				} else {
					lastVelocity.remove( entity.getUniqueId() );
				}
			}
		}
	}
}
