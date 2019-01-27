package com.ruinscraft.polar.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import com.ruinscraft.polar.PolarPlugin;

public class PolarPopulator extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		PolarPlugin.instance().getPopulatorHandler().handleChunk(world, chunk);
	}

}
