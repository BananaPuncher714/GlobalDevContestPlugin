package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.tracking;

import org.bukkit.entity.Player;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.BooleanOption;

public interface VisibilityController {
	BooleanOption isVisible( Player player, GunsmokeEntityTracker tracker );
}
