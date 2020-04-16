package com.aaaaahhhhhhh.bananapuncher714.spaaace.implementation;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.SpaaaceCore;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.Pair;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.command.SubCommand;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.command.executor.CommandExecutableMessage;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.command.validator.InputValidatorDouble;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.command.validator.sender.SenderValidatorPlayer;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.BukkitUtil;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.implementation.world.SpaceGenerator;

public class Spaaace {
	private SpaaaceCore core;

	public Spaaace( SpaaaceCore core ) {
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
						.generator( new SpaceGenerator() )
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

		Bukkit.getPluginManager().registerEvents( new SpaceListener(), core );
	}
}
