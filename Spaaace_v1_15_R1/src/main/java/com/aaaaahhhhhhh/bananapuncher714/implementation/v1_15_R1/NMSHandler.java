package com.aaaaahhhhhhh.bananapuncher714.implementation.v1_15_R1;

import org.bukkit.entity.Player;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.PacketHandler;

public class NMSHandler implements PacketHandler {
	@Override
	public Object onPacketInterceptOut( Player player, Object packet ) {
		return null;
	}

	@Override
	public Object onPacketInterceptIn( Player player, Object packet ) {
		return packet;
	}
}