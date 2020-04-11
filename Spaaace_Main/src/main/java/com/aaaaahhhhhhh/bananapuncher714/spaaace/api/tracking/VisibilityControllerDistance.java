package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.tracking;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.BooleanOption;

public class VisibilityControllerDistance implements VisibilityController {
	protected double distance;
	protected double distanceSquared;
	
	public VisibilityControllerDistance( double distance ) {
		this.distance = distance;
		distanceSquared = distance * distance;
	}
	
	@Override
	public BooleanOption isVisible( Player player, GunsmokeEntityTracker tracker ) {
		Entity entity = tracker.getEntity().getEntity();
		if ( entity.getWorld() == player.getWorld() && entity.getLocation().distanceSquared( player.getLocation() ) <= distanceSquared ) {
			return BooleanOption.TRUE;
		}
		
		return BooleanOption.FALSE;
	}
}
