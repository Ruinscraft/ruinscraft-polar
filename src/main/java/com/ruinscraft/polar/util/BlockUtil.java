package com.ruinscraft.polar.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;

/**
 * Utility class for convenient block editing functions
 *
 */
public class BlockUtil {

	public static Block set(Block block, Material material) {
		block.setType(material, false);
		return block;
	}

	public static Block setStairAndPreserveState(Block block, Material material) {
		if (!(block.getBlockData() instanceof Stairs)) return block;
		Stairs stairs = (Stairs) block.getBlockData().clone();
		BlockFace face = stairs.getFacing();
		Half half = stairs.getHalf();
		Stairs.Shape shape = stairs.getShape();
		boolean waterlogged = stairs.isWaterlogged();
		return setStair(block, material, face, half, shape, waterlogged);
	}

	public static Block setStair(Block block, Material material, BlockFace face, Half half, Stairs.Shape shape, boolean waterlogged) {
		block.setType(material, false);
		Stairs stairs = (Stairs) block.getBlockData();
		stairs.setShape(shape);
		stairs.setFacing(face);
		stairs.setHalf(half);
		stairs.setWaterlogged(waterlogged);
		block.setBlockData(stairs);
		return block;
	}

	public static boolean removeWaterlog(Block block) {
		BlockData blockData = block.getBlockData();
		if (blockData instanceof Waterlogged) {
			Waterlogged waterlogged = (Waterlogged) blockData;
			waterlogged.setWaterlogged(false);
			BlockState state = block.getState();
			state.setBlockData(waterlogged);
			state.update(true, false);
			return true;
		}
		return false;
	}

}
