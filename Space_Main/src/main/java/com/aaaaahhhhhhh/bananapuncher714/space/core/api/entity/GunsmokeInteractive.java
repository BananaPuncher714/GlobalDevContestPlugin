package com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity;

import org.bukkit.Location;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Unique;

public interface GunsmokeInteractive extends Unique {
	boolean isHoldingRightClick();
	void setIsHoldingRightClick( boolean holding );
	int getTicksSinceRightClick();
	boolean isMiningBlock();
	void setMining( Location location );
	Location getMiningBlock();
	int getTicksSinceMineStart();
	void stopMining();
}
