package com.ruinscraft.polar.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Utility class for miscellaneous Polar-related methods
 *
 */
public class PolarUtil {

	public static void setCompass(ItemStack item, int x) {
		ItemMeta meta = item.getItemMeta();
		if (meta.getDisplayName().contains("The Zero") || meta.getDisplayName().isEmpty()) {
			if (x > 1) {
				meta.setDisplayName(ChatColor.GREEN + "The Zero is " + ChatColor.GRAY 
						+ x + ChatColor.GREEN + " blocks away");
			} else if (x >= 0) {
				meta.setDisplayName(ChatColor.GREEN + "You are currently on The Zero");
			} else if (x >= -2) {
				meta.setDisplayName(ChatColor.RED + "You are currently on The Zero");
			} else if (x < -2) {
				meta.setDisplayName(ChatColor.RED + "The Zero is " + ChatColor.GRAY 
						+ Math.abs(x + 1) + ChatColor.RED + " blocks away");
			}
			item.setItemMeta(meta);
		}
	}

	public static int getNearbyEntityCount(Entity entity, int radius) {
		return getNearbyEntityCount(entity, entity.getType(), radius);
	}

	public static int getNearbyEntityCount(Entity entity, EntityType entityType, int radius) {
		int i = 0;

		for (Entity nearbyEntity : entity.getNearbyEntities(radius, radius, radius)) {
			if (nearbyEntity.getType() == entityType) i++;
		}

		return i;
	}

}
