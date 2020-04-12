package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.SpaaaceCore;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.npc.GunsmokeNPC;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.tracking.GunsmokeEntityTracker;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.util.CollisionResultBlock;

public interface PacketHandler {
	/**
	 * Called before a packet gets sent from server to player.
	 * 
	 * @param player
	 * The player receiving the packet.
	 * @param packet
	 * The packet being sent.
	 * @return
	 * The packet that should be sent, null indicates nothing should be sent.
	 */
	Object onPacketInterceptOut( Player player, Object packet );
	
	/**
	 * Called before a packet gets sent from player to server.
	 * 
	 * @param player
	 * The player sending the packet.
	 * @param packet
	 * The packet being received.
	 * @return
	 * The packet that should be received, null indicates nothing should be received.
	 */
	Object onPacketInterceptIn( Player player, Object packet );
	
	void tick();
	
	List< CollisionResultBlock > rayTrace( Location start, Vector ray );
	List< CollisionResultBlock > rayTrace( Location start, Vector ray, double dist );
	
	int getServerTick();
	void playHurtAnimationFor( LivingEntity entity );
	void setAir( Player player, int ticks );
	void damageBlock( Location location, int stage );
	
	boolean isRealPlayer( Player player );
	GunsmokeNPC getNPC( Player player );
	GunsmokeNPC spawnNPC( World bukkitWorld, String name, String skin );
	
	GunsmokeEntityTracker getEntityTrackerFor( Entity entity );
	
	/**
	 * Register a command with the fallback prefix indicated.
	 * 
	 * @param fallbackPrefix
	 * The prefix to use if another command shares the same value.
	 * @param command
	 * Cannot be null.
	 * @return
	 * Whether the command was registered successfully.
	 */
	boolean registerCommand( String fallbackPrefix, PluginCommand command );
	
	/**
	 * Register a command with the default fallback prefix.
	 * 
	 * @param command
	 * Cannot be null. The fallback prefix will be the name of the plugin of the command.
	 * @return
	 * Whether the command was registered successfully.
	 */
	boolean registerCommand( PluginCommand command );
	
	/**
	 * Unregister a command.
	 * 
	 * @param command
	 * The command to unregister, cannot be null.
	 */
	void unregisterCommand( PluginCommand command );
	
	void setPlugin( SpaaaceCore plugin );
}
