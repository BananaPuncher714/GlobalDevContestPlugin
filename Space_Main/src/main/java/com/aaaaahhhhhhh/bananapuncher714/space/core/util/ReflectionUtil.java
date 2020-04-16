package com.aaaaahhhhhhh.bananapuncher714.space.core.util;

import org.bukkit.Bukkit;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.PacketHandler;

/**
 * Internal use only
 * 
 * @author BananaPuncher714
 */
public final class ReflectionUtil {
	public static final String VERSION;
	
	static {
		VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}

	private ReflectionUtil() {
	}
	
	public static PacketHandler getNewPacketHandlerInstance() {
		try {
			Class< ? > clazz = Class.forName( "com.aaaaahhhhhhh.bananapuncher714.space.core.implementation." + VERSION + ".NMSHandler" );
			return ( PacketHandler ) clazz.newInstance();
		} catch ( ClassNotFoundException | InstantiationException | IllegalAccessException e ) {
			e.printStackTrace();
			return null;
		}
	}
}
