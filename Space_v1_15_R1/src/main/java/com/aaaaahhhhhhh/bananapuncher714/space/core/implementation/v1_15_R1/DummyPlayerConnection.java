package com.aaaaahhhhhhh.bananapuncher714.space.core.implementation.v1_15_R1;

import net.minecraft.server.v1_15_R1.EnumProtocolDirection;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PlayerConnection;

public class DummyPlayerConnection extends PlayerConnection {
	CustomNPC npc;
	
	public DummyPlayerConnection( MinecraftServer minecraftserver, CustomNPC entityplayer ) {
		super( minecraftserver, new DummyNetworkManager( EnumProtocolDirection.CLIENTBOUND, entityplayer ), entityplayer );
		
		npc = entityplayer;
	}
	
	@Override
	public void sendPacket( Packet< ? > packet ) {
		npc.onPacket( packet );
	}
}
