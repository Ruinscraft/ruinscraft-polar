package com.ruinscraft.polar;

import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.polar.listeners.EnvironmentListener;

public class PolarPlugin extends JavaPlugin {

	private static PolarPlugin instance;

	public static PolarPlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		getServer().getPluginManager().registerEvents(new EnvironmentListener(), this);
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	public static void log(String message) {
		instance.getLogger().info(message);
	}

}
