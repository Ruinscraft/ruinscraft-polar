package com.ruinscraft.polar;

import java.util.Random;

import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.polar.handlers.populator.OverworldPopulatorHandler;

public class PolarPlugin extends JavaPlugin {

	private static PolarPlugin instance;
	private static Random random;

	private OverworldPopulatorHandler overworldPopulatorHandler;

	public static final double CHANCE_CONSTANT = .002D;

	public static PolarPlugin instance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		random = new Random();
		overworldPopulatorHandler = new OverworldPopulatorHandler();
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

	public static void log(String message) {
		instance.getLogger().info(message);
	}

	public double getChanceFromX(int x) {
		return 1 - (PolarPlugin.CHANCE_CONSTANT * Math.sqrt(Math.abs(x)));
	}

}
