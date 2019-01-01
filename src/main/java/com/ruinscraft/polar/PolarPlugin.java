package com.ruinscraft.polar;

import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.polar.listeners.EnvironmentListener;
import com.ruinscraft.polar.populator.PopulatorHandler;

public class PolarPlugin extends JavaPlugin {

	private static PolarPlugin instance;

	private PopulatorHandler populatorHandler;

	public static PolarPlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		populatorHandler = new PopulatorHandler();
		getServer().getPluginManager().registerEvents(new EnvironmentListener(), this);
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	public PopulatorHandler getPopulatorHandler() {
		return populatorHandler;
	}

	public static void log(String message) {
		instance.getLogger().info(message);
	}

}
