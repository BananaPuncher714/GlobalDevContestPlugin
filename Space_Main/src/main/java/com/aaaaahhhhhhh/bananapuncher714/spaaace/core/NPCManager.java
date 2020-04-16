package com.aaaaahhhhhhh.bananapuncher714.spaaace.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.npc.GunsmokeNPC;

public class NPCManager {
	protected SpaceCore plugin;
	
	protected Map< UUID, GunsmokeNPC > npcs = new HashMap< UUID, GunsmokeNPC >();
	
	public NPCManager( SpaceCore plugin ) {
		this.plugin = plugin;
	}
	
	public GunsmokeNPC getNPC( UUID uuid ) {
		return npcs.get( uuid );
	}
	
	public void register( GunsmokeNPC npc ) {
		npcs.put( npc.getPlayer().getUniqueId(), npc );
	}
	
	public void remove( UUID uuid ) {
		GunsmokeNPC npc = npcs.remove( uuid );
		if ( npc != null ) {
			npc.remove();
		}
	}
	
	public Collection< GunsmokeNPC > getNPCs() {
		return npcs.values();
	}
	
	protected void disable() {
		for ( GunsmokeNPC npc : npcs.values() ) {
			npc.remove();
		}
		npcs.clear();
	}
}
