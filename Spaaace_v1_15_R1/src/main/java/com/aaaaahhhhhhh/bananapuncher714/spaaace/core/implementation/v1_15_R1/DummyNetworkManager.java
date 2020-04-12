package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.implementation.v1_15_R1;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.server.v1_15_R1.EnumProtocolDirection;
import net.minecraft.server.v1_15_R1.NetworkManager;
import net.minecraft.server.v1_15_R1.Packet;

public class DummyNetworkManager extends NetworkManager {
	protected CustomNPC entity;
	
	public DummyNetworkManager( EnumProtocolDirection enumprotocoldirection, CustomNPC player ) {
		super( enumprotocoldirection );
		this.entity = player;
	}

	@Override
	public boolean isConnected() {
		// For spiritual purposes we'll pretend that this network manager is connected
		return true;
	}
	
	@Override
	public void sendPacket( Packet< ? > packet, GenericFutureListener< ? extends Future< ? super Void > > genericfuturelistener ) {
		entity.onPacket( packet );
	}
}
