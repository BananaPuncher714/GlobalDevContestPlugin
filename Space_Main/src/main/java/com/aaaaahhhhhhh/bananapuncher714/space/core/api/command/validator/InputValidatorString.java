package com.aaaaahhhhhhh.bananapuncher714.space.core.api.command.validator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;

public class InputValidatorString implements InputValidator< String > {
	protected Set< String > values = new HashSet< String >();
	
	public InputValidatorString( String value ) {
		values.add( value.toLowerCase() );
	}

	public InputValidatorString( String... values ) {
		Validate.isTrue( values.length > 0, "Must provide at least 1 argument!" );
		for ( String str : values ) {
			this.values.add( str.toLowerCase() );
		}
	}
	
	@Override
	public Collection< String > getTabCompletes() {
		return values;
	}

	@Override
	public boolean isValid( String input, String[] args ) {
		return values.contains( input.toLowerCase() );
	}

	@Override
	public String get( String input ) {
		return input;
	}
}
