package com.aaaaahhhhhhh.bananapuncher714.space.implementation;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import com.aaaaahhhhhhh.bananapuncher714.space.core.SpaceCore;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Pair;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.command.SubCommand;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.command.executor.CommandExecutableMessage;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.command.validator.InputValidatorDouble;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.command.validator.sender.SenderValidatorPlayer;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperLivingEntity;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.BukkitUtil;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.SpaceUtil;
import com.aaaaahhhhhhh.bananapuncher714.space.implementation.world.SpaceGenerator;

public class Space {
	private SpaceCore core;
	private int currentTick = 0;
	
	public Space( SpaceCore core ) {
		this.core = core;

		PluginCommand spawnCommand = BukkitUtil.constructCommand( "spawnobserver" );
		new SubCommand( "spawnobserver " )
		.defaultTo( ( sender, args, params ) -> {
			if ( sender instanceof Player ) {
				Player player = ( Player ) sender;

				PowerHoe test = new PowerHoe();
				core.getItemManager().register( test );
				player.getInventory().addItem( test.getItem() );

				LeadBoots leadBoots = new LeadBoots();
				core.getItemManager().register( leadBoots );
				player.getInventory().addItem( leadBoots.getItem() );

				LongFallBoot apertureBoots = new LongFallBoot();
				core.getItemManager().register( apertureBoots );
				player.getInventory().addItem( apertureBoots.getItem() );

				PowerThrusters powerGloves = new PowerThrusters();
				core.getItemManager().register( powerGloves );
				player.getInventory().addItem( powerGloves.getItem() );
				
				DebugStick stick = new DebugStick();
				core.getItemManager().register( stick );
				player.getInventory().addItem( stick.getItem() );
				
				SpaceHelmet helmet = new SpaceHelmet( 250, 600 );
				core.getItemManager().register( helmet );
				player.getInventory().addItem( helmet.getItem() );
				
				//				Location location = player.getLocation();
				//				
				//				GunsmokeNPC npc = SpaaaceUtil.getPlugin().getHandler().spawnNPC( player.getWorld(), "scr", "scr" );
				//				
				//				npc.getPlayer().teleport( location );
			}
		} )
		.applyTo( spawnCommand );
		core.getHandler().registerCommand( spawnCommand );

		PluginCommand gravityCommand = BukkitUtil.constructCommand( "setgravity" );
		new SubCommand( "setgravity" )
		.add( new SubCommand( new InputValidatorDouble() )
				.addSenderValidator( new SenderValidatorPlayer() )
				.defaultTo( ( sender, args, params ) -> {
					if ( sender instanceof Player ) {
						Player player = ( Player ) sender;
						GunsmokeEntityWrapper wrapper = core.getItemManager().getEntityWrapper( player );
						wrapper.setGravity( params.getFirst( Double.class ) );
					}
				} ) )
		.defaultTo( new CommandExecutableMessage( "You must provide a number!" ) )
		.applyTo( gravityCommand );
		core.getHandler().registerCommand( gravityCommand );
		
		PluginCommand spaceCommand = BukkitUtil.constructCommand( "space" );
		new SubCommand( "space" )
		.defaultTo( ( sender, args, params ) -> {
			if ( Bukkit.getWorld( "space" ) == null ) {
				WorldCreator creator = new WorldCreator( "space" )
						.generator( new SpaceGenerator( Bukkit.getWorld( "world" ).getSeed() ) )
						.environment( Environment.NORMAL )
						.type( WorldType.LARGE_BIOMES );
			
				World world = Bukkit.getServer().createWorld( creator );
				world.setGameRule( GameRule.DO_DAYLIGHT_CYCLE, false );
				world.setTime( 18000 );
				world.setGameRule( GameRule.DO_WEATHER_CYCLE, false );
				world.setWeatherDuration( 0 );
			}
			
			if ( sender instanceof Player ) {
				Player player = ( Player ) sender;
				World world = player.getWorld();
				if ( world.getName().equalsIgnoreCase( "space" ) ) {
					player.teleport( Bukkit.getWorld( "world" ).getSpawnLocation() );
				} else {
					player.teleport( Bukkit.getWorld( "space" ).getSpawnLocation() );
				}
			}
		} )
		.applyTo( spaceCommand );
		core.getHandler().registerCommand( spaceCommand );
		
		PluginCommand sinkholeCommand = BukkitUtil.constructCommand( "sinkhole" );
		new SubCommand( "sinkhole" )
		.defaultTo( ( sender, args, params ) -> {
			if ( sender instanceof Player ) {
				Player player = ( Player ) sender;
				World world = player.getWorld();
				ChunkGenerator generator = world.getGenerator();
				if ( generator instanceof SpaceGenerator ) {
					SpaceGenerator sGen = ( SpaceGenerator ) generator;
					Pair< Double, Double > coords = sGen.getSinkholeCoords( player.getLocation().getBlockX(), player.getLocation().getBlockZ() );
					player.sendMessage( "Sinkhole at " + coords.getFirst() + ", " + coords.getSecond() );
				} else {
					player.sendMessage( "Not in space!" );
				}
			} else {
				sender.sendMessage( "Must be player!" );
			}
		} )
		.applyTo( sinkholeCommand );
		core.getHandler().registerCommand( sinkholeCommand );

		if ( Bukkit.getWorld( "space" ) == null ) {
			WorldCreator creator = new WorldCreator( "space" )
					.generator( new SpaceGenerator( Bukkit.getWorld( "world" ).getSeed() ) )
					.environment( Environment.NORMAL )
					.type( WorldType.LARGE_BIOMES );
		
			World world = Bukkit.getServer().createWorld( creator );
			world.setGameRule( GameRule.DO_DAYLIGHT_CYCLE, false );
			world.setTime( 18000 );
			world.setGameRule( GameRule.DO_WEATHER_CYCLE, false );
			world.setWeatherDuration( 0 );
		}
		
		Bukkit.getPluginManager().registerEvents( new SpaceListener( core ), core );
		
		Bukkit.getScheduler().runTaskTimer( core, this::tick, 0, 1 );
	}
	
	private void tick() {
		currentTick++;
		// Make sure other worlds don't get Earth in the sky
		for ( World world : Bukkit.getWorlds() ) {
			long fullTime = world.getFullTime();
			if ( !SpaceUtil.isSpaceWorld( world ) ) {
				if ( fullTime >= 0 && fullTime < 24000 ) {
					world.setFullTime( fullTime + 24000 );
				}
			} else {
				if ( fullTime < 0 || fullTime >= 24000 ) {
					world.setFullTime( ( ( fullTime % 24000 ) + 24000 ) % 24000 ); 
				}
			}
		}
		
		// Teleport players who reach past 500
		for ( Player player : Bukkit.getOnlinePlayers() ) {
			Location location = player.getLocation();
			if ( location.getY() > 500 ) {
				World world = location.getWorld();
				if ( world.getEnvironment() == Environment.NORMAL ) {
					
					World teleportTo = Bukkit.getWorld( "world" );
					if ( !SpaceUtil.isSpaceWorld( world ) ) {
						// Allow space travel only at night
						if ( world.getTime() > 14000 && world.getTime() < 22000 ) {
							teleportTo = Bukkit.getWorld( "space" );
						}
					}
					
					// Give the worlds a 1:1 ratio
					if ( teleportTo != null ) {
						location.setY( 400 );
						location.setWorld( teleportTo );
						player.teleport( location );
					}
				}
			}
		}
		
		// Minor space related event
		for ( Player player : Bukkit.getOnlinePlayers() ) {
			Location location = player.getLocation();

			if ( SpaceUtil.isSpaceWorld( location.getWorld() ) ) {
				SpaceGenerator generator = ( SpaceGenerator ) player.getWorld().getGenerator();

				GunsmokeEntityWrapperLivingEntity wrapper = ( GunsmokeEntityWrapperLivingEntity ) core.getItemManager().getEntityWrapper( player );
				Location loc = player.getEyeLocation();
				Biome biome = loc.getBlock().getBiome();
				if ( loc.getY() > 255 || biome == Biome.SNOWY_TUNDRA ) {
					core.getInteractiveManager().addAir( wrapper, -1 );
				} else if ( biome == Biome.FLOWER_FOREST || biome == Biome.MUSHROOM_FIELDS ) {
					core.getInteractiveManager().addAir( wrapper, 10 );
				}
				
				if ( currentTick % 600 == 0 ) {
					Pair< Double, Double > coords = generator.getSinkholeCoords( location.getBlockX(), location.getBlockZ() );
					player.setCompassTarget( new Location( player.getWorld(), coords.getFirst(), 0, coords.getSecond() ) );
					
					// Update the player's compass
				}
			}
		}
	}
	
	public boolean canBreath( Entity entity ) {
		if ( core.getHandler().isInFluid( entity ) ) {
			return false;
		}
		
		World world = entity.getWorld();
		if ( SpaceUtil.isSpaceWorld( world ) ) {
			// Don't let them breath in the air I guess?
			Location eyeLoc = entity.getLocation();
			if ( entity instanceof LivingEntity ) {
				eyeLoc = ( ( LivingEntity ) entity ).getEyeLocation();
			}
			
			if ( eyeLoc.getY() > 255 ) {
				return false;
			}
			
			Biome biome = eyeLoc.getBlock().getBiome();
			return biome == Biome.MUSHROOM_FIELDS || biome == Biome.FLOWER_FOREST;
		}
		
		return true;
	}
}
