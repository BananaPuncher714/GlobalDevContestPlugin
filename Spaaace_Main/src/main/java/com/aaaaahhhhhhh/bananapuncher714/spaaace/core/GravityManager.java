package com.aaaaahhhhhhh.bananapuncher714.spaaace.core;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
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

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapperPlayer;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityChangeVelocityEvent;

public class GravityManager {
	private SpaaaceCore core;

	private Map< UUID, Queue< Location > > positions = new HashMap< UUID, Queue< Location > >();
	private Set< UUID > onGround = new HashSet< UUID >();
	private Map< UUID, Vector > lastVelocity = new HashMap< UUID, Vector >();
	private Map< UUID, Vector > lastPlayerVelocity = new HashMap< UUID, Vector >();

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
		for ( Player player : Bukkit.getOnlinePlayers() ) {
			Queue< Location > locs = positions.getOrDefault( player.getUniqueId(), new ArrayDeque< Location >() );
			locs.add( player.getLocation() );
			positions.put( player.getUniqueId(), locs );
			while ( locs.size() > 5 ) locs.poll();

			Location currentLoc = player.getLocation();
			Location lastLoc = locs.peek();

			Vector vector = currentLoc.clone().subtract( lastLoc ).toVector();
			vector.setX( vector.getX() / 4 );
			vector.setY( vector.getY() / 4 );
			vector.setZ( vector.getZ() / 4 );

			if ( !player.isOnGround() ) {
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

		for ( World world : Bukkit.getWorlds() ) {
			for ( Entity entity : world.getEntities() ) {
				GunsmokeEntityWrapper wrapper = core.getItemManager().getEntityWrapper( entity );
				
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
