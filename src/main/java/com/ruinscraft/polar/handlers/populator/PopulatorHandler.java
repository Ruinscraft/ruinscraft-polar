package com.ruinscraft.polar.handlers.populator;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.ruinscraft.polar.handlers.PolarHandler;

public interface PopulatorHandler extends PolarHandler<Block> {

	void handleChunk(World world, Chunk chunk);

}
