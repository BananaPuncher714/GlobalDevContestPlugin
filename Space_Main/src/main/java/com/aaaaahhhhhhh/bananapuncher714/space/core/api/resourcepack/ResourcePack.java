package com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack;

import java.awt.Image;
import java.awt.image.BufferedImage;

import com.aaaaahhhhhhh.bananapuncher714.space.core.util.JetpImageUtil;

public class ResourcePack {
	protected PackDescription packMcmeta = new PackDescription();
	protected BufferedImage packImage = null;
	
	protected SoundIndex sounds = new SoundIndex();
	
	protected FontIndex fonts = new FontIndex();

	public PackDescription getPackMcmeta() {
		return packMcmeta;
	}

	public void setPackMcmeta( PackDescription packMcmeta ) {
		this.packMcmeta = packMcmeta;
	}

	public BufferedImage getPackImage() {
		return packImage;
	}

	public void setPackImage( BufferedImage image ) {
		if ( image.getWidth() != 64 && image.getHeight() != 64 ) {
			image = JetpImageUtil.toBufferedImage( image.getScaledInstance( 64, 64, Image.SCALE_SMOOTH ) );
		}
		this.packImage = image;
	}
}
