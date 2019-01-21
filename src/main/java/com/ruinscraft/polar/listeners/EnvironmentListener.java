package com.ruinscraft.polar.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Golem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.WorldInitEvent;

import com.ruinscraft.polar.playerstatus.PlayerOverworldStatusUpdater;
import com.ruinscraft.polar.PolarPlugin;
import com.ruinscraft.polar.populator.ChanceUtil;
import com.ruinscraft.polar.populator.PolarPopulator;

public class EnvironmentListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWorldInit(WorldInitEvent event) {
		World world = event.getWorld();
		if (world.getEnvironment() != Environment.NORMAL) return;

		world.getPopulators().add(new PolarPopulator());

		PolarPlugin.getInstance().getServer().getScheduler().runTaskTimer(
				PolarPlugin.getInstance(), new PlayerOverworldStatusUpdater(event.getWorld()), 0, 100);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		int x = event.getLocation().getBlockX();
		LivingEntity livingEntity = event.getEntity();

		if (event.getSpawnReason() == SpawnReason.CUSTOM) {
			return;
		}

		if (x >= 0) {
			if (livingEntity instanceof Monster) {
				event.setCancelled(true);
				return;
			}
		} else if (x < 0) {
			if (livingEntity instanceof Animals) {
				event.setCancelled(true);
				return;
			} else if (livingEntity instanceof Villager ||
					livingEntity instanceof Fish ||
					livingEntity instanceof Golem) {
				event.setCancelled(true);
				return;
			}
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

		if (ChanceUtil.chanceOutOf(1, 200)) {
			spawnMore = spawnMore + 25;
		}

		final int spawnMoreInt = (int) spawnMore;
		Bukkit.getScheduler().runTask(PolarPlugin.getInstance(), () -> {
			for (int i = 0; i < spawnMoreInt; i++) {
				event.getLocation().getWorld().spawnEntity(event.getLocation(), livingEntity.getType());
			}
		});
	}

}
