package com.ruinscraft.polar;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerOverworldStatusUpdater implements Runnable {

	private World overworld;

	public PlayerOverworldStatusUpdater(World overworld) {
		this.overworld = overworld;
	}

	@Override
	public void run() {
		List<Player> players = overworld.getPlayers();

		for (Player player : players) {
			Location location = player.getLocation();
			double chanceMultiplier = 1 - (PolarPlugin.CHANCE_CONSTANT * Math.sqrt(Math.abs(location.getX())));

			if (location.getBlockX() >= 0) handlePositive(player, chanceMultiplier);
			else handleNegative(player, chanceMultiplier);
		}
	}

	public void handlePositive(Player player, double c) {
		// do positive thingies
	}

	public void handleNegative(Player player, double c) {
		// do negative thingies
	}

}
