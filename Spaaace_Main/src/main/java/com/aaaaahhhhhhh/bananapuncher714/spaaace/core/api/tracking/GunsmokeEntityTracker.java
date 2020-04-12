package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.tracking;

import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapper;

public interface GunsmokeEntityTracker {
	GunsmokeEntityWrapper getEntity();
	int getTrackingDistance();
	int getChunkRange();
	boolean isDeltaTracking();
	Set< Player > getPlayers();
	
	void track( Player player );
	void trackPlayers( List< Player > players );
	void update( Player player );
	void update();
	void untrack( Player player );
	
	void setVisibilityController( VisibilityController controller );
	VisibilityController getVisiblityController();
}