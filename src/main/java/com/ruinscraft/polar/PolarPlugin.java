package com.ruinscraft.polar;

import java.util.Random;

import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.polar.handlers.BlockBreakHandler;
import com.ruinscraft.polar.handlers.BlockPlaceHandler;
import com.ruinscraft.polar.handlers.populator.OverworldPopulatorHandler;

public class PolarPlugin extends JavaPlugin {

	private static PolarPlugin instance;
	private static Random random;

	private OverworldPopulatorHandler overworldPopulatorHandler;
	private BlockBreakHandler blockBrokenHandler;
	private BlockPlaceHandler blockPlaceHandler;

	public static final double CHANCE_CONSTANT = .002D;

	public static PolarPlugin instance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		random = new Random();

		overworldPopulatorHandler = new OverworldPopulatorHandler();
		blockBrokenHandler = new BlockBreakHandler();
		blockPlaceHandler = new BlockPlaceHandler();

		getServer().getPluginManager().registerEvents(new EnvironmentListener(), this);
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	public static Random random() {
		return random;
	}

	public OverworldPopulatorHandler getOverworldPopulatorHandler() {
		return overworldPopulatorHandler;
	}

	public BlockBreakHandler getBlockBrokenHandler() {
		return blockBrokenHandler;
	}

	public BlockPlaceHandler getBlockPlaceHandler() {
		return blockPlaceHandler;
	}

	public static void log(String message) {
		instance.getLogger().info(message);
	}

	public double getChanceFromX(int x) {
		double chance = PolarPlugin.CHANCE_CONSTANT * Math.sqrt(Math.abs(x));
		if (chance > .95) chance = .95;
		return 1 - chance;
	}

}
