package com.ruinscraft.polar.playerstatus;

import java.util.List;

import org.bukkit.WeatherType;
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
		// do more positive thingies
		player.resetPlayerWeather();
	}

	public void handleNegative(Player player, double c) {
		player.setFoodLevel(player.getFoodLevel() - ((int) (.1/(Math.random() * c))));
		player.setExhaustion(player.getExhaustion() + (float) (.8/c));
		player.setPlayerWeather(WeatherType.DOWNFALL);
		player.setSaturation(0);
		player.setWalkSpeed(0.2F * (float) (c));
	}

	public World getWorld() {
		return this.overworld;
	}

}
