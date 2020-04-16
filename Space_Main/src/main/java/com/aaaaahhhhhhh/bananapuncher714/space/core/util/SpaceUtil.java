package com.aaaaahhhhhhh.bananapuncher714.space.core.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.aaaaahhhhhhh.bananapuncher714.space.core.SpaceCore;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.DamageType;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.GunsmokeRepresentable;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.RegenType;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.block.GunsmokeBlock;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.GunsmokeEntity;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.entity.bukkit.GunsmokeEntityWrapper;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.util.CollisionResultBlock;
import com.aaaaahhhhhhh.bananapuncher714.space.implementation.world.SpaceGenerator;

public final class SpaceUtil {
	private static final SpaceCore PLUGIN;
	
	private static final EquipmentSlot[] EQUIPMENT_SLOT_IMPORTANCE;
	
	static {
		PLUGIN = JavaPlugin.getPlugin( SpaceCore.class );
		
		EQUIPMENT_SLOT_IMPORTANCE = new EquipmentSlot[ EquipmentSlot.values().length ];
		EQUIPMENT_SLOT_IMPORTANCE[ 0 ] = EquipmentSlot.HAND;
		EQUIPMENT_SLOT_IMPORTANCE[ 1 ] = EquipmentSlot.OFF_HAND;
		EQUIPMENT_SLOT_IMPORTANCE[ 2 ] = EquipmentSlot.HEAD;
		EQUIPMENT_SLOT_IMPORTANCE[ 3 ] = EquipmentSlot.CHEST;
		EQUIPMENT_SLOT_IMPORTANCE[ 4 ] = EquipmentSlot.LEGS;
		EQUIPMENT_SLOT_IMPORTANCE[ 5 ] = EquipmentSlot.FEET;
	}
	
	public static void callEventSync( Event event ) {
		if ( Bukkit.isPrimaryThread() ) {
			Bukkit.getPluginManager().callEvent( event );
		} else {
			Bukkit.getScheduler().runTask( getPlugin(), () -> callEventSync( event ) );
		}
	}
	
	public static int getCurrentTick() {
		return getPlugin().getHandler().getServerTick();
	}
	
	public static void playHurtAnimationFor( LivingEntity entity ) {
		getPlugin().getHandler().playHurtAnimationFor( entity );
	}
	
	public static GunsmokeEntityWrapper getEntity( Entity entity ) {
		return getPlugin().getItemManager().getEntityWrapper( entity );
	}
	
	public static List< CollisionResultBlock > rayTrace( Location start, Vector ray ) {
		return getPlugin().getHandler().rayTrace( start, ray );
	}
	
	public static List< CollisionResultBlock > rayTrace( Location start, Vector ray, double dist ) {
		return getPlugin().getHandler().rayTrace( start, ray, dist );
	}
	
	public static boolean damage( GunsmokeEntity entity, DamageType type, double damage, DamageCause cause ) {
		return getPlugin().getDamageManager().damage( entity, damage, type, cause );
	}
	
	public static boolean damage( GunsmokeEntity entity, DamageType type, double damage, GunsmokeEntity damager ) {
		return getPlugin().getDamageManager().damage( entity, damage, type, damager );
	}
	
	public static boolean regen( GunsmokeEntity entity, RegenType type, double amount, RegainReason reason ) {
		return getPlugin().getDamageManager().regen( entity, amount, type, reason );
	}
	
	public static boolean regen( GunsmokeEntity entity, RegenType type, double amount ) {
		return getPlugin().getDamageManager().regen( entity, amount, type );
	}
	
	public static void setBlockStage( Location location, int stage ) {
		getPlugin().getHandler().damageBlock( location, stage );
	}
	
	public static GunsmokeBlock getBlockAt( Location location ) {
		return getPlugin().getBlockManager().getBlockOrCreate( location );
	}
	
	public static void damageBlockAt( Location location, double damage, GunsmokeRepresentable damager, DamageType type ) {
		getPlugin().getBlockManager().damage( location, damage, damager, type );
	}
	
	public static boolean isSpaceWorld( World world ) {
		return world.getGenerator() instanceof SpaceGenerator;
	}
	
	public static EquipmentSlot[] getEquipmentSlotOrdering() {
		return EQUIPMENT_SLOT_IMPORTANCE;
	}
	
	public static SpaceCore getPlugin() {
		return PLUGIN;
	}
}
