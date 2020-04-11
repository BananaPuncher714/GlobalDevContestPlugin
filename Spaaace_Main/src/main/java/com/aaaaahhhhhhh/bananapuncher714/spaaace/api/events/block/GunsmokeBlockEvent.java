package com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.block;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.block.GunsmokeBlock;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.events.GunsmokeEvent;

public abstract class GunsmokeBlockEvent extends GunsmokeEvent {
	protected GunsmokeBlock block;
	
	public GunsmokeBlockEvent( GunsmokeBlock block ) {
		super( block );
		this.block = block;
	}
	
	@Override
	public GunsmokeBlock getRepresentable() {
		return block;
	}
}
