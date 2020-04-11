package com.aaaaahhhhhhh.bananapuncher714.spaaace;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.PacketHandler;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.tinyprotocol.TinyProtocol;

import io.netty.channel.Channel;

public class Spaaace extends JavaPlugin {
	private TinyProtocol protocol;
	private PacketHandler handler;
	
	private EntityManager entityManager;
	private EntityLocationTracker entityTracker;
	private ItemManager itemManager;
	private BlockManager blockManager;
	private PlayerManager playerManager;
	private NPCManager npcManager;
	
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
		
		entityManager = new EntityManager( this );
		Bukkit.getPluginManager().registerEvents( entityManager, this );
		
		entityTracker = new EntityLocationTracker( this );
		itemManager = new ItemManager( this );
		blockManager = new BlockManager( this );
		playerManager = new PlayerManager( this );
		npcManager = new NPCManager( this );
		
		Bukkit.getPluginManager().registerEvents( new PlayerListener( this ), this );
		
		Bukkit.getScheduler().runTaskTimer( this, this::run, 0, 1 );
	}
	
	private void run() {
		// Tick in order
		playerManager.tick();
		itemManager.tick();
		handler.tick();
		
		for ( Player player : Bukkit.getOnlinePlayers() ) {
//			GunsmokePlayer entity = entityManager.getEntity( player.getUniqueId() );
//			entity.update();

			GunsmokeEntity playerEntity = itemManager.getEntityWrapper( player );
//			handler.setTint( player, 1 - playerEntity.getHealth() / playerEntity.getMaxHealth() );
			
//			NMSUtils.setNoFly( player );
			
//			protocol.getHandler().display( player );
			
//			AABB[] boxes = protocol.getHandler().getBoxesFor( player.getLocation() );
//			if ( VectorUtil.intersects( new Vector( 0, 0, 0 ), new AABB( player.getBoundingBox().expand(-9.999999747378752E-6D) ), BukkitUtil.getBlockLocation( player.getLocation() ).toVector(), boxes ) ) {
//				player.sendMessage( "IN BLOCK" );
//			}
//			
//			for ( Entity entity : player.getWorld().getNearbyEntities( player.getLocation(), 20, 20, 20 ) ) {
//				Location location = entityTracker.getLocationOf( entity.getUniqueId(), 6 );
//				if ( location != null ) {
//					entity.getWorld().spawnParticle( Particle.WATER_BUBBLE, location.clone().add( 0, entity.getHeight() + .5, 0 ), 0 );
//				}
//			}
//			
//			for ( GunsmokeNPC npc : GunsmokeUtil.getPlugin().getNPCManager().getNPCs() ) {
//				if ( npc.getPlayer() == player ) {
//					continue;
//				}
//				npc.look( player.getEyeLocation() );
//			}
			
//			player.setRemainingAir( 285 );
	 		handler.setAir( player, 285 );
		}
	}
	
	@Override
	public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
		if ( args.length == 1 ) {
			float movementSpeed = Float.valueOf( args[ 0 ] );
			sender.sendMessage( "Set speed to " + movementSpeed );
			
			for ( Player player : Bukkit.getOnlinePlayers() ) {
				player.setFlySpeed( movementSpeed );
				player.setWalkSpeed( movementSpeed );
			}
		}
		
		return false;
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
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public EntityLocationTracker getEntityTracker() {
		return entityTracker;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	public NPCManager getNPCManager() {
		return npcManager;
	}
}
