package com.aaaaahhhhhhh.bananapuncher714.space.core.api.font;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class BananaFont {
	private List< BananaFontProvider > providers = new ArrayList< BananaFontProvider >();
	
	public void addProvider( BananaFontProvider provider ) {
		providers.add( provider );
	}
	
	public int getStringWidth( String string, boolean bold ) {
		char[] arr = ChatColor.stripColor( string ).toCharArray();
		int length = 0;
		for ( char c : arr ) {
			for ( BananaFontProvider provider : providers ) {
				if ( provider.contains( c ) ) {
					length += provider.getWidthFor( c );
					break;
				}
			}
		}
		return length + ( bold ? arr.length : 0 );
	}
}
