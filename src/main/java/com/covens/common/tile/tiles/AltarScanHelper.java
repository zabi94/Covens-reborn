package com.covens.common.tile.tiles;

import java.util.HashMap;

import com.covens.api.mp.MPContainer;
import com.covens.common.core.statics.ModConfig;
import com.covens.common.item.tool.ItemBoline;
import com.covens.common.item.tool.ItemSilverSword;
import com.covens.common.lib.LibIngredients;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;

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

	public void scanNature() {
		if (this.upgradeCheckScheduled) {
			this.te.refreshUpgrades();
			this.upgradeCheckScheduled = false;
		}
		for (int i = 0; i < ModConfig.altar_scan_blocks_per_tick; i++) {
			this.getNextCycle();
			this.performCurrentCycle();
		}
	}

	private void performCurrentCycle() {
		this.updateScore();
		if (this.complete) {
			this.refreshNature();
		}
	}

	public void scheduleUpgradeCheck() {
		this.upgradeCheckScheduled = true;
	}

	private void getNextCycle() {
		this.complete = false;
		int radius_c = RADIUS;
		if (this.te.getSwordIds().contains(ItemBoline.BOLINE_UUID)) {
			radius_c += 2;
		}
		this.dx++;
		if (this.dx > radius_c) {
			this.dx = -radius_c;
			this.dy++;
		}
		if (this.dy > radius_c) {
			this.dy = -radius_c;
			this.dz++;
		}
		if (this.dz > radius_c) {
			this.dz = -radius_c;
			this.complete = true;
		}
		this.checking.setPos(this.te.getPos().getX() + this.dx, this.te.getPos().getY() + this.dy, this.te.getPos().getZ() + this.dz);
	}

	private int getPowerValue(BlockPos add) {
		IBlockState blockState = this.te.getWorld().getBlockState(add);
		if (blockState.getBlock().equals(Blocks.AIR)) {
			return 0;
		}
		if (blockState.getBlock() instanceof IPlantable) {
			return 30;
		}
		if (blockState.getBlock() instanceof IGrowable) {
			return 30;
		}
		if (blockState.getBlock().equals(Blocks.MELON_BLOCK)) {
			return 30;
		}
		if (blockState.getBlock().equals(Blocks.PUMPKIN)) {
			return 30;
		}
		ItemStack stack = new ItemStack(blockState.getBlock());
		if (!stack.isEmpty()) {
			if (LibIngredients.anyLog.apply(stack)) {
				return 15;
			}
			if (LibIngredients.anyLeaf.apply(stack)) {
				return 8;
			}
		}
		return 0;
	}

	private void updateScore() {
		int score = this.getPowerValue(this.checking);
		if (score > 0) {
			Block block = this.te.getWorld().getBlockState(this.checking).getBlock();
			int currentScore = 0;
			if (this.map.containsKey(block)) {
				currentScore = this.map.get(block);
			}
			int max_score = MAX_SCORE_PER_CATEGORY;
			if (currentScore < max_score) {
				this.map.put(block, currentScore + score);
			}
		}
	}

	private void refreshNature() {
		this.te.refreshUpgrades();
		int maxPower = this.map.values().parallelStream().reduce(0, (a, b) -> a + b);
		int varietyMultiplier = 40;
		if (this.te.getSwordIds().contains(ItemSilverSword.SILVER_UUID)) {
			varietyMultiplier = 51;
		}
		maxPower += (this.map.keySet().size() * varietyMultiplier); // Variety is the most important thing
		maxPower = (int) (maxPower * this.te.getMultiplier());
		this.te.getCapability(MPContainer.CAPABILITY, null).setMaxAmount(maxPower);
		this.map.clear();
		this.te.markDirty();
	}

	public void forceFullScan() {
		this.dx = -RADIUS;
		this.dy = -RADIUS;
		this.dz = -RADIUS;
		this.complete = false;
		while (!this.complete) {
			this.getNextCycle();
			this.performCurrentCycle();
		}
	}
}
