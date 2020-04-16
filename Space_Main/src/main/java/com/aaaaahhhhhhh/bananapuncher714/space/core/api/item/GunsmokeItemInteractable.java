package com.aaaaahhhhhhh.bananapuncher714.space.core.api.item;

import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.EnumEventResult;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.block.GunsmokeBlockBreakEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.block.GunsmokeBlockDamageEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.AdvancementOpenEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.DropItemEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.HoldLeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.HoldRightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.InstabreakBlockEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.LeftClickBlockEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.LeftClickEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.LeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.ReleaseLeftClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.ReleaseRightClickEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.RightClickEntityEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.player.RightClickEvent;

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
	
	public EnumEventResult onClick( LeftClickBlockEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( HoldLeftClickEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( InstabreakBlockEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( ReleaseLeftClickEvent event ) {
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
	
	public EnumEventResult onClick( GunsmokeBlockDamageEvent event ) {
		return EnumEventResult.SKIPPED;
	}
	
	public EnumEventResult onClick( GunsmokeBlockBreakEvent event ) {
		return EnumEventResult.SKIPPED;
	}
}
