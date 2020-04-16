package com.aaaaahhhhhhh.bananapuncher714.space.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.PacketHandler;
import com.aaaaahhhhhhh.bananapuncher714.space.core.tinyprotocol.TinyProtocol;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.ReflectionUtil;
import com.aaaaahhhhhhh.bananapuncher714.space.implementation.Space;
import com.aaaaahhhhhhh.bananapuncher714.space.implementation.world.SpaceGenerator;

import io.netty.channel.Channel;

public class SpaceCore extends JavaPlugin {
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
