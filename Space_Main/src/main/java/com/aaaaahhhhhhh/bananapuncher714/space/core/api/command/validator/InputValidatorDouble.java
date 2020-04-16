package com.aaaaahhhhhhh.bananapuncher714.space.core.api.command.validator;

import java.util.Collection;

public class InputValidatorDouble implements InputValidator< Double > {
	protected double min = Double.MIN_VALUE;
	protected double max = Double.MAX_VALUE;
	
	public InputValidatorDouble() {
	}
	
	public InputValidatorDouble( double min, double max ) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public Collection< String > getTabCompletes() {
		return null;
	}

	@Override
	public boolean isValid( String input, String[] args ) {
		try {
			double i = Double.valueOf( input );
			return i >= min && i <= max;
		} catch ( Exception exception ) {
			return false;
		}
	}

	@Override
	public Double get( String input ) {
		return Double.parseDouble( input );
	}
}
