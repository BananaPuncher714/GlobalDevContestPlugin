package com.aaaaahhhhhhh.bananapuncher714.space.core;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.DamageType;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.PacketHandler;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperPlayer;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.font.BananaFont;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.font.BananaFontFactory;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.font.BananaFontProvider;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.font.BananaFontProviderBitmap;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.font.BananaFontProviderNegativeSpace;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.font.BananaFontProviderTTF;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.font.MinecraftFontContainer;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.FontBitmap;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.FontIndex;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.FontLegacy;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.FontProvider;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.FontTTF;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.ResourcePack;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack.ResourcePackZip;
import com.aaaaahhhhhhh.bananapuncher714.space.core.tinyprotocol.TinyProtocol;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.FileUtil;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.ReflectionUtil;
import com.aaaaahhhhhhh.bananapuncher714.space.implementation.Space;
import com.aaaaahhhhhhh.bananapuncher714.space.implementation.world.SpaceGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.netty.channel.Channel;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SpaceCore extends JavaPlugin {
	public static final Gson GSON = new Gson();
	
	private TinyProtocol protocol;
	private PacketHandler handler;
	
	private DamageManager entityManager;
	private LocationTracker entityTracker;
	private ItemManager itemManager;
	private BlockManager blockManager;
	private InteractiveManager playerManager;
	private NPCManager npcManager;
	private GravityManager gravityManager;
	
	private PlayerListener listener;
	
	private Space instance;
	
	private ResourcePackZip pack;
	private BananaFont defaultFont;
	
	@Override
	public void onEnable() {
		handler = ReflectionUtil.getNewPacketHandlerInstance();
		if ( handler == null ) {
			getLogger().severe( "This version(" + ReflectionUtil.VERSION + ") is not supported currently!" );
			getLogger().severe( "Disabling..." );
			Bukkit.getPluginManager().disablePlugin( this );
			return;
		}
		handler.setPlugin( this );
		
		protocol = new TinyProtocol( this ) {
			@Override
			public Object onPacketOutAsync( Player player, Channel channel, Object packet ) {
				return handler.onPacketInterceptOut( player, packet );
			}

			@Override
			public Object onPacketInAsync( Player player, Channel channel, Object packet ) {
				return handler.onPacketInterceptIn( player, packet );
			}
		};
		
		// Set up resources
		File resourcepack = new File( getDataFolder() + "/resources.zip" );
		FileUtil.saveToFile( getResource( "data/Space.zip" ), resourcepack, false );
		
		try {
			pack = new ResourcePackZip( resourcepack );
			
			defaultFont = BananaFontFactory.constructFrom( pack );
			
			enhanceFont( defaultFont );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		
		// Set up resource managers
		entityManager = new DamageManager( this );
		Bukkit.getPluginManager().registerEvents( entityManager, this );
		
		entityTracker = new LocationTracker( this );
		itemManager = new ItemManager( this );
		blockManager = new BlockManager( this );
		playerManager = new InteractiveManager( this );
		npcManager = new NPCManager( this );
		gravityManager = new GravityManager( this );
		
		listener = new PlayerListener( this );
		
		Bukkit.getPluginManager().registerEvents( listener, this );
		
		Bukkit.getScheduler().runTaskTimer( this, this::run, 0, 1 );
		
		instance = new Space( this );
	}
	
	private void run() {
		// Tick in order
		playerManager.tick();
		itemManager.tick();
		handler.tick();
		
		for ( Player player : Bukkit.getOnlinePlayers() ) {
			GunsmokeEntityWrapperPlayer wrapper = ( GunsmokeEntityWrapperPlayer ) itemManager.getEntityWrapper( player );
			
			// Simulate drowning
			if ( handler.isInFluid( player ) ) {
				playerManager.addAir( wrapper, -1 );
			} else {
				playerManager.addAir( wrapper, 5 );
			}
			
			if ( wrapper.getAir() == 0 ) {
				entityManager.damage( wrapper, .4, DamageType.VANILLA, DamageCause.DROWNING );
			}
			
			String actionbar = wrapper.getMessage();
			if ( actionbar != null ) {
				player.spigot().sendMessage( ChatMessageType.ACTION_BAR, new TextComponent( actionbar ) );
			}
		}
	}
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator( String worldName, String id ) {
		return new SpaceGenerator( id.hashCode() );
	}
	
	public TinyProtocol getProtocol() {
		return protocol;
	}
	
	public PacketHandler getHandler() {
		return handler;
	}
	
	public BlockManager getBlockManager() {
		return blockManager;
	}
	
	public ItemManager getItemManager() {
		return itemManager;
	}
	
	public DamageManager getDamageManager() {
		return entityManager;
	}
	
	public LocationTracker getLocationTracker() {
		return entityTracker;
	}
	
	public InteractiveManager getInteractiveManager() {
		return playerManager;
	}
	
	public NPCManager getNPCManager() {
		return npcManager;
	}
	
	public GravityManager getGravityManager() {
		return gravityManager;
	}
	
	public void setAllowBreakBlock( boolean set ) {
		listener.setAllowBlockBreak( set );
	}
	
	public boolean isAllowBreakBlock() {
		return listener.isAllowBlockBreak();
	}
	
	public Space getSpaceInstance() {
		return instance;
	}
	
	public void enhanceFont( BananaFont font ) {
		JsonObject obj = GSON.fromJson( new InputStreamReader( getResource( "data/font/default.json" ) ), JsonObject.class );
		FontIndex index = new FontIndex( obj );
		for ( FontProvider provider : index.getProviders() ) {
			if ( provider instanceof FontBitmap ) {
				FontBitmap bitmap = ( FontBitmap ) provider;
				
				InputStream resource = getResource( bitmap.getFile().key );
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
					InputStream resource = getResource( ttf.getFile().key );
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
				InputStream resource = getResource( legacy.getSizes().key );
				BananaFontProvider container = new MinecraftFontContainer( resource );
				font.addProvider( container );
			}
		}
	}
}
