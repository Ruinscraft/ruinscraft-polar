package com.ruinscraft.polar.handlers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

import com.ruinscraft.polar.PolarPlugin;

public class BlockBreakHandler implements PolarHandler<BlockBreakEvent> {

	public void handle(BlockBreakEvent blockEvent) {
		int x = blockEvent.getBlock().getX();
		double c = PolarPlugin.instance().getChanceFromX(x);

		if (x >= 0) handlePositive(blockEvent, c);
		else handleNegative(blockEvent, c);
	}

	@Override
	public void handlePositive(BlockBreakEvent blockEvent, double c) {
		Block block = blockEvent.getBlock();
		switch (block.getType()) {
		default:
			return;
		}
	}

	@Override
	public void handleNegative(BlockBreakEvent blockEvent, double c) {
		Block block = blockEvent.getBlock();
		switch (block.getType()) {
		case COAL_ORE:
			blockEvent.setDropItems(false);
			block.setType(Material.AIR);
			block.getWorld().createExplosion(block.getLocation(), 3F * (float) (2/c), true);
			return;
		case COAL_BLOCK:
			blockEvent.setDropItems(false);
			block.setType(Material.AIR);
			block.getWorld().createExplosion(block.getLocation(), 6F * (float) (2/c), true);
			return;
		default:
			return;
		}
	}

}
