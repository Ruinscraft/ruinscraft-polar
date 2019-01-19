package com.ruinscraft.polar.populator;

import static com.ruinscraft.polar.populator.ChanceUtil.*;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.ruinscraft.polar.PolarPlugin;

public class PopulatorHandler {

	public void handleChunk(World world, Chunk chunk) {
		double chanceMultiplier = 1 - (PolarPlugin.CHANCE_CONSTANT * Math.sqrt(Math.abs(chunk.getX())));
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 216; y++) { // world shouldn't go above ~216 for y, right?
					Block block = chunk.getBlock(x, y, z);

					if (chunk.getX() >= 0) {
						handlePositiveBlock(block, chanceMultiplier);
					} else {
						handleNegativeBlock(block, chanceMultiplier);
						checkIfWaterlogged(block);
						if (block.getY() == 215) block.setBiome(Biome.DESERT);
					}
				}
			}
		}
	}

	// handle blocks which on the good side
	public void handlePositiveBlock(Block block, double c) {
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
			set(block, Material.WATER);
			return;
		case TRAPPED_CHEST:
		case CHEST:
			Chest chest = (Chest) block.getState();
			Inventory inventory = chest.getInventory();
			for (int slot = 0; slot < inventory.getSize(); slot++) {
				ItemStack item = inventory.getItem(slot);
				if (item == null) continue;
				if (chance(50 * c)) item.setType(Material.COAL);
				else if (chance(50 * c)) item.setType(Material.OAK_LOG);
				else item.setType(Material.DIRT);
			}
		default:
			return;
		}
	}

	// handle blocks which are on the bad side
	public void handleNegativeBlock(Block block, double c) {
		switch (block.getType()) {
		case DARK_OAK_LEAVES:
		case ACACIA_LEAVES:
		case SPRUCE_LEAVES:
		case BIRCH_LEAVES:
		case JUNGLE_LEAVES:
			set(block, Material.OAK_LEAVES);
		case OAK_LEAVES:
			if (chance(85 * (1/c))) set(block, Material.AIR);
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
		case SEAGRASS:
		case TALL_SEAGRASS:
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
		case TUBE_CORAL:
		case BRAIN_CORAL:
		case BUBBLE_CORAL:
		case FIRE_CORAL:
		case HORN_CORAL:
		case TUBE_CORAL_BLOCK:
		case BRAIN_CORAL_BLOCK:
		case BUBBLE_CORAL_BLOCK:
		case FIRE_CORAL_BLOCK:
		case HORN_CORAL_BLOCK:
		case TUBE_CORAL_FAN:
		case BRAIN_CORAL_FAN:
		case BUBBLE_CORAL_FAN:
		case FIRE_CORAL_FAN:
		case HORN_CORAL_FAN:
		case TUBE_CORAL_WALL_FAN:
		case BRAIN_CORAL_WALL_FAN:
		case BUBBLE_CORAL_WALL_FAN:
		case FIRE_CORAL_WALL_FAN:
		case HORN_CORAL_WALL_FAN:
		case LILY_PAD:
		case SEA_PICKLE:
		case KELP_PLANT:
		case KELP:
			set(block, Material.AIR);
			return;
		case GRAVEL:
		case CLAY:
		case SAND:
			if (block.getY() > 50) {
				set(block, Material.DIRT);
			} else if (block.getY() > 45) {
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
			setAndPreserveStair(block, Material.RED_SANDSTONE_STAIRS);
			return;
		case SANDSTONE_SLAB:
			set(block, Material.RED_SANDSTONE_SLAB);
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
			set(block, Material.STONE);
			return;
		case TRAPPED_CHEST:
		case CHEST:
			Chest chest = (Chest) block.getState();
			Inventory inventory = chest.getInventory();
			for (int slot = 0; slot < inventory.getSize(); slot++) {
				ItemStack item = inventory.getItem(slot);
				if (item == null) continue;
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
		case WATER:
			if (block.getY() < 64) set(block, Material.AIR);
			else set(block, Material.LAVA);
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
			if (block.getY() < 14) {
				if (chanceOutOf(1 * (1/c), 15000)) set(block, Material.GOLD_BLOCK);
				if (chanceOutOf(1 * (1/c), 240)) set(block, Material.DIAMOND_ORE);
				if (chanceOutOf(1 * (1/c), 160)) set(block, Material.GOLD_ORE);
				if (chanceOutOf(1 * (1/c), 300)) set(block, Material.REDSTONE_ORE);
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
				if (chanceOutOf(1 * (1/c), 60)) set(block, Material.IRON_ORE);
			}
			if (chance(2 * c)) set(block, Material.INFESTED_STONE);
			return;
		default:
			return;
		}
	}

	// sets the Material for the block
	public void set(Block block, Material material) {
		block.setType(material, false);
	}

	public void setAndPreserveStair(Block block, Material material) {
		Stairs stairs = (Stairs) block.getBlockData().clone();
		BlockFace face = stairs.getFacing();
		Half half = stairs.getHalf();
		Stairs.Shape shape = stairs.getShape();
		block.setType(material);
		Stairs updatedStairs = (Stairs) block.getBlockData();
		updatedStairs.setShape(shape);
		updatedStairs.setFacing(face);
		updatedStairs.setHalf(half);
		block.setBlockData(updatedStairs);
	}

	public void checkIfWaterlogged(Block block) {
		switch (block.getType()) {
		case WALL_SIGN:
		case OAK_TRAPDOOR:
		case ACACIA_TRAPDOOR:
		case BIRCH_TRAPDOOR:
		case SPRUCE_TRAPDOOR:
		case DARK_OAK_TRAPDOOR:
		case JUNGLE_TRAPDOOR:
		case PURPUR_STAIRS:
		case OAK_STAIRS:
		case COBBLESTONE_STAIRS:
		case BRICK_STAIRS:
		case STONE_BRICK_STAIRS:
		case NETHER_BRICK_STAIRS:
		case SANDSTONE_STAIRS:
		case SPRUCE_STAIRS:
		case BIRCH_STAIRS:
		case JUNGLE_STAIRS:
		case QUARTZ_STAIRS:
		case ACACIA_STAIRS:
		case DARK_OAK_STAIRS:
		case PRISMARINE_STAIRS:
		case PRISMARINE_BRICK_STAIRS:
		case DARK_PRISMARINE_STAIRS:
		case OAK_SLAB:
		case SPRUCE_SLAB:
		case BIRCH_SLAB:
		case JUNGLE_SLAB:
		case ACACIA_SLAB:
		case DARK_OAK_SLAB:
		case STONE_SLAB:
		case SANDSTONE_SLAB:
		case PETRIFIED_OAK_SLAB:
		case COBBLESTONE_SLAB:
		case BRICK_SLAB:
		case STONE_BRICK_SLAB:
		case NETHER_BRICK_SLAB:
		case QUARTZ_SLAB:
		case RED_SANDSTONE_SLAB:
		case PURPUR_SLAB:
		case PRISMARINE_SLAB:
		case PRISMARINE_BRICK_SLAB:
		case DARK_PRISMARINE_SLAB:
		case SIGN:
		case SEA_PICKLE:
		case LADDER:
		case GLASS_PANE:
		case WHITE_STAINED_GLASS_PANE:
		case ORANGE_STAINED_GLASS_PANE:
		case MAGENTA_STAINED_GLASS_PANE:
		case LIGHT_BLUE_STAINED_GLASS_PANE:
		case YELLOW_STAINED_GLASS_PANE:
		case LIME_STAINED_GLASS_PANE:
		case PINK_STAINED_GLASS_PANE:
		case GRAY_STAINED_GLASS_PANE:
		case LIGHT_GRAY_STAINED_GLASS_PANE:
		case CYAN_STAINED_GLASS_PANE:
		case PURPLE_STAINED_GLASS_PANE:
		case BLUE_STAINED_GLASS_PANE:
		case BROWN_STAINED_GLASS_PANE:
		case GREEN_STAINED_GLASS_PANE:
		case RED_STAINED_GLASS_PANE:
		case BLACK_STAINED_GLASS_PANE:
		case OAK_FENCE:
		case OAK_FENCE_GATE:
		case ACACIA_FENCE:
		case ACACIA_FENCE_GATE:
		case SPRUCE_FENCE:
		case SPRUCE_FENCE_GATE:
		case BIRCH_FENCE:
		case BIRCH_FENCE_GATE:
		case JUNGLE_FENCE:
		case JUNGLE_FENCE_GATE:
		case DARK_OAK_FENCE:
		case DARK_OAK_FENCE_GATE:
		case NETHER_BRICK_FENCE:
		case COBBLESTONE_WALL:
		case MOSSY_COBBLESTONE_WALL:
		case IRON_BARS:
		case ENDER_CHEST:
		case TUBE_CORAL_FAN:
		case BRAIN_CORAL_FAN:
		case BUBBLE_CORAL_FAN:
		case FIRE_CORAL_FAN:
		case HORN_CORAL_FAN:
		case DEAD_TUBE_CORAL_FAN:
		case DEAD_BRAIN_CORAL_FAN:
		case DEAD_BUBBLE_CORAL_FAN:
		case DEAD_FIRE_CORAL_FAN:
		case DEAD_HORN_CORAL_FAN:
		case TUBE_CORAL_WALL_FAN:
		case BRAIN_CORAL_WALL_FAN:
		case BUBBLE_CORAL_WALL_FAN:
		case FIRE_CORAL_WALL_FAN:
		case HORN_CORAL_WALL_FAN:
		case CHEST:
		case TRAPPED_CHEST:
			BlockData blockData = block.getBlockData();
			if (blockData instanceof Waterlogged) {
				Waterlogged waterlogged = (Waterlogged) blockData;
				waterlogged.setWaterlogged(false);
				BlockState state = block.getState();
				state.setBlockData(waterlogged);
				state.update(true, false);
			}
		default:
			return;
		}
	}

}
