package com.ruinscraft.polar.handlers.playerstatus;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.ruinscraft.polar.handlers.PolarHandler;

public interface PlayerStatusHandler extends PolarHandler<Player> {

	World getWorld();

	default Environment getEnvironment() {
		return getWorld().getEnvironment();
	}

}
