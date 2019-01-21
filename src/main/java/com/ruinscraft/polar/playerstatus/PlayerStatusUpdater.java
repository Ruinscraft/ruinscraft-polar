package com.ruinscraft.polar.playerstatus;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

public interface PlayerStatusUpdater {

	void handlePositive(Player player, double c);

	void handleNegative(Player player, double c);

	World getWorld();

	default Environment getEnvironment() {
		return getWorld().getEnvironment();
	}

}
