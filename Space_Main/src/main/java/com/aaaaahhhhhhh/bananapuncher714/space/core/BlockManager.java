package com.aaaaahhhhhhh.bananapuncher714.space.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.DamageType;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.block.GunsmokeBlock;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.block.VanillaMaterialData;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapperPlayer;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.block.GunsmokeBlockBreakEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.block.GunsmokeBlockCreateEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.block.GunsmokeBlockDamageEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.SpaceUtil;

public class BlockManager {
	public static final int UPDATE_BLOCK_DELAY = 20 * 15;
	
	private SpaceCore plugin;
	
	private Map< Location, GunsmokeBlock > blocks = new HashMap< Location, GunsmokeBlock >();
	
	public BlockManager( SpaceCore plugin ) {
		this.plugin = plugin;
		
		Bukkit.getScheduler().runTaskTimer( plugin, this::update, 0, UPDATE_BLOCK_DELAY );
	}
	
	private void update() {
		for ( GunsmokeBlock block : blocks.values() ) {
			block.updateBlockStage();
		}
	}
	
	public void damage( Location location, double damage, GunsmokeRepresentable damager, DamageType type ) {
		GunsmokeBlock block = getBlockOrCreate( location );

		if ( block == null ) {
			return;
		}
		
		GunsmokeBlockDamageEvent damageEvent = new GunsmokeBlockDamageEvent( block, damage, damager, type );
		damageEvent.callEvent();
		if ( damageEvent.isCancelled() ) {
			return;
		}
		damage = damageEvent.getDamage();
		
		if ( block.isInvincible() ) {
			return;
		}
		
		block.damage( damage, type );

		if ( block.getHealth() <= 0 ) {
			GunsmokeBlockBreakEvent breakEvent = new GunsmokeBlockBreakEvent( block, damager );
			breakEvent.callEvent();
			if ( !breakEvent.isCancelled() ) {
				SpaceUtil.getPlugin().getBlockManager().playVanillaBlockBreak( location );
				if ( damager instanceof GunsmokeEntityWrapperPlayer ) {
					SpaceUtil.getPlugin().setAllowBreakBlock( true );
					SpaceUtil.getPlugin().getHandler().breakBlock( ( ( GunsmokeEntityWrapperPlayer ) damager ).getEntity(), location );
					SpaceUtil.getPlugin().setAllowBreakBlock( false );
				}
				
				block.destroy();
				blocks.remove( block.getLocation() );
				plugin.getItemManager().remove( block.getUUID() );
			} else {
				block.updateBlockStage();
			}
		} else {
			block.updateBlockStage();
		}
	}
	
	public void destroy( Location location ) {
		for ( Iterator< Entry< Location, GunsmokeBlock > > iterator = blocks.entrySet().iterator(); iterator.hasNext(); ) {
			GunsmokeBlock block = iterator.next().getValue();
			
			if ( block.contains( location ) ) {
				iterator.remove();
				block.destroy();
				blocks.remove( block.getLocation() );
				plugin.getItemManager().remove( block.getUUID() );
			}
		}
	}
	
	public void updateBlockStage( Location location ) {
		GunsmokeBlock block = getBlockAt( location );
		block.updateBlockStage();
	}
	
	public GunsmokeBlock getBlockAt( Location location ) {
		for ( GunsmokeBlock block : blocks.values() ) {
			if ( block.contains( location ) ) {
				return block;
			}
		}
		return null;
	}
	
	public GunsmokeBlock getBlockOrCreate( Location location ) {
		for ( GunsmokeBlock block : blocks.values() ) {
			if ( block.contains( location ) ) {
				return block;
			}
		}
		GunsmokeBlock block = new GunsmokeBlock( location, getDefaultResistanceFor( location.getBlock().getType() ));
		
		GunsmokeBlockCreateEvent event = new GunsmokeBlockCreateEvent( block );
		event.callEvent();
		block = event.getRepresentable();
		
		blocks.put( block.getLocation(), block );
		plugin.getItemManager().register( block );
		return block;
	}
	
	public void unregisterBlock( Location location ) {
		GunsmokeBlock block = getBlockAt( location );
		if ( block != null ) {
			unregisterBlock( block );
		}
	}
	
	public void unregisterBlock( GunsmokeBlock block ) {
		blocks.remove( block.getLocation() );
		plugin.getItemManager().remove( block.getUUID() );
	}
	
	public void registerBlock( GunsmokeBlock block ) {
		Location location = block.getLocation();
		GunsmokeBlock old = getBlockAt( location );
		if ( old != null ) {
			unregisterBlock( old );
		}
		
		blocks.put( block.getLocation(), block );
		plugin.getItemManager().register( block );
		block.updateBlockStage();
	}
	
	// Note: Do NOT return 0 or bad things will happen!
	public double getDefaultResistanceFor( Material material ) {
		switch ( material ) {
		case WATER:
		case LAVA:
		case BEDROCK:
		case BARRIER:
		case STONE: return 75;
		case DIRT:
		case GRASS: return 40;
		case GLASS: return 1;
		case PRISMARINE_BRICKS: return 200;
		default: {
			VanillaMaterialData vData = plugin.getHandler().getVanillaMaterialDataFor( material );
			float hardness = vData.getStrength();
			if ( hardness < 0 ) {
				return hardness;
			} else if ( hardness == 0 ) {
				return 1;
			}
			// Hardness in ticks, multiplied by 5
			return hardness * 20 * 5;
		}
		}
	}
	
	public void playVanillaBlockBreak( Location location ) {
		Block block = location.getBlock();
		Material mat = block.getType();
		VanillaMaterialData vData = plugin.getHandler().getVanillaMaterialDataFor( mat );
		location.getWorld().spawnParticle( Particle.BLOCK_CRACK, location.clone().add( .5, .5, .5 ), 100, .25, .25, .25, block.getBlockData() );
		SoundMixer.playSound( location, 15, vData.getSounds().getSound( "BREAK" ) );
	}
}
