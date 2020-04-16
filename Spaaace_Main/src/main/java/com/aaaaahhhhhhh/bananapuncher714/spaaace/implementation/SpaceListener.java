package com.aaaaahhhhhhh.bananapuncher714.spaaace.implementation;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.Gravitable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.block.GunsmokeBlock;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.block.GunsmokeBlockCreateEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityLoadEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.SpaaaceUtil;

public class SpaceListener implements Listener {
	@EventHandler
	private void onEvent( GunsmokeBlockCreateEvent event ) {
		GunsmokeBlock block = event.getRepresentable();
		RegeneratingGunsmokeBlock regenBlock = new RegeneratingGunsmokeBlock( block.getLocation(), block.getHealth() );
		event.setBlock( regenBlock );
	}
	
	@EventHandler
	private void onEvent( EntitySpawnEvent event ) {
		Location location = event.getLocation();
		if ( SpaaaceUtil.isSpaceWorld( location.getWorld() ) ) {
			if ( event.getEntityType() == EntityType.SPIDER ) {
				event.setCancelled( true );
			}
			Rabbit rabbit = location.getWorld().spawn( location, Rabbit.class );
			rabbit.setRabbitType( Type.THE_KILLER_BUNNY );
		}
	}
	
	@EventHandler
	private void onEvent( GunsmokeEntityLoadEvent event ) {
		GunsmokeEntity ent = event.getRepresentable();
		Location location = ent.getLocation();
		if ( SpaaaceUtil.isSpaceWorld( location.getWorld() ) ) {
			if ( ent instanceof Gravitable ) {
				Gravitable gEnt = ( Gravitable ) ent;
				gEnt.setGravity( .3 );
			}
		}
	}
	
	@EventHandler
	private void onEvent( PlayerChangedWorldEvent event ) {
		GunsmokeEntityWrapper wrapper = SpaaaceUtil.getEntity( event.getPlayer() );
		Location location = event.getPlayer().getLocation();
		if ( SpaaaceUtil.isSpaceWorld( location.getWorld() ) ) {
			wrapper.setGravity( .3 );
		} else {
			wrapper.setGravity( 1 );
		}
	}
}
