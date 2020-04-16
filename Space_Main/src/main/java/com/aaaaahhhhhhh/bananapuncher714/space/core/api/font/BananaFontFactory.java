package com.aaaaahhhhhhh.bananapuncher714.space.core.api.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.FontBitmap;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.FontIndex;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.FontLegacy;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.FontProvider;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.FontTTF;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.ResourcePackZip;

public class BananaFontFactory {
	public static BananaFont constructFrom( ResourcePackZip resourcePack ) throws IOException {
		FontIndex index = resourcePack.getIndex();
		BananaFont font = new BananaFont();
		for ( FontProvider provider : index.getProviders() ) {
			if ( provider instanceof FontBitmap ) {
				FontBitmap bitmap = ( FontBitmap ) provider;
				
				InputStream resource = resourcePack.getTexture( bitmap.getFile() );
				try {
					BufferedImage image = ImageIO.read( resource );
					BananaFontProvider bitmapProvider = new BananaFontProviderBitmap( image, bitmap.getChars(), bitmap.getHeight() );

					font.addProvider( bitmapProvider );
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			} else if ( provider instanceof FontTTF ) {
				FontTTF ttf = ( FontTTF ) provider;
				
				if ( ttf.getFile().key.contains( "negative_spaces" ) ) {
					font.addProvider( BananaFontProviderNegativeSpace.getProvider() );
				} else {
					InputStream resource = resourcePack.getResource( ttf.getFile() );
					try {
						Font jFont = Font.createFont( Font.TRUETYPE_FONT, resource );
						BananaFontProvider bananaProvider = new BananaFontProviderTTF( jFont );
						font.addProvider( bananaProvider );
					} catch ( FontFormatException | IOException e ) {
						e.printStackTrace();
					}
				}
			} else if ( provider instanceof FontLegacy ) {
				FontLegacy legacy = ( FontLegacy ) provider;
				InputStream resource = resourcePack.getResource( legacy.getSizes() );
				BananaFontProvider container = new MinecraftFontContainer( resource );
				font.addProvider( container );
			}
		}
		return font;
	}
}
