package com.aaaaahhhhhhh.bananapuncher714.space.core.api.font;

import java.util.Collection;

public interface BananaFontProvider {
	int getWidthFor( char c );
	boolean contains( char c );
	Collection< Character > getCharacters();
}
