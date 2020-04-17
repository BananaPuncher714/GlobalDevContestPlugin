package com.aaaaahhhhhhh.bananapuncher714.space.implementation;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import com.aaaaahhhhhhh.bananapuncher714.space.core.SpaceCore;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Breathable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.DamageType;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Gravitable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.block.GunsmokeBlock;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.GunsmokeAirChangeEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.block.GunsmokeBlockCreateEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity.GunsmokeEntityDamageEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.events.entity.GunsmokeEntityLoadEvent;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.item.GunsmokeItem;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.SpaceUtil;

public class SpaceListener implements Listener {
	private SpaceCore core;
	
	private UUID leadBoots;
	private UUID apertureBoots;
	private UUID helmet;
	
	protected SpaceListener( SpaceCore core ) {
		this.core = core;
		
		LeadBoots bootItem = new LeadBoots();
		leadBoots = bootItem.getUUID();
		ShapedRecipe leadBootRecipe = new ShapedRecipe( NamespacedKey.minecraft( "lead_boots" ), bootItem.getItem() )
				.shape( "i.i", "x.x", "x.x" )
				.setIngredient( 'i', Material.IRON_INGOT )
				.setIngredient( 'x', Material.OBSIDIAN );
		Bukkit.addRecipe( leadBootRecipe );
			
		LongFallBoot apertureBootItem = new LongFallBoot();
		apertureBoots = apertureBootItem.getUUID();
		ShapedRecipe apretureBootRecipe = new ShapedRecipe( NamespacedKey.minecraft( "aperture_boots" ), apertureBootItem.getItem() )
				.shape( ".a.", ".b.", ".c." )
				.setIngredient( 'a', Material.SHULKER_SHELL )
				.setIngredient( 'b', Material.IRON_BOOTS )
				.setIngredient( 'c', Material.SLIME_BLOCK );
		Bukkit.addRecipe( apretureBootRecipe );
		
		SpaceHelmet helmetItem = new SpaceHelmet( 0, 6000 );
		helmet = helmetItem.getUUID();
		ShapedRecipe helmetRecipe = new ShapedRecipe( NamespacedKey.minecraft( "space_helmet" ), helmetItem.getItem() )
				.shape( "aaa", "aba", "aaa" )
				.setIngredient( 'a', Material.OBSIDIAN )
				.setIngredient( 'b', Material.GLASS_PANE );
		Bukkit.addRecipe( helmetRecipe );
	}
	
	@EventHandler
	private void onEvent( PlayerJoinEvent event ) {
		Bukkit.getScheduler().runTaskLater( core, () -> { event.getPlayer().setResourcePack( "https://www.dropbox.com/s/qxt15bd6kgat912/Space.zip?dl=1" ); }, 20 );
	}
	
	@EventHandler
	private void onEvent( GunsmokeBlockCreateEvent event ) {
		GunsmokeBlock block = event.getRepresentable();
		RegeneratingGunsmokeBlock regenBlock = new RegeneratingGunsmokeBlock( block.getLocation(), block.getHealth() );
		event.setBlock( regenBlock );
	}
	
	@EventHandler
	private void onEvent( EntitySpawnEvent event ) {
		Location location = event.getLocation();
		if ( SpaceUtil.isSpaceWorld( location.getWorld() ) ) {
			if ( event.getEntityType() == EntityType.SPIDER ) {
				// Spawn a rabbit for each spider
				Rabbit rabbit = location.getWorld().spawn( location, Rabbit.class );
				rabbit.setRabbitType( Type.THE_KILLER_BUNNY );
			}
		}
	}
	
	@EventHandler
	private void onEvent( GunsmokeEntityLoadEvent event ) {
		GunsmokeEntity ent = event.getRepresentable();
		Location location = ent.getLocation();
		if ( SpaceUtil.isSpaceWorld( location.getWorld() ) ) {
			if ( ent instanceof Gravitable ) {
				Gravitable gEnt = ( Gravitable ) ent;
				
				core.getGravityManager().setGravity( gEnt, .3 );
			}
		}
	}
	
	@EventHandler
	private void onEvent( PlayerChangedWorldEvent event ) {
		Player player = event.getPlayer();
		GunsmokeEntityWrapper wrapper = SpaceUtil.getEntity( player );
		Location location = event.getPlayer().getLocation();
		if ( SpaceUtil.isSpaceWorld( location.getWorld() ) ) {
			core.getGravityManager().setGravity( wrapper, .3 );
		} else {
			player.setCompassTarget( player.getBedSpawnLocation() != null ? player.getBedSpawnLocation() : player.getWorld().getSpawnLocation() );
			core.getGravityManager().setGravity( wrapper, 1 );
		}
	}
	
	@EventHandler
	private void onEvent( GunsmokeEntityDamageEvent event ) {
		if ( event.getCause() == DamageCause.FALL && event.getType() == DamageType.VANILLA ) {
			GunsmokeEntity entity = event.getRepresentable();
			if ( entity instanceof Gravitable ) {
				Gravitable gEnt = ( Gravitable ) entity;
				double grav = gEnt.getGravity();
				
				double damage = grav * event.getDamage();
				if ( damage < 1 ) {
					event.setCancelled( true );
				}
				event.setDamage( damage );
			}
		}
	}
	
	@EventHandler
	private void onEvent( GunsmokeAirChangeEvent event ) {
		Breathable breathable = event.getBreathable();
		if ( breathable instanceof GunsmokeEntityWrapper ) {
			GunsmokeEntityWrapper wrapper = ( GunsmokeEntityWrapper ) breathable;
			if ( !core.getSpaceInstance().canBreath( wrapper.getEntity() ) ) {
				if ( event.getAir() > 0 ) {
					event.setCancelled( true );
				}
			}
		}
	}
	
	@EventHandler
	private void onEvent( CraftItemEvent event ) {
		Recipe recipe = event.getRecipe();
		ItemStack result = recipe.getResult();
		if ( result != null ) {
			UUID uuid = GunsmokeItem.getUUID( result );
			if ( uuid.equals( leadBoots ) ) {
				LeadBoots boots = new LeadBoots();
				core.getItemManager().register( boots );
				event.setCurrentItem( boots.getItem() );
			} else if ( uuid.equals( apertureBoots ) ) {
				LongFallBoot boots = new LongFallBoot();
				core.getItemManager().register( boots );
				event.setCurrentItem( boots.getItem() );
			} else if ( uuid.equals( helmet ) ) {
				SpaceHelmet helmet = new SpaceHelmet( 0, 6000 );
				helmet.setRefillRate( 20 );
				core.getItemManager().register( helmet );
				event.setCurrentItem( helmet.getItem() );
			}
		}
	}
}
