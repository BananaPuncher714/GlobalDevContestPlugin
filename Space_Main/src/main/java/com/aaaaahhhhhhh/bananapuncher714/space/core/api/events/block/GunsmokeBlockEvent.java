package com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.block;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.block.GunsmokeBlock;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.GunsmokeEvent;

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
