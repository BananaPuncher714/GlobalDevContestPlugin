package com.aaaaahhhhhhh.bananapuncher714.space.core.api.font;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

public class BananaFontProviderBitmap implements BananaFontProvider {
	Map< Character, Integer > widths = new HashMap< Character, Integer >();
	
	public BananaFontProviderBitmap( BufferedImage image, String[] characters, int bitmapWidth ) {
		Validate.isTrue( characters.length > 0, "Must include at least 1 character!" );
		int width = image.getWidth() / characters[ 0 ].length();
		int height = image.getHeight() / characters.length;
		// Scan the columns
		for ( int i = 0; i < characters.length; i++ ) {
			int yStart = i * height;
			int yEnd = yStart + height;

			// Scan the rows
			for ( int j = 0; j < characters[ 0 ].length(); j++ ) {
				int charWidth = 0;
				
				// Scan the character widths
				for ( int x = width - 1; x >= 0 && charWidth == 0; x-- ) {
					boolean empty = true;
					// Scan each column of the character
					for ( int y = yStart; y < yEnd && empty; y++ ) {
						int c = image.getRGB( x + j * width, y );
						empty = c >> 24 == 0;
					}
					if ( !empty ) {
						charWidth = x + 1;
					}
				}
				if ( charWidth > 0 ) {
					// How wide it is in relation to the max width of the character
					double percentageWidth = charWidth / width;
					// Get the per pixels that it really appears to be
					double pixels = bitmapWidth * percentageWidth;
					int finWidth = ( int ) Math.ceil( pixels + 1 );
					
					widths.put( characters[ i ].charAt( j ), finWidth );
				} else {
					widths.put( characters[ i ].charAt( j ), 0 );
				}
				
			}
		}
	}

	@Override
	public int getWidthFor( char c ) {
		return widths.getOrDefault( c, 0 );
	}

	@Override
	public boolean contains( char c ) {
		return widths.containsKey( c );
	}

	@Override
	public Collection< Character > getCharacters() {
		return widths.keySet();
	}
}
