package com.ruinscraft.polar.listeners;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.WorldInitEvent;

import com.ruinscraft.polar.PolarPlugin;
import com.ruinscraft.polar.populator.PolarPopulator;

public class EnvironmentListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWorldInit(WorldInitEvent event) {
		event.getWorld().getPopulators().add(new PolarPopulator());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMobSpawn(EntitySpawnEvent event) {
		int x = event.getLocation().getBlockX();
		Entity entity = event.getEntity();

		if (!(entity instanceof LivingEntity)) return;

		LivingEntity livingEntity = (LivingEntity) entity;

		if (livingEntity instanceof Monster && x >= 0) event.setCancelled(true);
		if (livingEntity instanceof Animals && x < 0) event.setCancelled(true);

		double spawnMore;
		if (x >= 0) {
			spawnMore = Math.sqrt(PolarPlugin.CHANCE_CONSTANT * (x/2));
		} else {
			spawnMore = Math.sqrt(PolarPlugin.CHANCE_CONSTANT * (x/20));
		}

		if (spawnMore > 10) {
			spawnMore = 10 + (spawnMore / 10);
		}

		for (int i = 0; i < spawnMore; i++) {
			event.getLocation().getWorld().spawnEntity(event.getLocation(), entity.getType());
		}
	}

}
