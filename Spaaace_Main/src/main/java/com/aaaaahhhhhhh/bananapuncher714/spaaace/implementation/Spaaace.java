package com.aaaaahhhhhhh.bananapuncher714.spaaace.implementation;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.SpaaaceCore;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.command.SubCommand;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.command.executor.CommandExecutableMessage;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.command.validator.InputValidatorDouble;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.command.validator.sender.SenderValidatorPlayer;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.BukkitUtil;

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
		new SubCommand( "setgravity " )
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

		Bukkit.getPluginManager().registerEvents( new TemporaryListener(), core );
	}
}
