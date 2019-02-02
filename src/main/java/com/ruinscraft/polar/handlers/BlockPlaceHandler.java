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

		if (ChanceUtil.chance(2 * (1/c))) {
			if (ChanceUtil.chance(30 * c) && block.getType() == Material.COBBLESTONE) return;
			blockEvent.setCancelled(true);
			block.getWorld().createExplosion(block.getLocation(), 8F * (float) Math.random(), true);

			return;
		}

		if (ChanceUtil.chance(10 * (1/Math.pow(c - .05, 5)))) {
			long ticksUntilCrumble = (long) (200 * Math.random() * c);
			PolarPlugin.instance().getServer().getScheduler().runTaskLater(PolarPlugin.instance(), () -> {
				block.breakNaturally();
				block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ITEM_FRAME_PLACE, 1, 1);
			}, ticksUntilCrumble);

			return;
		}

		switch (block.getType()) {
		default:
			return;
		}
	}

}