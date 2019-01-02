package com.covens.common.tile.util;

import javax.annotation.Nullable;

import com.covens.common.tile.tiles.TileEntityCauldron;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class CauldronFluidTank extends FluidTank {

	private final TileEntityCauldron tileCauldron;

	public CauldronFluidTank(TileEntityCauldron tile) {
		super(Fluid.BUCKET_VOLUME);
		this.tileCauldron = tile;
	}

	@Override
	public int fillInternal(FluidStack resource, boolean doFill) {
		int filled = super.fillInternal(resource, doFill);
		if (doFill && (filled > 0)) {
			// world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
		}
		return filled;
	}

	@Nullable
	@Override
	public FluidStack drainInternal(int maxDrain, boolean doDrain) {
		FluidStack drained = super.drainInternal(maxDrain, doDrain);
		if (doDrain && (drained != null) && (drained.amount > 0)) {
			// world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
		}
		return drained;
	}

	@Override
	protected void onContentsChanged() {
		this.tileCauldron.onLiquidChange();
	}

	@Override
	public String toString() {
		return String.format("Cauldron: %s, %d/%d", (this.fluid != null) && (this.fluid.getFluid() != null) ? this.fluid.getFluid().getName() : "Empty", this.getFluidAmount(), this.getCapacity());
	}

	public boolean hasFluid() {
		FluidStack fluid = this.getFluid();
		return (fluid != null) && (fluid.amount > 0) && (fluid.getFluid() != null);
	}

	public boolean hasFluid(Fluid other) {
		return (this.fluid != null) && (this.fluid.getFluid() == other);
	}

	@Nullable
	public Fluid getInnerFluid() {
		return this.fluid != null ? this.fluid.getFluid() : null;
	}

	public boolean isEmpty() {
		return (this.getFluid() == null) || (this.getFluid().amount <= 0);
	}

	public boolean isFull() {
		return (this.getFluid() != null) && (this.getFluid().amount == this.getCapacity());
	}
}
