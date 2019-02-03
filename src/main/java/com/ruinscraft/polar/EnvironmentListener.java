package com.ruinscraft.polar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Golem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.ItemStack;

import com.ruinscraft.polar.handlers.playerstatus.OverworldPlayerStatusHandler;
import com.ruinscraft.polar.handlers.populator.OverworldPopulatorHandler;
import com.ruinscraft.polar.util.ChanceUtil;
import com.ruinscraft.polar.util.PolarUtil;

public class EnvironmentListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onWorldInit(WorldInitEvent event) {
		World world = event.getWorld();

		if (world.getEnvironment() == Environment.NORMAL) {
			world.getPopulators().add(new OverworldPopulatorHandler());
			PolarPlugin.instance().getServer().getScheduler().runTaskTimer(
					PolarPlugin.instance(), new OverworldPlayerStatusHandler(event.getWorld()), 0, 100);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPlayedBefore()) return;

		ItemStack compass = new ItemStack(Material.COMPASS);
		PolarUtil.setCompass(compass, player.getLocation().getBlockX());
		player.getInventory().setItem(8, compass);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerItemDamage(PlayerItemDamageEvent event) {
		int x = event.getPlayer().getLocation().getBlockX();
		double c = .7 - Math.abs(PolarPlugin.CHANCE_CONSTANT * (x/30));
		if (c < .08) c = .08;
		if (x >= 0) return;

		int damage = event.getDamage() * (int) (1 / c);
		event.setDamage(damage);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Block block = event.getClickedBlock();
		if (block.getLocation().getBlockX() > 0) return;
		if (event.getItem() == null) return;
		if (event.getItem().getType() == Material.WATER_BUCKET) {
			event.setCancelled(true);
			block.getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_BURN, 1, 1);
			block.getWorld().spawnParticle(Particle.SMOKE_LARGE, block.getLocation(), 3);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent event) {
		int x = event.getEntity().getLocation().getBlockX();
		if (x < 0) return;
		if (event.getCause() == DamageCause.STARVATION) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		PolarPlugin.instance().getBlockPlaceHandler().handle(event);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		PolarPlugin.instance().getBlockBrokenHandler().handle(event);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
			return;
		}
		
		int damagerX = event.getDamager().getLocation().getBlockX();
		int hurtX = event.getEntity().getLocation().getBlockX();
		if (damagerX >= 0 && hurtX >= 0) {
			event.setCancelled(true);
			event.getDamager().sendMessage(ChatColor.RED + "Hey, can't hit here!");
			return;
		} else if (hurtX >= 0 && damagerX < 0 || damagerX >= 0 && hurtX < 0) {
			event.setCancelled(true);
			event.getDamager().sendMessage(ChatColor.RED + "Hey, can't hit over there!");
			return;
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.getEntityType() == EntityType.PRIMED_TNT) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		int x = event.getLocation().getChunk().getX();
		double chanceMultiplier = PolarPlugin.instance().getChanceFromX(x);

		LivingEntity livingEntity = event.getEntity();
		SpawnReason reason = event.getSpawnReason();

		if (reason == SpawnReason.CUSTOM ||
				reason == SpawnReason.REINFORCEMENTS ||
				reason == SpawnReason.JOCKEY ||
				reason == SpawnReason.VILLAGE_INVASION ||
				reason == SpawnReason.MOUNT) {
			if (livingEntity instanceof Monster) {
				handleMonster((Monster) livingEntity, chanceMultiplier);
			}
			return;
		} else if (reason != SpawnReason.SPAWNER &&
				reason != SpawnReason.NATURAL &&
				reason != SpawnReason.CHUNK_GEN &&
				reason != SpawnReason.SILVERFISH_BLOCK) {
			return;
		}

		if (x >= 0) {
			if (livingEntity instanceof Monster ||
					livingEntity.getType() == EntityType.PHANTOM) {
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
			spawnMore = Math.sqrt(PolarPlugin.CHANCE_CONSTANT * (Math.abs(x) * 6)) + (Math.random() * 3);
		}

		if (spawnMore > 10) {
			spawnMore = 10 + (spawnMore / 100);
		}

		if (ChanceUtil.chanceOutOf(1, 200)) {
			spawnMore = spawnMore + 25;
		}

		if (spawnMore > 50) spawnMore = 50;

		final int spawnMoreInt = (int) spawnMore;
		Bukkit.getScheduler().runTask(PolarPlugin.instance(), () -> {
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
					(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * (1.4/(c*c)));
		}
		if (monster.getAttribute(Attribute.GENERIC_FOLLOW_RANGE) != null) {
			monster.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(monster.getAttribute
					(Attribute.GENERIC_FOLLOW_RANGE).getBaseValue() * (1.7/c));
		}
		if (monster.getAttribute(Attribute.GENERIC_ARMOR) != null) {
			monster.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(monster.getAttribute
					(Attribute.GENERIC_ARMOR).getBaseValue() * (1.5/c));
		}
		if (monster.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null) {
			monster.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(monster.getAttribute
					(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() * (1.2/c));
		}
		if (monster.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null) {
			monster.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(monster.getAttribute
					(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue() * (1.3/c));
		}

		if (monster.getType() == EntityType.CREEPER) {
			Creeper creeper = (Creeper) monster;
			creeper.setExplosionRadius(creeper.getExplosionRadius() + (int) (2/c));
			creeper.setMaxFuseTicks((int) (creeper.getMaxFuseTicks() * (.5 * c)));
		} else if (monster.getType() == EntityType.PHANTOM) {
			monster.getAttribute(Attribute.GENERIC_FLYING_SPEED).setBaseValue(monster.getAttribute
					(Attribute.GENERIC_FLYING_SPEED).getDefaultValue() * (1.2/c));
		} else if (monster.getType() == EntityType.HUSK || monster.getType() == EntityType.ZOMBIE) {
			monster.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(monster.getAttribute
					(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).getDefaultValue() * (5/c));
		}
	}

}
