package com.ruinscraft.polar.handlers;

import org.bukkit.Material;
import org.bukkit.Sound;
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

		switch (block.getType()) {
		default:
		}

		// torches don't break/explode
		if (block.getType() == Material.TORCH ||
				block.getType() == Material.WALL_TORCH) return;
		// cobble breaks/explodes less
		if (ChanceUtil.chance(40 * c) && (block.getType() == Material.COBBLESTONE)) return;

		if (ChanceUtil.chance(2 * (1/c))) {
			blockEvent.setCancelled(true);
			block.getWorld().createExplosion(block.getLocation(), 6F * (float) Math.random(), true);
			return;
		}

		if (ChanceUtil.chance(10 * (1/Math.pow(c - .05, 5)))) {
			Material originalType = block.getType();
			long ticksUntilCrumble = (long) (200 * Math.random() * c);

			PolarPlugin.instance().getServer().getScheduler().runTaskLater(PolarPlugin.instance(), () -> {
				if (block.getType() != originalType) return;
				block.breakNaturally();
				block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ITEM_FRAME_PLACE, 1, 1);
			}, ticksUntilCrumble);

			return;
		}
	}

}