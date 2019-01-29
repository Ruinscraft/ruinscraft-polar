package com.ruinscraft.polar.handlers.playerstatus;

import java.util.List;

import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import com.ruinscraft.polar.PolarPlugin;
import com.ruinscraft.polar.util.ChanceUtil;

public class OverworldPlayerStatusHandler implements PlayerStatusHandler, Runnable {

	private World overworld;

	public OverworldPlayerStatusHandler(World overworld) {
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

	@Override
	public void handlePositive(Player player, double c) {
		if (player.getLocation().getBlock().getBiome() == Biome.FLOWER_FOREST) {
			if (ChanceUtil.chance(25) && player.getFoodLevel() < 20) {
				player.setFoodLevel(player.getFoodLevel() + 1);
				player.setSaturation(player.getSaturation() * 2);
			}
		}
		player.resetPlayerWeather();
	}

	@Override
	public void handleNegative(Player player, double c) {
		player.setFoodLevel(player.getFoodLevel() - ((int) (.1/(Math.random() * c))));
		player.setExhaustion(player.getExhaustion() + (float) (.8/c));
		player.setPlayerWeather(WeatherType.DOWNFALL);
		player.setSaturation(0);
		player.setWalkSpeed(0.2F * (float) (c));
	}

	@Override
	public World getWorld() {
		return this.overworld;
	}

}
