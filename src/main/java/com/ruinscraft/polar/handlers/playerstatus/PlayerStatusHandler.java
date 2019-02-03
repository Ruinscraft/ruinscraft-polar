package com.ruinscraft.polar.handlers.playerstatus;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ruinscraft.polar.handlers.PolarHandler;
import com.ruinscraft.polar.util.PolarUtil;

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
					PolarUtil.setCompass(item, player.getLocation().getBlockX());
				}
			}
		}
	}

}
