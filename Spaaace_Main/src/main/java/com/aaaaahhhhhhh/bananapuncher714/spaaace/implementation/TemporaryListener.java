package com.aaaaahhhhhhh.bananapuncher714.spaaace.implementation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.block.GunsmokeBlock;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapperPlayer;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.block.GunsmokeBlockBreakEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.block.GunsmokeBlockCreateEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.block.GunsmokeBlockDamageEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.SpaaaceUtil;

public class TemporaryListener implements Listener {
	@EventHandler
	private void onEvent( GunsmokeBlockCreateEvent event ) {
		GunsmokeBlock block = event.getRepresentable();
		RegeneratingGunsmokeBlock regenBlock = new RegeneratingGunsmokeBlock( block.getLocation(), block.getHealth() );
		event.setBlock( regenBlock );
	}
	
	@EventHandler
	private void onEvent( GunsmokeBlockBreakEvent event ) {
		GunsmokeRepresentable breaker = event.getBreaker();
		if ( breaker instanceof GunsmokeEntityWrapperPlayer ) {
			SpaaaceUtil.getPlugin().setAllowBreakBlock( true );
			SpaaaceUtil.getPlugin().getHandler().breakBlock( ( ( GunsmokeEntityWrapperPlayer ) breaker ).getEntity(), event.getRepresentable().getLocation() );
			SpaaaceUtil.getPlugin().setAllowBreakBlock( false );
		}
	}
}
