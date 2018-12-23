package com.covens.common.tile.tiles;

import com.covens.api.mp.IMagicPowerContainer;
import com.covens.common.core.statics.ModConfig;
import com.covens.common.item.ModItems;
import com.covens.common.lib.LibIngredients;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;

import java.util.HashMap;

class AltarScanHelper {

	private static final int RADIUS = 18;
	private static final int MAX_SCORE_PER_CATEGORY = 20;
	
	private TileEntityWitchAltar te;
	private boolean upgradeCheckScheduled = false;
	private int dx = -RADIUS, dy = -RADIUS, dz = -RADIUS;
	private HashMap<Block, Integer> map = new HashMap<Block, Integer>();
	private BlockPos.MutableBlockPos checking = new BlockPos.MutableBlockPos(0, 0, 0);
	private boolean complete = false;

	public AltarScanHelper(TileEntityWitchAltar te) {
		this.te = te;
	}

	void scanNature() {
		if (upgradeCheckScheduled) {
			te.refreshUpgrades();
			upgradeCheckScheduled = false;
		}
		for (int i = 0; i < ModConfig.altar_scan_blocks_per_tick; i++) {
			getNextCycle();
			performCurrentCycle();
		}
	}

	private void performCurrentCycle() {
		updateScore();
		if (complete) {
			refreshNature();
		}
	}
	
	public void scheduleUpgradeCheck() {
		upgradeCheckScheduled = true;
	}

	private void getNextCycle() {
		complete = false;
		int radius_c = RADIUS;
		if (te.getSwordItemStack().getItem() == ModItems.boline) {
			radius_c += 2;
		}
		dx++;
		if (dx > radius_c) {
			dx = -radius_c;
			dy++;
		}
		if (dy > radius_c) {
			dy = -radius_c;
			dz++;
		}
		if (dz > radius_c) {
			dz = -radius_c;
			complete = true;
		}
		checking.setPos(te.getPos().getX() + dx, te.getPos().getY() + dy, te.getPos().getZ() + dz);
	}

	private int getPowerValue(BlockPos add) {
		IBlockState blockState = te.getWorld().getBlockState(add);
		if (blockState.getBlock().equals(Blocks.AIR)) return 0;
		if (blockState.getBlock() instanceof IPlantable) return 30;
		if (blockState.getBlock() instanceof IGrowable) return 30;
		if (blockState.getBlock().equals(Blocks.MELON_BLOCK)) return 30;
		if (blockState.getBlock().equals(Blocks.PUMPKIN)) return 30;
		ItemStack stack = new ItemStack(blockState.getBlock());
		if (!stack.isEmpty()) {
			if (LibIngredients.anyLog.apply(stack)) return 15;
			if (LibIngredients.anyLeaf.apply(stack)) return 8;
		}
		return 0;
	}

	private void updateScore() {
		int score = getPowerValue(checking);
		if (score > 0) {
			Block block = te.getWorld().getBlockState(checking).getBlock();
			int currentScore = 0;
			if (map.containsKey(block)) {
				currentScore = map.get(block);
			}
			int max_score = MAX_SCORE_PER_CATEGORY;
			if (currentScore < max_score) {
				map.put(block, currentScore + score);
			}
		}
	}

	private void refreshNature() {
		te.refreshUpgrades();
		int maxPower = map.values().parallelStream().reduce(0, (a, b) -> a + b);
		int varietyMultiplier = 40;
		if (te.getSwordItemStack().getItem() == ModItems.silver_sword) {
			varietyMultiplier = 51;
		}
		maxPower += (map.keySet().size() * varietyMultiplier); //Variety is the most important thing
		maxPower = (int) (maxPower * te.getMultiplier());
		te.getCapability(IMagicPowerContainer.CAPABILITY, null).setMaxAmount(maxPower);
		map.clear();
		te.markDirty();
	}

	public void forceFullScan() {
		dx = -RADIUS;
		dy = -RADIUS;
		dz = -RADIUS;
		complete = false;
		while (!complete) {
			getNextCycle();
			performCurrentCycle();
		}
	}
}