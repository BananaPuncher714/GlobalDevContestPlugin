package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.BlockFace;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import io.github.bananapuncher714.nbteditor.NBTEditor;

public class BukkitUtil {
	private static Constructor< PluginCommand > PLUGINCOMMAND_CONSTRUCTOR;
	private static Field SIMPLEPLUGINMANAGER_FILEASSOCIATIONS;
	private static Method JAVAPLUGINLOADER_GETCLASS;
	private static Method JAVAPLUGINLOADER_SETCLASS;
	private static Method JAVAPLUGINLOADER_REMOVECLASS;
	
	static {
		try {
			PLUGINCOMMAND_CONSTRUCTOR = PluginCommand.class.getDeclaredConstructor( String.class, Plugin.class );
			PLUGINCOMMAND_CONSTRUCTOR.setAccessible( true );
			SIMPLEPLUGINMANAGER_FILEASSOCIATIONS = SimplePluginManager.class.getDeclaredField( "fileAssociations" );
			SIMPLEPLUGINMANAGER_FILEASSOCIATIONS.setAccessible( true );
			JAVAPLUGINLOADER_GETCLASS = JavaPluginLoader.class.getDeclaredMethod( "getClassByName", String.class );
			JAVAPLUGINLOADER_GETCLASS.setAccessible( true );
			JAVAPLUGINLOADER_SETCLASS = JavaPluginLoader.class.getDeclaredMethod( "setClass", String.class, Class.class );
			JAVAPLUGINLOADER_SETCLASS.setAccessible( true );
			JAVAPLUGINLOADER_REMOVECLASS = JavaPluginLoader.class.getDeclaredMethod( "removeClass", String.class );
			JAVAPLUGINLOADER_REMOVECLASS.setAccessible( true );
		} catch ( NoSuchMethodException | SecurityException | NoSuchFieldException e ) {
			e.printStackTrace();
		}
	}
	
	public static PluginCommand constructCommand( String id ) {
		PluginCommand command = null;
		try {
			command = PLUGINCOMMAND_CONSTRUCTOR.newInstance( id, SpaaaceUtil.getPlugin() );
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
		return command;
	}
	
	/**
	 * Check if the plugin given is loaded
	 * 
	 * @param id
	 * case sensitive plugin id
	 * @return
	 * If the plugin is loaded and enabled
	 */
	public static boolean isPluginLoaded( String id ) {
		return Bukkit.getPluginManager().isPluginEnabled( id );
	}
	
	public static Class< ? > getClassByName( String name ) {
		PluginManager manager = Bukkit.getPluginManager();
		if ( manager instanceof SimplePluginManager ) {
			SimplePluginManager sManager = ( SimplePluginManager ) manager;
			try {
				Map< Pattern, PluginLoader > associations = ( Map< Pattern, PluginLoader > ) SIMPLEPLUGINMANAGER_FILEASSOCIATIONS.get( sManager );
				for ( PluginLoader loader : associations.values() ) {
					if ( loader instanceof JavaPluginLoader ) {
						return ( Class< ? > ) JAVAPLUGINLOADER_GETCLASS.invoke( loader, name );
					}
				}
			} catch ( IllegalArgumentException | IllegalAccessException | InvocationTargetException e ) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void setClassToJavaPluginLoader( String name, Class< ? > clazz ) {
		PluginManager manager = Bukkit.getPluginManager();
		if ( manager instanceof SimplePluginManager ) {
			SimplePluginManager sManager = ( SimplePluginManager ) manager;
			try {
				Map< Pattern, PluginLoader > associations = ( Map< Pattern, PluginLoader > ) SIMPLEPLUGINMANAGER_FILEASSOCIATIONS.get( sManager );
				for ( PluginLoader loader : associations.values() ) {
					if ( loader instanceof JavaPluginLoader ) {
						JAVAPLUGINLOADER_SETCLASS.invoke( loader, name, clazz );
					}
				}
			} catch ( IllegalArgumentException | IllegalAccessException | InvocationTargetException e ) {
				e.printStackTrace();
			}
		}
	}
	
	public static void removeClassFromJavaPluginLoader( String name ) {
		PluginManager manager = Bukkit.getPluginManager();
		if ( manager instanceof SimplePluginManager ) {
			SimplePluginManager sManager = ( SimplePluginManager ) manager;
			try {
				Map< Pattern, PluginLoader > associations = ( Map< Pattern, PluginLoader > ) SIMPLEPLUGINMANAGER_FILEASSOCIATIONS.get( sManager );
				for ( PluginLoader loader : associations.values() ) {
					if ( loader instanceof JavaPluginLoader ) {
						JAVAPLUGINLOADER_REMOVECLASS.invoke( loader, name );
					}
				}
			} catch ( IllegalArgumentException | IllegalAccessException | InvocationTargetException e ) {
				e.printStackTrace();
			}
		}
	}
	
	public final static Location getBlockLocation( Location loc ) {
		return new Location( loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() );
	}
	
	public final static ItemStack getEquipment( LivingEntity entity, EquipmentSlot slot ) {
		switch ( slot ) {
		case CHEST:
			return entity.getEquipment().getChestplate();
		case FEET:
			return entity.getEquipment().getBoots();
		case HEAD:
			return entity.getEquipment().getHelmet();
		case LEGS:
			return entity.getEquipment().getLeggings();
		case HAND:
			return entity.getEquipment().getItemInMainHand();
		case OFF_HAND:
			return entity.getEquipment().getItemInOffHand();
		default:
			return null;
		}
	}
	
	public final static void setEquipment( LivingEntity entity, ItemStack item, EquipmentSlot slot ) {
		switch ( slot ) {
		case CHEST:
			entity.getEquipment().setChestplate( item );
			return;
		case FEET:
			entity.getEquipment().setBoots( item );
			return;
		case HEAD:
			entity.getEquipment().setHelmet( item );
			return;
		case LEGS:
			entity.getEquipment().setLeggings( item );
			return;
		case HAND:
			entity.getEquipment().setItemInMainHand( item );
			return;
		case OFF_HAND:
			entity.getEquipment().setItemInOffHand( item );
			return;
		}
	}
	
	public final static boolean isRightClickable( Material material ) {
		switch ( material ) {
		case BOW:
		case TRIDENT:
		case SHIELD:
		case CROSSBOW:
			return true;
		default:
			return false;
		}
	}
	
	public final static ItemStack setItemCooldown( ItemStack item, double ms ) {
		double attackSpeed = ( 1 / ( ms / 1000.0 ) ) - 4;
		ItemMeta meta = item.getItemMeta();
		meta.addAttributeModifier( Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier( UUID.randomUUID(), "Cooldown", attackSpeed, Operation.ADD_NUMBER, EquipmentSlot.HAND ) );
		item.setItemMeta( meta );
		return item;
	}
	
	public static void flash( LivingEntity player ) {
		player.addPotionEffect( new PotionEffect( PotionEffectType.NIGHT_VISION, 2, 0, true, false ) );
		player.addPotionEffect( new PotionEffect( PotionEffectType.BLINDNESS, 4, 0, true, false ) );
	}
	
	public static Vector toVector( BlockFace face ) {
		return new Vector( face.getModX(), face.getModY(), face.getModZ() );
	}
	
	public static ItemStack setCustomModelData( ItemStack item, int val ) {
		return NBTEditor.set( item, val, "CustomModelData" );
	}
}
