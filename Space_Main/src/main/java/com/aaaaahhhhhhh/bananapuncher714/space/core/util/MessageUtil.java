package com.aaaaahhhhhhh.bananapuncher714.space.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.chat.ChatComponent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.chat.ChatMessage;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.font.BananaFont;

public class MessageUtil {
	
	/**
	 * Get the length of a given ChatMessage
	 * 
	 * @param message
	 * @param container
	 * @return
	 */
	public static int lengthOf( ChatMessage message, BananaFont font ) {
		if ( message == null ) {
			return -1;
		}
		int len = 0;
		for ( ChatComponent component : message.getComponents() ) {
			if ( component.getText().contains( "" + ChatColor.COLOR_CHAR ) ) {
				ChatMessage noLegacy = ChatMessage.getMessageFromString( component.getLegacyText() );
				len = len + lengthOf( noLegacy, font );
			} else {
				len = len + font.getStringWidth( component.getLegacyText(), component.isBold() );
			}
		}
		
		return len;
	}
	
	public static List< String > getMatches( String string, String regex ) {
		Pattern pattern = Pattern.compile( regex );
		Matcher matcher = pattern.matcher( string );
		List< String > matches = new ArrayList< String >();
		while ( matcher.find() ) {
			matches.add( matcher.group( 1 ) );
		}
		return matches;
	}
	
	public static String getMatch( String string, String regex ) {
		Pattern pattern = Pattern.compile( regex );
		Matcher matcher = pattern.matcher( string );
		if ( matcher.find() ) {
			return matcher.group( 1 );
		} else {
			return null;
		}
	}
	
	public static String getSpaceFor( int length, BananaFont font ) {
		int spaceLength = font.getStringWidth( " ", false );
		return StringUtils.repeat( " ", length / spaceLength );
	}
	
	public static net.md_5.bungee.api.ChatColor toMDColor( ChatColor color ) {
		return net.md_5.bungee.api.ChatColor.valueOf( color.name() );
	}
	
	public static ChatColor toBukkitColor( net.md_5.bungee.api.ChatColor color ) {
		return  ChatColor.valueOf( color.name() );
	}
}
