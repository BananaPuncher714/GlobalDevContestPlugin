package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity;

import org.bukkit.Location;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.Unique;

public interface GunsmokeInteractive extends Unique {
	boolean isHoldingRightClick();
	void setIsHoldingRightClick( boolean holding );
	int getTicksSinceRightClick();
	boolean isMiningBlock();
	void setMining( Location location );
	int getTicksSinceMineStart();
	void stopMining();
}
