package com.aaaaahhhhhhh.bananapuncher714.space.core.api;

public enum BooleanOption {
	TRUE, FALSE, UNSET;
	
	public boolean isTrue() {
		return this == TRUE;
	}
	
	public boolean isUnset() {
		return this == UNSET;
	}
}
