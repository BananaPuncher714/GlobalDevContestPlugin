package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api;

import org.bukkit.event.entity.EntityRegainHealthEvent;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.entity.GunsmokeEntityDamageEvent;

public interface InteractableDamage {
	EnumEventResult onTakeDamage( GunsmokeEntityDamageEvent event );
	default EnumEventResult onEvent( EntityRegainHealthEvent event ) {
		return EnumEventResult.SKIPPED;
	}
}
