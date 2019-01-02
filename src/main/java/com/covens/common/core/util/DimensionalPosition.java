package com.covens.common.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class DimensionalPosition {

	private int dim, x, y, z;

	public DimensionalPosition(int xIn, int yIn, int zIn, int dimension) {
		this.x = xIn;
		this.y = yIn;
		this.z = zIn;
		this.dim = dimension;
	}

	public DimensionalPosition(NBTTagCompound tag) {
		this(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"), tag.getInteger("d"));
	}

	public DimensionalPosition(BlockPos pos, int dimension) {
		this(pos.getX(), pos.getY(), pos.getZ(), dimension);
	}

	public DimensionalPosition(Entity entity) {
		this(entity.getPosition(), entity.world.provider.getDimension());
	}

	public DimensionalPosition(TileEntity entity) {
		this(entity.getPos(), entity.getWorld().provider.getDimension());
	}

	public BlockPos getPosition() {
		return new BlockPos(this.getX(), this.getY(), this.getZ());
	}

	public int getX() {
		return this.x;
	}

	public int getDim() {
		return this.dim;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public double getDistanceSqFrom(DimensionalPosition pos) {
		if (pos.getDim() != this.getDim()) {
			return Double.POSITIVE_INFINITY;
		}
		int diffx = this.getX() - pos.getX();
		int diffy = this.getY() - pos.getY();
		int diffz = this.getZ() - pos.getZ();

		return (diffx * diffx) + (diffy * diffy) + (diffz * diffz);
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("x", this.x);
		tag.setInteger("y", this.y);
		tag.setInteger("z", this.z);
		tag.setInteger("d", this.dim);
		return tag;
	}
}
