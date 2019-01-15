package com.ruinscraft.polar.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.WorldInitEvent;

import com.ruinscraft.polar.PolarPlugin;
import com.ruinscraft.polar.populator.PolarPopulator;

public class EnvironmentListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWorldInit(WorldInitEvent event) {
		event.getWorld().getPopulators().add(new PolarPopulator());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		int x = event.getLocation().getBlockX();
		LivingEntity livingEntity = event.getEntity();

		if (event.getSpawnReason() == SpawnReason.CUSTOM) {
			return;
		}

		if (livingEntity instanceof Monster && x >= 0) {
			event.setCancelled(true);
			return;
		} if (livingEntity instanceof Animals && x < 0) {
			event.setCancelled(true);
			return;
		}

		double spawnMore;
		if (x >= 0) {
			spawnMore = Math.sqrt(PolarPlugin.CHANCE_CONSTANT * (x/4));
		} else {
			spawnMore = Math.sqrt(PolarPlugin.CHANCE_CONSTANT * (Math.abs(x)/2)) + (Math.random() * 3);
		}

		if (spawnMore > 10) {
			spawnMore = 10 + (spawnMore / 100);
		}

		final int spawnMoreInt = (int) spawnMore;
		Bukkit.getScheduler().runTask(PolarPlugin.getInstance(), () -> {
			for (int i = 0; i < spawnMoreInt; i++) {
				event.getLocation().getWorld().spawnEntity(event.getLocation(), livingEntity.getType());
			}
		});
	}

}
