package com.aaaaahhhhhhh.bananapuncher714.spaaace.implementation;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.SpaaaceCore;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api.command.SubCommand;
import com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util.BukkitUtil;

public class Spaaace {
	private SpaaaceCore core;
	
	public Spaaace( SpaaaceCore core ) {
		this.core = core;
		
		PluginCommand command = BukkitUtil.constructCommand( "spawnobserver" );
		new SubCommand( "spawnobserver " )
		.defaultTo( ( sender, args, params ) -> {
			if ( sender instanceof Player ) {
				Player player = ( Player ) sender;
				
				PowerHoe test = new PowerHoe();
				core.getItemManager().register( test );
				
				player.getInventory().addItem( test.getItem() );
				
//				Location location = player.getLocation();
//				
//				GunsmokeNPC npc = SpaaaceUtil.getPlugin().getHandler().spawnNPC( player.getWorld(), "scr", "scr" );
//				
//				npc.getPlayer().teleport( location );
			}
		} )
		.applyTo( command );
		
		core.getHandler().registerCommand( command );
		
		Bukkit.getPluginManager().registerEvents( new TemporaryListener(), core );
	}
}
