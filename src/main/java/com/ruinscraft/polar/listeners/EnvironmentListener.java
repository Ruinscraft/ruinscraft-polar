package com.ruinscraft.polar.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Golem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
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
		int x = event.getLocation().getChunk().getX();
		double chanceMultiplier = 1 - (PolarPlugin.CHANCE_CONSTANT * Math.sqrt(Math.abs(x)));

		LivingEntity livingEntity = event.getEntity();
		SpawnReason reason = event.getSpawnReason();

		if (reason == SpawnReason.CUSTOM ||
				reason == SpawnReason.REINFORCEMENTS ||
				reason == SpawnReason.JOCKEY ||
				reason == SpawnReason.VILLAGE_INVASION ||
				reason == SpawnReason.SILVERFISH_BLOCK ||
				reason == SpawnReason.MOUNT) {
			if (livingEntity instanceof Monster) {
				handleMonster((Monster) livingEntity, chanceMultiplier);
			}
			return;
		} else if (reason != SpawnReason.SPAWNER &&
				reason != SpawnReason.NATURAL && 
				reason != SpawnReason.CHUNK_GEN) {
			return;
		}

		if (x >= 0) {
			if (livingEntity instanceof Monster) {
				event.setCancelled(true);
				return;
			}
		} else if (x < 0) {
			if (livingEntity instanceof Animals ||
					livingEntity instanceof Fish ||
					livingEntity instanceof Golem ||
					livingEntity.getType() == EntityType.SLIME ||
					livingEntity.getType() == EntityType.VILLAGER) {
				event.setCancelled(true);
				return;
			}
			if (livingEntity instanceof Monster) {
				handleMonster((Monster) livingEntity, chanceMultiplier);
			}
		}

		double spawnMore;
		if (x >= 0) {
			spawnMore = Math.sqrt(PolarPlugin.CHANCE_CONSTANT * (x * 4));
		} else {
			spawnMore = Math.sqrt(PolarPlugin.CHANCE_CONSTANT * (Math.abs(x) * 8)) + (Math.random() * 3);
		}

		if (spawnMore > 10) {
			spawnMore = 10 + (spawnMore / 100);
		}

		if (ChanceUtil.chanceOutOf(1, 200)) {
			spawnMore = spawnMore + 25;
		}

		if (spawnMore > 50) spawnMore = 50;

		final int spawnMoreInt = (int) spawnMore;
		Bukkit.getScheduler().runTask(PolarPlugin.getInstance(), () -> {
			for (int i = 0; i < spawnMoreInt; i++) {
				event.getLocation().getWorld().spawnEntity(event.getLocation(), livingEntity.getType());
			}
		});
	}

	public void handleMonster(Monster monster, double c) {
		monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(monster.getAttribute
				(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * (1.5/c));
		monster.setHealth(monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		if (monster.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
			monster.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(monster.getAttribute
					(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * (1.1/(c*c)));
		}
		if (monster.getAttribute(Attribute.GENERIC_FOLLOW_RANGE) != null) {
			monster.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(monster.getAttribute
					(Attribute.GENERIC_FOLLOW_RANGE).getBaseValue() * (1.5/c));
		}
		if (monster.getAttribute(Attribute.GENERIC_ARMOR) != null) {
			monster.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(monster.getAttribute
					(Attribute.GENERIC_ARMOR).getBaseValue() * (1.5/c));
		}
		if (monster.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null) {
			monster.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(monster.getAttribute
					(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() * (1/c));
		}
		if (monster.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null) {
			monster.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(monster.getAttribute
					(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue() * (1.3/c));
		}

		if (monster.getType() == EntityType.CREEPER) {
			Creeper creeper = (Creeper) monster;
			creeper.setExplosionRadius(creeper.getExplosionRadius() + (int) (2/c));
			creeper.setMaxFuseTicks((int) (creeper.getMaxFuseTicks() * (.6 * c)));
		} else if (monster.getType() == EntityType.PHANTOM) {
			monster.getAttribute(Attribute.GENERIC_FLYING_SPEED).setBaseValue(monster.getAttribute
					(Attribute.GENERIC_FLYING_SPEED).getDefaultValue() * (1.2/c));
		} else if (monster.getType() == EntityType.HUSK) {
			monster.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(monster.getAttribute
					(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).getDefaultValue() * (5/c));
		}
	}

}
