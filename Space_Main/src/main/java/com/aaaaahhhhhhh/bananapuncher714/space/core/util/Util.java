package com.aaaaahhhhhhh.bananapuncher714.space.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Util {
	public static < T > T getRandom( Map< T, Integer > objects ) {
		int sum = 0;
		for ( int i : objects.values() ) {
			sum = sum + i;
		}
		List< T > items = new ArrayList< T >( objects.keySet() );
		int randomIndex = -1;
		double random = Math.random() * sum;
		for ( int i = 0; i < items.size(); ++i ) {
		    random -= objects.get( items.get( i ) );
		    if (random <= 0.0d )  {
		        randomIndex = i;
		        break;
		    }
		}
		return items.get( randomIndex );
	}
	
	public static String[] pop( String[] array ) {
		String[] array2 = new String[ Math.max( 0, array.length - 1 ) ];
		for ( int i = 1; i < array.length; i++ ) {
			array2[ i - 1 ] = array[ i ];
		}
		return array2;
	}
	
	public static < T extends JsonElement > T getOrCreate( JsonObject object, String name, T element ) {
		JsonElement jsonElement = object.get( name );
		if ( jsonElement != null && jsonElement.getClass() == element.getClass() ) {
			element = ( T ) object.get( name );
		} else {
			object.remove( name );
			object.add( name, element );
		}
		return element;
	}
}
