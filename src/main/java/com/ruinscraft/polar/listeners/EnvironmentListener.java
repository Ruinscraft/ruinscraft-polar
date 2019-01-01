package com.ruinscraft.polar.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

import com.ruinscraft.polar.populator.PolarPopulator;

public class EnvironmentListener implements Listener {

	@EventHandler
	public void onWorldInit(WorldInitEvent event) {
		event.getWorld().getPopulators().add(new PolarPopulator());
	}

}
