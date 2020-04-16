package com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface GunsmokeNPC {
	void interact( NPCAction action );
	void moveTo( Location location );
	void jump();
	void look( float yaw, float pitch );
	void look( Location location );
	
	void remove();
	Player getPlayer();
}
