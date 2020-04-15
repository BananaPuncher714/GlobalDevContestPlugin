package com.aaaaahhhhhhh.bananapuncher714.spaaace.implementation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.block.GunsmokeBlock;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.block.GunsmokeBlockCreateEvent;

public class TemporaryListener implements Listener {
	@EventHandler
	private void onEvent( GunsmokeBlockCreateEvent event ) {
		GunsmokeBlock block = event.getRepresentable();
		RegeneratingGunsmokeBlock regenBlock = new RegeneratingGunsmokeBlock( block.getLocation(), block.getHealth() );
		event.setBlock( regenBlock );
	}
}
