package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.implementation.v1_15_R1;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiFunction;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.SpaaaceCore;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.PacketHandler;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.block.VanillaMaterialData;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.GunsmokeInteractive;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.npc.GunsmokeNPC;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.AdvancementOpenEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.DropItemEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.events.player.PlayerJumpEvent;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.sound.SoundSet;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.sound.SoundWave;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.tracking.GunsmokeEntityTracker;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.util.CollisionResultBlock;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.BukkitUtil;
import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_15_R1.AttributeInstance;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcher.Item;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.Fluid;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.MovingObjectPositionBlock;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayInAdvancements;
import net.minecraft.server.v1_15_R1.PacketPlayInAdvancements.Status;
import net.minecraft.server.v1_15_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_15_R1.PacketPlayInBlockDig.EnumPlayerDigType;
import net.minecraft.server.v1_15_R1.PacketPlayInBlockPlace;
import net.minecraft.server.v1_15_R1.PacketPlayInFlying;
import net.minecraft.server.v1_15_R1.PacketPlayOutAbilities;
import net.minecraft.server.v1_15_R1.PacketPlayOutBlockBreak;
import net.minecraft.server.v1_15_R1.PacketPlayOutBlockBreakAnimation;
import net.minecraft.server.v1_15_R1.PacketPlayOutBlockChange;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_15_R1.PacketPlayOutMapChunk;
import net.minecraft.server.v1_15_R1.PacketPlayOutUpdateAttributes;
import net.minecraft.server.v1_15_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_15_R1.PlayerChunkMap.EntityTracker;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.RayTrace;
import net.minecraft.server.v1_15_R1.SoundEffect;
import net.minecraft.server.v1_15_R1.SoundEffectType;
import net.minecraft.server.v1_15_R1.TagsFluid;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.VoxelShape;
import net.minecraft.server.v1_15_R1.WorldServer;

public class NMSHandler implements PacketHandler {
	private static Field ENTITYMETADATA_ITEMLIST;
	private static Field ENTITYMETADATA_ID;

	private static Field ENTITYEQUIPMENT_ID;
	private static Field ENTITYEQUIPMENT_SLOT;
	private static Field ENTITYEQUIPMENT_ITEMSTACK;
	
	private static Field BLOCKBREAK_ID;
	private static Field BLOCKBREAK_STATUS;
	private static Field BLOCKBREAK_SUCCESS;

	private static Field BLOCKCHANGE_POSITION;

	private static Field MAPCHUNK_X;
	private static Field MAPCHUNK_Z;

	private static Field PLAYERABILITIES_FOV;

	private static Field TELEPORT_AWAIT;
	
	private static Field[] WORLDBORDERPACKET_FIELDS;
	
	private static Field ENTITYTRACKER_ENTITY;
	
	private static Method VOXEL_SHAPE_CONTAINS;
	
	private static Field SIMPLECOMMANDMAP_COMMANDS;
	private static Method CRAFTSERVER_SYNCCOMMANDS;
	
	private static Field INTERACT_BLOCKPOS;
	private static Field INTERACT_ISDESTROYING;
	
	private static Field SOUNDEFFECT_KEY;
	
	private static Field SOUNDTYPE_BREAK;
	private static Field SOUNDTYPE_HIT;
	
	static {
		try {
			ENTITYMETADATA_ITEMLIST = PacketPlayOutEntityMetadata.class.getDeclaredField("b");
			ENTITYMETADATA_ITEMLIST.setAccessible(true);

			ENTITYMETADATA_ID = PacketPlayOutEntityMetadata.class.getDeclaredField("a");
			ENTITYMETADATA_ID.setAccessible(true);

			ENTITYEQUIPMENT_ID = PacketPlayOutEntityEquipment.class.getDeclaredField("a");
			ENTITYEQUIPMENT_ID.setAccessible(true);

			ENTITYEQUIPMENT_SLOT = PacketPlayOutEntityEquipment.class.getDeclaredField("b");
			ENTITYEQUIPMENT_SLOT.setAccessible(true);

			ENTITYEQUIPMENT_ITEMSTACK = PacketPlayOutEntityEquipment.class.getDeclaredField("c");
			ENTITYEQUIPMENT_ITEMSTACK.setAccessible(true);

			BLOCKBREAK_ID = PacketPlayOutBlockBreak.class.getDeclaredField( "d" );
			BLOCKBREAK_ID.setAccessible( true );
			BLOCKBREAK_STATUS = PacketPlayOutBlockBreak.class.getDeclaredField( "a" );
			BLOCKBREAK_STATUS.setAccessible( true );
			BLOCKBREAK_SUCCESS = PacketPlayOutBlockBreak.class.getDeclaredField( "e" );
			BLOCKBREAK_SUCCESS.setAccessible( true );
			
			BLOCKCHANGE_POSITION = PacketPlayOutBlockChange.class.getDeclaredField("a");
			BLOCKCHANGE_POSITION.setAccessible(true);

			MAPCHUNK_X = PacketPlayOutMapChunk.class.getDeclaredField("a");
			MAPCHUNK_X.setAccessible(true);

			MAPCHUNK_Z = PacketPlayOutMapChunk.class.getDeclaredField("b");
			MAPCHUNK_Z.setAccessible(true);

			PLAYERABILITIES_FOV = PacketPlayOutAbilities.class.getDeclaredField("f");
			PLAYERABILITIES_FOV.setAccessible(true);
			
			TELEPORT_AWAIT = PlayerConnection.class.getDeclaredField( "teleportAwait" );
			TELEPORT_AWAIT.setAccessible( true );
			
			VOXEL_SHAPE_CONTAINS = VoxelShape.class.getDeclaredMethod( "b", double.class, double.class, double.class );
			VOXEL_SHAPE_CONTAINS.setAccessible( true );
			
			ENTITYTRACKER_ENTITY = EntityTracker.class.getDeclaredField( "tracker" );
			ENTITYTRACKER_ENTITY.setAccessible( true );
			
			WORLDBORDERPACKET_FIELDS = new Field[ 9 ];
			WORLDBORDERPACKET_FIELDS[ 0 ] = PacketPlayOutWorldBorder.class.getDeclaredField( "a" );
			WORLDBORDERPACKET_FIELDS[ 1 ] = PacketPlayOutWorldBorder.class.getDeclaredField( "b" );
			WORLDBORDERPACKET_FIELDS[ 2 ] = PacketPlayOutWorldBorder.class.getDeclaredField( "c" );
			WORLDBORDERPACKET_FIELDS[ 3 ] = PacketPlayOutWorldBorder.class.getDeclaredField( "d" );
			WORLDBORDERPACKET_FIELDS[ 4 ] = PacketPlayOutWorldBorder.class.getDeclaredField( "e" );
			WORLDBORDERPACKET_FIELDS[ 5 ] = PacketPlayOutWorldBorder.class.getDeclaredField( "f" );
			WORLDBORDERPACKET_FIELDS[ 6 ] = PacketPlayOutWorldBorder.class.getDeclaredField( "g" );
			WORLDBORDERPACKET_FIELDS[ 7 ] = PacketPlayOutWorldBorder.class.getDeclaredField( "h" );
			WORLDBORDERPACKET_FIELDS[ 8 ] = PacketPlayOutWorldBorder.class.getDeclaredField( "i" );
			for ( Field field : WORLDBORDERPACKET_FIELDS ) {
				field.setAccessible( true );
			}
			
			SIMPLECOMMANDMAP_COMMANDS = SimpleCommandMap.class.getDeclaredField( "knownCommands" );
			SIMPLECOMMANDMAP_COMMANDS.setAccessible( true );
			
			CRAFTSERVER_SYNCCOMMANDS = CraftServer.class.getDeclaredMethod( "syncCommands" );
			CRAFTSERVER_SYNCCOMMANDS.setAccessible( true );
			
			INTERACT_BLOCKPOS = PlayerInteractManager.class.getDeclaredField( "g" );
			INTERACT_BLOCKPOS.setAccessible( true );
			INTERACT_ISDESTROYING = PlayerInteractManager.class.getDeclaredField( "e" );
			INTERACT_ISDESTROYING.setAccessible( true );
			
			SOUNDEFFECT_KEY = SoundEffect.class.getDeclaredField( "a" );
			SOUNDEFFECT_KEY.setAccessible( true );
			
			SOUNDTYPE_BREAK = SoundEffectType.class.getDeclaredField( "z" );
			SOUNDTYPE_BREAK.setAccessible( true );
			SOUNDTYPE_HIT = SoundEffectType.class.getDeclaredField( "C" );
			SOUNDTYPE_HIT.setAccessible( true );
		} catch ( NoSuchFieldException | SecurityException | IllegalArgumentException | NoSuchMethodException e ) {
			e.printStackTrace();
		}
	}
	
	private SpaaaceCore plugin;
	private NMSTracker entityTracker = new NMSTracker();
	
	@Override
	public Object onPacketInterceptOut( Player player, Object packet ) {
		return packet;
	}

	@Override
	public Object onPacketInterceptIn( Player reciever, Object packet ) {
		if ( packet instanceof PacketPlayInBlockPlace ) {
			return handleBlockPlacePacket( reciever, ( PacketPlayInBlockPlace ) packet );
		} else if ( packet instanceof PacketPlayInBlockDig ) {
			return handleBlockDigPacket( reciever, ( PacketPlayInBlockDig ) packet );
		} else if ( packet instanceof PacketPlayInAdvancements ) {
			return handleAdvancementPacket( reciever, ( PacketPlayInAdvancements ) packet );
		} else if ( packet instanceof PacketPlayInFlying ) {
			return handleFlyingPacket( reciever, ( PacketPlayInFlying ) packet );
		}
		return packet;
	}
	
	private Packet< ? > handleAdvancementPacket( Player player, PacketPlayInAdvancements packet ) {
		if ( packet.c() == Status.OPENED_TAB ) {
			GunsmokeEntityWrapper entity = plugin.getItemManager().getEntityWrapper( player );
			if ( entity instanceof GunsmokeInteractive ) {
				new AdvancementOpenEvent( ( GunsmokeInteractive ) entity, packet.d().getKey() ).callEvent();
			}
		}
		return packet;
	}

	private Packet< ? > handleBlockPlacePacket( Player player, PacketPlayInBlockPlace packet ) {
		if ( BukkitUtil.isRightClickable( player.getEquipment().getItemInMainHand().getType() ) ) {
			GunsmokeEntityWrapper entity = plugin.getItemManager().getEntityWrapper( player );
			if ( entity instanceof GunsmokeInteractive ) {
				plugin.getInteractiveManager().setHolding( ( GunsmokeInteractive ) entity, true );
			}
		}
		return packet;
	}
	
	private Packet< ? > handleBlockDigPacket( Player player, PacketPlayInBlockDig packet ) {
		GunsmokeEntityWrapper entity = plugin.getItemManager().getEntityWrapper( player );
		
		EnumPlayerDigType action = packet.d();
		if ( action == EnumPlayerDigType.RELEASE_USE_ITEM ) {
			if ( entity instanceof GunsmokeInteractive ) {
				plugin.getInteractiveManager().setHolding( ( GunsmokeInteractive ) entity, false );
			}
		} else if ( action == EnumPlayerDigType.DROP_ITEM ) {
			if ( entity instanceof GunsmokeInteractive ) {
				DropItemEvent event = new DropItemEvent( ( GunsmokeInteractive ) entity );

				CountDownLatch latch = new CountDownLatch( 1 );
				Bukkit.getScheduler().scheduleSyncDelayedTask( plugin, () -> {
					Bukkit.getPluginManager().callEvent( event );
					latch.countDown();
				} );

				try {
					latch.await();
				} catch ( InterruptedException e ) {
					e.printStackTrace();
				}

				if ( event.isCancelled() ) {
					player.getEquipment().setItemInMainHand( player.getEquipment().getItemInMainHand() );
					return null;
				}
			}
		} else if ( action == EnumPlayerDigType.ABORT_DESTROY_BLOCK || action == EnumPlayerDigType.STOP_DESTROY_BLOCK ) {
			if ( entity instanceof GunsmokeInteractive ) {
				plugin.getInteractiveManager().setMining( ( GunsmokeInteractive ) entity, null );
			}
		}
		return packet;
	}
	
	private Packet< ? > handleFlyingPacket( Player player, PacketPlayInFlying packet ) {
		if ( player.isOnGround() && !packet.b() && packet.b( player.getLocation().getY() ) - player.getLocation().getY() > 0 ) {
			GunsmokeEntityWrapper entity = plugin.getItemManager().getEntityWrapper( player );
			if ( entity instanceof GunsmokeInteractive ) {
				new PlayerJumpEvent( ( GunsmokeInteractive ) entity ).callEvent();
			}
		}
		return packet;
	}
	
	@Override
	public void tick() {
		entityTracker.tick();
	}

	@Override
	public List< CollisionResultBlock > rayTrace( Location start, Vector vector, double distance ) {
		return rayTrace( start, vector.clone().normalize().multiply( distance ) );
	}
	
	@Override
	public List< CollisionResultBlock > rayTrace( Location start, Vector vector ) {
		net.minecraft.server.v1_15_R1.World world = ( ( CraftWorld ) start.getWorld() ).getHandle();
		List< CollisionResultBlock > collisions = new ArrayList< CollisionResultBlock >();
		for ( MovingObjectPositionBlock result : rayTrace( start, vector, new BiFunction< RayTrace, BlockPosition, MovingObjectPositionBlock >() {
			@Override
			public MovingObjectPositionBlock apply( RayTrace ray, BlockPosition blockPosition ) {
				IBlockData hitBlock = world.getType( blockPosition );
	            Fluid hitFluid = world.getFluid( blockPosition);
	            Vec3D getStart = ray.b();
	            Vec3D getEnd = ray.a();
	            VoxelShape blockShape = ray.a( hitBlock, world, blockPosition );
	            MovingObjectPositionBlock blockResult = world.rayTrace( getStart, getEnd, blockPosition, blockShape, hitBlock );
	            VoxelShape fluidShape = ray.a( hitFluid, world, blockPosition );
	            MovingObjectPositionBlock fluidResult = fluidShape.rayTrace( getStart, getEnd, blockPosition);
	            // Get whichever one is shorter and return
	            double blockDistance = blockResult == null ? Double.MAX_VALUE : ray.b().distanceSquared( blockResult.getPos() );
	            double fluidDistance = fluidResult == null ? Double.MAX_VALUE : ray.b().distanceSquared( fluidResult.getPos() );
	            return blockDistance <= fluidDistance ? blockResult : fluidResult;
			}
		} ) ) {
			collisions.add( getResultFrom( world, result ) );
		}
		return collisions;
	}
	
	private List< MovingObjectPositionBlock > rayTrace( Location origin, Vector vector, BiFunction< RayTrace, BlockPosition, MovingObjectPositionBlock > biFunction ) {
		Location dest = origin.clone().add( vector );
		Vec3D start = new Vec3D( origin.getX(), origin.getY(), origin.getZ() );
		Vec3D end = new Vec3D( dest.getX(), dest.getY(), dest.getZ() );
		RayTrace trace = new RayTrace( start, end, RayTrace.BlockCollisionOption.OUTLINE, RayTrace.FluidCollisionOption.NONE, null );
		
		BlockIterator iterator = new BlockIterator( origin.getWorld(), origin.toVector(), vector, 0, ( int ) ( 1 + vector.length() ) );

	    List< MovingObjectPositionBlock > results = new ArrayList< MovingObjectPositionBlock >();
	    
	    while ( iterator.hasNext() ) {
	    	Block block = iterator.next();
	    	Location bLoc = block.getLocation();
	    	MovingObjectPositionBlock result = biFunction.apply( trace, new BlockPosition( bLoc.getBlockX(), bLoc.getBlockY(), bLoc.getBlockZ() ) );
	    	if ( result != null ) {
	    		results.add( result );
	    	}
	    }
	    
	    return results;
	}
	
	private CollisionResultBlock getResultFrom( net.minecraft.server.v1_15_R1.World world, MovingObjectPositionBlock position ) {
		Block block = CraftBlock.at( world, position.getBlockPosition() );
		BlockFace face = CraftBlock.notchToBlockFace( position.getDirection() );
		Vec3D fin = position.getPos();
		Location interception = new Location( world.getWorld(), fin.getX(), fin.getY(), fin.getZ() );
		return new CollisionResultBlock( interception, face, block );
	}
	
	@Override
	public int getServerTick() {
		return MinecraftServer.currentTick;
	}
	
	@Override
	public void playHurtAnimationFor( LivingEntity entity ) {
		if ( entity instanceof Player ) {
			List< AttributeInstance > attributes = new ArrayList< AttributeInstance >();
			attributes.add( ( ( CraftLivingEntity ) entity ).getHandle().getAttributeInstance( GenericAttributes.MAX_HEALTH ) );
			PacketPlayOutUpdateAttributes attributePacket = new PacketPlayOutUpdateAttributes( entity.getEntityId(), attributes );

			sendPacket( ( Player ) entity , attributePacket );
		}
		
		PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus( ( ( CraftEntity ) entity ).getHandle(), ( byte ) 2 );
		broadcastPacket( entity, packet, true );
	}
	
	@Override
	public void setAir( Player player, int air ) {
		int id = player.getEntityId();
		DataWatcher watcher = ( ( CraftEntity ) player ).getHandle().getDataWatcher();
		PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata( id, watcher, false );

		List< Item< ? > > items = null;
		try {
			items = ( List< Item< ? > > ) ENTITYMETADATA_ITEMLIST.get( packet );
			if ( items == null ) {
				items = new ArrayList< Item< ? > >();
				ENTITYMETADATA_ITEMLIST.set( packet, items );
			}
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			e.printStackTrace();
		}
		items.clear();
		items.add( new Item< Integer >( new DataWatcherObject< Integer >( 1, DataWatcherRegistry.b ), air ) );
		
		sendPacket( player, packet );
	}
	
	@Override
	public void damageBlock( Location location, int stage ) {
		location.setYaw( 0 );
		location.setPitch( 0 );
		location = BukkitUtil.getBlockLocation( location );
		PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation( location.hashCode(), new BlockPosition( location.getBlockX(), location.getBlockY(), location.getBlockZ() ), stage );
		
		broadcastPacket( location.getWorld(), packet );
	}
	
	@Override
	public VanillaMaterialData getVanillaMaterialDataFor( Material material ) {
		VanillaMaterialData vData = new VanillaMaterialData( material );
		IBlockData data = CraftMagicNumbers.getBlock( new MaterialData( material ) );
		
		vData.setStrength( data.f( null, null ) );
		SoundEffectType sound = data.r();
		
		float volume = sound.a();
		float pitch = sound.b() - .2f;
		SoundSet soundSet = new SoundSet();
		try {
			Sound breakSound = getSoundOf( ( SoundEffect ) SOUNDTYPE_BREAK.get( sound ) );
			soundSet.setSound( "BREAK", new SoundWave( breakSound, volume, pitch ) );
			
			Sound stepSound = getSoundOf( sound.d() );
			soundSet.setSound( "STEP", new SoundWave( stepSound, volume, pitch ) );
			
			Sound placeSound = getSoundOf( sound.e() );
			soundSet.setSound( "PLACE", new SoundWave( placeSound, volume, pitch ) );
			
			Sound hitSound = getSoundOf( ( SoundEffect ) SOUNDTYPE_HIT.get( sound ) );
			soundSet.setSound( "HIT", new SoundWave( hitSound, volume, pitch ) );
			
			Sound fallSound = getSoundOf( sound.g() );
			soundSet.setSound( "FALL", new SoundWave( fallSound, volume, pitch ) );
        } catch ( IllegalAccessException ex ) {
            ex.printStackTrace();
        }
		vData.setSounds( soundSet );
		
		return vData;
	}
	
	private Sound getSoundOf( SoundEffect effect ) {
		try {
			MinecraftKey key = ( MinecraftKey ) SOUNDEFFECT_KEY.get( effect );
			return Sound.valueOf( key.getKey().replace( '.', '_' ).toUpperCase() );
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public float getVanillaDestroySpeed( ItemStack item, Material material ) {
		float speed = 1;
		if ( item != null ) {
			IBlockData data = CraftMagicNumbers.getBlock( new MaterialData( material ) );
			net.minecraft.server.v1_15_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy( item );
			speed = nmsItem.a( data );
		}
		return speed;
	}
	
	@Override
	public GunsmokeEntityTracker getEntityTrackerFor( org.bukkit.entity.Entity bukkitEntity ) {
		return entityTracker.getEntityTrackerFor( bukkitEntity );
	}
	
	@Override
	public boolean isMiningBlock( Player player ) {
		PlayerInteractManager manager = ( ( CraftPlayer ) player ).getHandle().playerInteractManager;
		try {
			boolean isBreaking = ( boolean ) INTERACT_ISDESTROYING.get( manager );
			return isBreaking;
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean isInFluid( Entity entity ) {
		net.minecraft.server.v1_15_R1.Entity nmsEntity = ( ( CraftEntity ) entity ).getHandle();
		return nmsEntity.a( TagsFluid.WATER ) || nmsEntity.a( TagsFluid.LAVA );
	}
	
	@Override
	public void breakBlock( Player player, Location location ) {
		PlayerInteractManager manager = ( ( CraftPlayer ) player ).getHandle().playerInteractManager;
		manager.breakBlock( new BlockPosition( location.getBlockX(), location.getBlockY(), location.getBlockZ() ) );
	}
	
	@Override
	public boolean isRealPlayer( Player player ) {
		return ( ( CraftPlayer ) player ).getHandle().getClass().equals( EntityPlayer.class );
	}
	
	@Override
	public GunsmokeNPC getNPC( Player player ) {
		EntityPlayer ep = ( ( CraftPlayer ) player ).getHandle();
		if ( ep instanceof GunsmokeNPC ) {
			return ( GunsmokeNPC ) ep;
		}
		return null;
	}
	
	@Override
	public GunsmokeNPC spawnNPC( World bukkitWorld, String name, String skin ) {
		WorldServer world = ( ( CraftWorld ) bukkitWorld ).getHandle();
		
		GameProfile profile = NMSUtils.convert( new GameProfile( UUID.randomUUID(), name ), skin );
		
		CustomNPC npc = new CustomNPC( world, profile );
		
		world.addEntity( npc );
		
		return npc;
	}
	
	@Override
	public boolean registerCommand( PluginCommand command ) {
		Validate.notNull( command );
		return registerCommand( command.getPlugin().getName(), command );
	}
	
	@Override
	public boolean registerCommand( String fallbackPrefix, PluginCommand command ) {
		Validate.notNull( fallbackPrefix );
		Validate.notNull( command );
		boolean registered = ( ( CraftServer ) Bukkit.getServer() ).getCommandMap().register( fallbackPrefix, command );
		
		try {
			// Pretty dumb, but apparently you need to re-sync the commands after you do your business or else it won't tab complete properly for players
			CRAFTSERVER_SYNCCOMMANDS.invoke( Bukkit.getServer() );
		} catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
			e.printStackTrace();
		}
		
		return registered;
	}

	@Override
	public void unregisterCommand( PluginCommand command ) {
		Validate.notNull( command );
		try {
			SimpleCommandMap map = ( ( CraftServer ) Bukkit.getServer() ).getCommandMap();
			Map< String, Command > commands = ( Map< String, Command > ) SIMPLECOMMANDMAP_COMMANDS.get( map );
			for ( Iterator< Entry< String, Command > > iterator = commands.entrySet().iterator(); iterator.hasNext(); ) {
				Entry< String, Command > entry = iterator.next();
				if ( entry.getValue() == command ) {
					iterator.remove();
				}
			}
			
			try {
				CRAFTSERVER_SYNCCOMMANDS.invoke( Bukkit.getServer() );
			} catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
				e.printStackTrace();
			}
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setPlugin( SpaaaceCore plugin ) {
		this.plugin = plugin;
	}

	protected void sendPacket( Player player, Packet< ? > packet ) {
		( ( CraftPlayer ) player ).getHandle().playerConnection.sendPacket( packet );
	}
	
	protected void broadcastPacket( World world, Packet< ? > packet ) {
		for ( Player player : world.getPlayers() ) {
			sendPacket( player, packet );
		}
	}
	
	protected void broadcastPacket( org.bukkit.entity.Entity origin, Packet< ? > packet, boolean updateSelf ) {
		CustomEntityTracker tracker = entityTracker.getEntityTrackerFor( origin );
		if ( updateSelf ) {
			tracker.broadcastIncludingSelf( packet );
		} else {
			tracker.broadcast( packet );
		}
	}
}