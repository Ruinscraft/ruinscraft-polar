package com.ruinscraft.polar.handlers.playerstatus;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ruinscraft.polar.handlers.PolarHandler;

public interface PlayerStatusHandler extends PolarHandler<Player> {

	World getWorld();

	List<Player> getCurrentPlayers();

	default Environment getEnvironment() {
		return getWorld().getEnvironment();
	}

	default void updateCompasses() {
		for (Player player : getCurrentPlayers()) {
			for (ItemStack item : player.getInventory().getContents()) {
				if (item == null) continue;
				if (item.getType() == Material.COMPASS) {
					ItemMeta meta = item.getItemMeta();
					if (meta.getDisplayName().contains("The Zero") || meta.getDisplayName().isEmpty()) {
						int x = player.getLocation().getBlockX();

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
			}
		}
	}

}
