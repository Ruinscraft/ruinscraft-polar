package com.ruinscraft.polar;

import java.util.Random;

import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.polar.listeners.EnvironmentListener;
import com.ruinscraft.polar.populator.PopulatorHandler;

public class PolarPlugin extends JavaPlugin {

	private static PolarPlugin instance;
	private static Random random;

	private PopulatorHandler populatorHandler;

	public static final double CHANCE_CONSTANT = .002D;

	public static PolarPlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		random = new Random();
		populatorHandler = new PopulatorHandler();
		getServer().getPluginManager().registerEvents(new EnvironmentListener(), this);
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	public static Random random() {
		return random;
	}

	public PopulatorHandler getPopulatorHandler() {
		return populatorHandler;
	}

	public static void log(String message) {
		instance.getLogger().info(message);
	}

}
