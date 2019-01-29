package com.ruinscraft.polar.handlers;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;

import com.ruinscraft.polar.PolarPlugin;
import com.ruinscraft.polar.util.ChanceUtil;

public class BlockPlaceHandler implements PolarHandler<BlockPlaceEvent> {

	public void handle(BlockPlaceEvent blockEvent) {
		int x = blockEvent.getBlock().getX();
		double c = PolarPlugin.instance().getChanceFromX(x);

		if (x >= 0) handlePositive(blockEvent, c);
		else handleNegative(blockEvent, c);
	}

	@Override
	public void handlePositive(BlockPlaceEvent blockEvent, double c) {
		Block block = blockEvent.getBlock();
		switch (block.getType()) {
		default:
			return;
		}
	}

	@Override
	public void handleNegative(BlockPlaceEvent blockEvent, double c) {
		Block block = blockEvent.getBlock();

		if (ChanceUtil.chance(2 * (1/c))) {
			blockEvent.setCancelled(true);
			block.getWorld().createExplosion(block.getLocation(), 4F, true);
			return;
		}

		switch (block.getType()) {
		default:
			return;
		}
	}

}