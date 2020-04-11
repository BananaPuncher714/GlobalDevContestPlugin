package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.item;

import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.EnumEventResult;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player.AdvancementOpenEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player.DropItemEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player.HoldRightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player.LeftClickEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player.LeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player.ReleaseRightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player.RightClickEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.player.RightClickEvent;

public abstract class GunsmokeItemInteractable extends GunsmokeItem {
	public EnumEventResult onClick( AdvancementOpenEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( DropItemEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( PlayerSwapHandItemsEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( LeftClickEntityEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( LeftClickEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( RightClickEntityEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( RightClickEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( HoldRightClickEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( ReleaseRightClickEvent event ) {
		return EnumEventResult.SKIPPED;
	}
}
