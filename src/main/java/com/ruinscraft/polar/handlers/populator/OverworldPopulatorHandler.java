package com.ruinscraft.polar.handlers.populator;

import static com.ruinscraft.polar.util.ChanceUtil.*;
import static com.ruinscraft.polar.util.BlockUtil.*;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.ruinscraft.polar.PolarPlugin;

public class OverworldPopulatorHandler extends BlockPopulator implements PopulatorHandler {

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		handleChunk(world, chunk);
	}

	private Material[] goodFoodItems = new Material[] {
			Material.WHEAT, Material.APPLE, Material.MUSHROOM_STEW, Material.BREAD, Material.PORKCHOP, Material.COOKED_PORKCHOP,
			Material.GOLDEN_APPLE, Material.ENCHANTED_GOLDEN_APPLE, Material.COD, Material.SALMON, Material.TROPICAL_FISH,
			Material.PUFFERFISH, Material.COOKED_COD, Material.COOKED_SALMON, Material.CAKE, Material.COOKIE, Material.MELON_SLICE,
			Material.DRIED_KELP, Material.BEEF, Material.COOKED_BEEF, Material.CHICKEN, Material.COOKED_CHICKEN,
			Material.CARROT, Material.POTATO, Material.BAKED_POTATO, Material.PUMPKIN_PIE, Material.RABBIT, Material.COOKED_RABBIT,
			Material.RABBIT_STEW, Material.MUTTON, Material.COOKED_MUTTON, Material.BEETROOT, Material.BEETROOT_SOUP};

	@Override
	public void handleChunk(World world, Chunk chunk) {
		double chanceMultiplier = PolarPlugin.instance().getChanceFromX(chunk.getX());
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 256; y++) { // world shouldn't go above ~216 for y, right?
					Block block = chunk.getBlock(x, y, z);

					if (chunk.getX() >= 0) {
						handlePositive(block, chanceMultiplier);
					} else {
						handleNegative(block, chanceMultiplier);
						removeWaterlog(block);
						if (block.getY() == 255) block.setBiome(Biome.DESERT);
					}
				}
			}
		}
	}

	// handle blocks which are on the good side
	@Override
	public void handlePositive(Block block, double c) {
		switch (block.getType()) {
		case DIAMOND_ORE:
		case GOLD_ORE:
		case REDSTONE_ORE:
		case EMERALD_ORE:
		case LAPIS_ORE:
			if (chance(50 * c)) set(block, Material.COAL_ORE); 
			else set(block, Material.STONE);
			return;
		case IRON_ORE:
			if (chance(85 * c)) set(block, Material.COAL_ORE);
			else set(block, Material.STONE);
			return;
		case LAVA:
		case OBSIDIAN:
		case MAGMA_BLOCK:
			set(block, Material.WATER);
			return;
		case GOLD_BLOCK:
			set(block, Material.COAL_BLOCK);
			return;
		case SPAWNER:
			CreatureSpawner spawner = (CreatureSpawner) block.getState();
			spawner.setMaxSpawnDelay(Integer.MAX_VALUE);
			spawner.setDelay(Integer.MAX_VALUE);
			spawner.update(true, false);
			return;
		case TRAPPED_CHEST:
		case CHEST:
			Chest chest = (Chest) block.getState();
			Inventory inventory = chest.getInventory();
			for (int slot = 0; slot < inventory.getSize(); slot++) {
				ItemStack item = inventory.getItem(slot);
				if (item == null) continue;

				boolean continueBecauseFood = false;
				for (Material food : goodFoodItems) {
					if (item.getType() == food) continueBecauseFood = true;
				}
				if (continueBecauseFood) continue;

				inventory.clear(slot);

				ItemStack newItem = new ItemStack(Material.COAL);
				newItem.setAmount(item.getAmount());

				if (chance(50 * c)) newItem.setType(Material.OAK_LOG);
				else if (chance(30 * c)) newItem.setType(Material.OAK_PLANKS);
				else if (chance(90 * c)) newItem.setType(Material.DIRT);
				else newItem.setType(Material.IRON_INGOT);

				inventory.setItem(slot, newItem);
			}
		default:
			return;
		}
	}

	// handle blocks which are on the bad side
	@Override
	public void handleNegative(Block block, double c) {
		switch (block.getType()) {
		case DARK_OAK_LEAVES:
		case ACACIA_LEAVES:
		case SPRUCE_LEAVES:
		case BIRCH_LEAVES:
		case JUNGLE_LEAVES:
		case OAK_LEAVES:
			set(block, Material.AIR);
			return;
		case GRASS:
		case TALL_GRASS:
		case FERN:
		case LARGE_FERN:
		case SNOW:
		case ICE:
		case BLUE_ICE:
		case FROSTED_ICE:
		case PACKED_ICE:
		case VINE:
		case SUGAR_CANE:
		case CACTUS:
		case MELON:
		case PUMPKIN:
		case SUNFLOWER:
		case AZURE_BLUET:
		case DANDELION:
		case POPPY:
		case ALLIUM:
		case BLUE_ORCHID:
		case RED_TULIP:
		case ORANGE_TULIP:
		case WHITE_TULIP:
		case PINK_TULIP:
		case OXEYE_DAISY:
		case PEONY:
		case LILAC:
		case ROSE_BUSH:
		case LILY_PAD:
		case POTATOES:
		case CARROTS:
		case BEETROOTS:
		case WHEAT:
			set(block, Material.AIR);
			return;
		case TUBE_CORAL:
			set(block, Material.DEAD_TUBE_CORAL);
			return;
		case BRAIN_CORAL:
			set(block, Material.DEAD_BRAIN_CORAL);
			return;
		case BUBBLE_CORAL:
			set(block, Material.DEAD_BUBBLE_CORAL);
			return;
		case FIRE_CORAL:
			set(block, Material.DEAD_FIRE_CORAL);
			return;
		case HORN_CORAL:
			set(block, Material.DEAD_HORN_CORAL);
			return;
		case TUBE_CORAL_BLOCK:
			set(block, Material.DEAD_TUBE_CORAL_BLOCK);
			return;
		case BRAIN_CORAL_BLOCK:
			set(block, Material.DEAD_BRAIN_CORAL_BLOCK);
			return;
		case BUBBLE_CORAL_BLOCK:
			set(block, Material.DEAD_BUBBLE_CORAL_BLOCK);
			return;
		case FIRE_CORAL_BLOCK:
			set(block, Material.DEAD_FIRE_CORAL_BLOCK);
			return;
		case HORN_CORAL_BLOCK:
			set(block, Material.DEAD_HORN_CORAL_BLOCK);
			return;
		case TUBE_CORAL_FAN:
			set(block, Material.DEAD_TUBE_CORAL_FAN);
			return;
		case BRAIN_CORAL_FAN:
			set(block, Material.DEAD_BRAIN_CORAL_FAN);
			return;
		case BUBBLE_CORAL_FAN:
			set(block, Material.DEAD_BUBBLE_CORAL_FAN);
			return;
		case FIRE_CORAL_FAN:
			set(block, Material.DEAD_FIRE_CORAL_FAN);
			return;
		case HORN_CORAL_FAN:
			set(block, Material.DEAD_HORN_CORAL_FAN);
			return;
		case TUBE_CORAL_WALL_FAN:
			set(block, Material.DEAD_TUBE_CORAL_WALL_FAN);
			return;
		case BRAIN_CORAL_WALL_FAN:
			set(block, Material.DEAD_BRAIN_CORAL_WALL_FAN);
			return;
		case BUBBLE_CORAL_WALL_FAN:
			set(block, Material.DEAD_BUBBLE_CORAL_WALL_FAN);
			return;
		case FIRE_CORAL_WALL_FAN:
			set(block, Material.DEAD_FIRE_CORAL_WALL_FAN);
			return;
		case HORN_CORAL_WALL_FAN:
			set(block, Material.DEAD_HORN_CORAL_WALL_FAN);
			return;
		case MYCELIUM:
			if (chance(50) && block.getY() > 66) return;
			else if (chance(25) && block.getY() > 63 && block.getY() <= 66) return;
			else if (chance(15) && block.getY() > 61 && block.getY() <= 63) return;
			else set(block, Material.GRASS_BLOCK);
		case GRAVEL:
		case CLAY:
		case SAND:
			if (block.getY() > 50 && block.getType() != Material.GRASS_BLOCK) {
				set(block, Material.DIRT);
			} else if (block.getY() > 45 && block.getType() != Material.GRASS_BLOCK) {
				if (chance(50)) set(block, Material.DIRT);
			}
		case GRASS_BLOCK:
		case DIRT:
		case GRASS_PATH:
		case PODZOL:
		case COARSE_DIRT:
			if (chance(50)) {
				set(block, Material.STONE);
			} else if (chance(40)) {
				set(block, Material.ANDESITE);
			} else {
				set(block, Material.COBBLESTONE);
			}
			return;
		case FARMLAND:
			if (chance(80)) set(block, Material.COAL_ORE);
			else set(block, Material.STONE);
			return;
		case RED_MUSHROOM_BLOCK:
		case BROWN_MUSHROOM_BLOCK:
		case MUSHROOM_STEM:
		case OAK_LOG:
		case DARK_OAK_LOG:
		case ACACIA_LOG:
		case SPRUCE_LOG:
		case BIRCH_LOG:
		case JUNGLE_LOG:
			if (chance(75 * (1/c))) set(block, Material.COBBLESTONE);
			if (chance(40 * (1/c))) set(block, Material.MOSSY_COBBLESTONE);
			return;
		case SANDSTONE:
			set(block, Material.RED_SANDSTONE);
			return;
		case CHISELED_SANDSTONE:
			set(block, Material.CHISELED_RED_SANDSTONE);
			return;
		case CUT_SANDSTONE:
			set(block, Material.CUT_RED_SANDSTONE);
			return;
		case SANDSTONE_STAIRS:
			setStairAndPreserveState(block, Material.RED_SANDSTONE_STAIRS);
			return;
		case SANDSTONE_SLAB:
			set(block, Material.RED_SANDSTONE_SLAB);
			return;
		case STONE_BRICK_STAIRS:
		case BRICK_STAIRS:
		case COBBLESTONE_STAIRS:
		case OAK_STAIRS:
		case DARK_OAK_STAIRS:
		case ACACIA_STAIRS:
		case QUARTZ_STAIRS:
		case JUNGLE_STAIRS:
		case BIRCH_STAIRS:
		case SPRUCE_STAIRS:
		case DARK_PRISMARINE_STAIRS:
		case PRISMARINE_BRICK_STAIRS:
		case PRISMARINE_STAIRS:
			if (chance(50)) setStairAndPreserveState(block, Material.COBBLESTONE_STAIRS);
			else if (chance(40)) setStairAndPreserveState(block, Material.STONE_BRICK_STAIRS);
			return;
		case OAK_PLANKS:
		case DARK_OAK_PLANKS:
		case ACACIA_PLANKS:
		case BIRCH_PLANKS:
		case JUNGLE_PLANKS:
		case SPRUCE_PLANKS:
			if (chance(6 * (1/c)) && block.getY() > 63) set(block, Material.FIRE);
			else if (chance(2 * (1/c)) && block.getY() > 30) set(block, Material.FIRE);
			else if (chance(60 * (1/c))) set(block, Material.AIR);
			else if (chance(60 * (1/c))) set(block, Material.STONE);
			else if (chance(60 * (1/c))) set(block, Material.COBBLESTONE);
			return;
		case SNOW_BLOCK:
			Biome biome = block.getBiome();
			if (biome == Biome.FROZEN_OCEAN || 
					biome == Biome.DEEP_FROZEN_OCEAN) {
				set(block, Material.AIR);
			} else set(block, Material.STONE);
			return;
		case TRAPPED_CHEST:
		case CHEST:
			Chest chest = (Chest) block.getState();
			Inventory inventory = chest.getInventory();
			for (int slot = 0; slot < inventory.getSize(); slot++) {
				ItemStack item = inventory.getItem(slot);
				if (item == null) continue;
				boolean isFood = false;
				for (Material food : this.goodFoodItems) {
					if (item.getType() == food) {
						isFood = true;
						break;
					}
				}
				if (isFood == true) item.setType(Material.IRON_INGOT);
				if (item.getMaxStackSize() == 1) continue;
				int randomInt = (int) (PolarPlugin.random().nextDouble() * (7 * (1/c)));
				item.setAmount(item.getAmount() + randomInt);
			}
			return;
		case PRISMARINE:
			if (chance(70 * (1/c))) set(block, Material.STONE);
			else if (chance(50 * (1/c))) set(block, Material.COBBLESTONE);
			else if (chance(10 * (1/c))) set(block, Material.GRAVEL);
			else if (chance(10 * (1/c))) set(block, Material.AIR);
			else if (chance(10 * (1/c))) set(block, Material.DIAMOND_ORE);
			return;
		case PRISMARINE_BRICKS:
			if (chance(80 * (1/c))) set(block, Material.STONE_BRICKS);
			else if (chance(60 * (1/c))) set(block, Material.POLISHED_ANDESITE);
			else if (chance(10 * (1/c))) set(block, Material.AIR);
			return;
		case SEA_LANTERN:
			if (chance(30 * (1/c))) set(block, Material.AIR);
			else if (chance(70 * (1/c))) set(block, Material.GLOWSTONE);
			else if (chance(20 * (1/c))) set(block, Material.GOLD_BLOCK);
			else set(block, Material.AIR);
			return;
		case DARK_PRISMARINE:
			set(block, Material.COAL_ORE);
			return;
		case WET_SPONGE:
			if (chance(50 * c)) set(block, Material.SPONGE);
			else set(block, Material.INFESTED_STONE);
			return;
		case GOLD_BLOCK:
			set(block, Material.DIAMOND_BLOCK);
			return;
		case SEAGRASS:
		case TALL_SEAGRASS:
		case SEA_PICKLE:
		case KELP_PLANT:
		case KELP:
			if (block.getX() == -1) {
				set(block, Material.WATER);
			} else {
				set(block, Material.AIR);
				return;
			}
		case WATER:
			if (block.getY() < 64) {
				if (block.getX() == -1) {
					if (block.getY() == 62) {
						setStair(block, Material.COBBLESTONE_STAIRS, BlockFace.EAST, 
								Bisected.Half.BOTTOM, Stairs.Shape.STRAIGHT, true);
						return;
					}
					if (chance(80)) set(block, Material.STONE);
					else set(block, Material.ANDESITE);
					return;
				} else set(block, Material.AIR);
			}
			else set(block, Material.LAVA);
			return;
		case LAVA:
			if (block.getY() < 64) {
				if (block.getX() == -1) {
					if (block.getY() == 62) {
						setStair(block, Material.COBBLESTONE_STAIRS, BlockFace.WEST, 
								Bisected.Half.BOTTOM, Stairs.Shape.STRAIGHT, true);
						return;
					}
					if (chance(80)) set(block, Material.STONE);
					else set(block, Material.ANDESITE);
					return;
				}
			}
			return;
		case COBBLESTONE:
			if (chance(25 * (1/c))) set(block, Material.MOSSY_COBBLESTONE);
			if (chance(10 * (1/c))) set(block, Material.INFESTED_COBBLESTONE);
			return;
		case COBBLESTONE_WALL:
			if (chance(25 * (1/c))) set(block, Material.MOSSY_COBBLESTONE_WALL);
			return;
		case STONE_BRICKS:
			if (chance(25 * (1/c))) set(block, Material.MOSSY_STONE_BRICKS);
			if (chance(10 * (1/c))) set(block, Material.INFESTED_STONE_BRICKS);
			if (chance(7 * (1/c))) set(block, Material.INFESTED_MOSSY_STONE_BRICKS);
			return;
		case STONE:
			if (block.getY() < 16) {
				if (chanceOutOf(1 * (1/(c*c)), 15000)) set(block, Material.GOLD_BLOCK);
				if (chanceOutOf(1 * (1/(c*c)), 180)) set(block, Material.DIAMOND_ORE);
				if (chanceOutOf(1 * (1/(c*c)), 160)) set(block, Material.GOLD_ORE);
				if (chanceOutOf(1 * (1/(c*c)), 280)) set(block, Material.REDSTONE_ORE);
			}
			if (block.getY() < 34) {
				if (chanceOutOf(1 * (1/c), 120)) set(block, Material.GOLD_ORE);
				if (block.getBiome() == Biome.BADLANDS) {
					if (chanceOutOf(1 * (1/c), 50)) set(block, Material.GOLD_ORE);
				}
				if (chanceOutOf(1 * (1/c), 500)) set(block, Material.LAPIS_ORE);
				if (block.getBiome() == Biome.MOUNTAINS ||
						block.getBiome() == Biome.GRAVELLY_MOUNTAINS ||
						block.getBiome() == Biome.MODIFIED_GRAVELLY_MOUNTAINS ||
						block.getBiome() == Biome.WOODED_MOUNTAINS ||
						block.getBiome() == Biome.MOUNTAIN_EDGE) {
					if (chanceOutOf(1 * (1/c), 50)) set(block, Material.EMERALD_ORE);
				}
			}
			if (block.getY() < 63) {
				if (chanceOutOf(1 * (1/c), 53)) set(block, Material.IRON_ORE);
			}
			if (chance(2 * (1/c))) set(block, Material.INFESTED_STONE);
			return;
		default:
			return;
		}
	}

}
