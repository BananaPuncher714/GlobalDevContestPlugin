package com.aaaaahhhhhhh.bananapuncher714.space.core;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.DamageType;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.PacketHandler;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperPlayer;
import com.aaaaahhhhhhh.bananapuncher714.space.core.tinyprotocol.TinyProtocol;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.ReflectionUtil;
import com.aaaaahhhhhhh.bananapuncher714.space.implementation.Space;
import com.aaaaahhhhhhh.bananapuncher714.space.implementation.world.SpaceGenerator;
import com.google.gson.Gson;

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
			
			StringBuilder actionBuilder = new StringBuilder();
			double left = wrapper.getLeftPercent();
			if ( left >= 0 ) {
				int amount = Math.max( 0, Math.min( 112, ( int ) ( left * 112 ) ) ) + '\uE4A4';
				actionBuilder.append( ( char ) amount );
			} else {
				actionBuilder.append( ChatColor.BLACK );
				actionBuilder.append( "." );
				actionBuilder.append( StringUtils.repeat( " ", 8 ) );
				actionBuilder.append( ChatColor.RESET );
			}
			
			actionBuilder.append( StringUtils.repeat( " ", 65 ) );
			
			double right = wrapper.getRightPercent();
			if ( right >= 0 ) {
				int amount = Math.max( 0, Math.min( 112, ( int ) ( right * 112 ) ) ) + '\uE4A4';
				actionBuilder.append( ( char ) amount );
			} else {
				actionBuilder.append( ChatColor.BLACK );
				actionBuilder.append( "." );
				actionBuilder.append( StringUtils.repeat( " ", 8 ) );
				actionBuilder.append( ChatColor.RESET );
			}
			
			player.spigot().sendMessage( ChatMessageType.ACTION_BAR, new TextComponent( actionBuilder.toString() ) );
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
}
