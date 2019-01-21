package com.ruinscraft.polar.playerstatus;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.ruinscraft.polar.PolarPlugin;

public class PlayerOverworldStatusUpdater implements PlayerStatusUpdater, Runnable {

	private World overworld;

	public PlayerOverworldStatusUpdater(World overworld) {
		this.overworld = overworld;
	}

	@Override
	public void run() {
		List<Player> players = overworld.getPlayers();

		for (Player player : players) {
			int x = player.getLocation().getBlockX();
			double chanceMultiplier = 1 - (PolarPlugin.CHANCE_CONSTANT * Math.sqrt(Math.abs(x)));

			if (x >= 0) handlePositive(player, chanceMultiplier);
			else handleNegative(player, chanceMultiplier);
		}
	}

	public void handlePositive(Player player, double c) {
		// do positive thingies
	}

	public void handleNegative(Player player, double c) {
		// do negative thingies
	}

	public World getWorld() {
		return this.overworld;
	}

}
