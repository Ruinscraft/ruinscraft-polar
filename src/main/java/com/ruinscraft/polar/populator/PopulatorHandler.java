package com.ruinscraft.polar.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class PopulatorHandler {

	private Random random;

	public PopulatorHandler() {
		this.random = new Random();
	}

	public void handleChunk(World world, Chunk chunk) {
		if (!chunk.isLoaded()) {
			chunk.load();
		}
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 216; y++) {
					Block block = chunk.getBlock(x, y, z);

					if (chunk.getX() >= 0) handlePositiveBlock(block);
					else handleNegativeBlock(block);
				}
			}
		}
	}

	public void handlePositiveBlock(Block block) {
		// handle blocks which on the good side
		switch (block.getType()) {
		default:
			return;
		}
	}

	public void handleNegativeBlock(Block block) {
		// handle blocks which are on the bad side
		switch (block.getType()) {
		default:
			return;
		}
	}

	// chance out of 100
	public boolean chanceOutOf100(int number) {
		return getChanceOutOf(number, 100);
	}

	// chance out of any value
	// ex. 20 out of 40 == 50% chance
	public boolean getChanceOutOf(int number, int outOf) {
		int nextInt = random.nextInt(outOf);
		if (nextInt >= number) return true;
		return false;
	}

	public void replace(Block block, Material material) {
		block.setType(material, false);
	}

}
