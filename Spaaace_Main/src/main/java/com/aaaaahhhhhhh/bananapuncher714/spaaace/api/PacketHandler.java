package com.aaaaahhhhhhh.bananapuncher714.spaaace.api;

import org.bukkit.entity.Player;

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
}
