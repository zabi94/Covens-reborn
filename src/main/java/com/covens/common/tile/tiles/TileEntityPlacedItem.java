package com.covens.common.tile.tiles;

import com.covens.common.tile.ModTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPlacedItem extends ModTileEntity {

	private ItemStack stack = ItemStack.EMPTY;

	public void setItem(ItemStack itemstack) {
		this.stack = itemstack;
		this.markDirty();
		this.syncToClient();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return true;
	}

	public ItemStack getItem() {
		return this.stack.copy();
	}

	public ItemStack pop() {
		ItemStack stackOut = this.stack.copy();
		this.world.destroyBlock(this.pos, false);
		return stackOut;
	}

	@Override
	protected void readAllModDataNBT(NBTTagCompound tag) {
		this.readModSyncDataNBT(tag);
	}

	@Override
	protected void writeAllModDataNBT(NBTTagCompound tag) {
		this.writeModSyncDataNBT(tag);
	}

	@Override
	protected void writeModSyncDataNBT(NBTTagCompound tag) {
		NBTTagCompound nbt = new NBTTagCompound();
		this.stack.writeToNBT(nbt);
		tag.setTag("item", nbt);
	}

	@Override
	protected void readModSyncDataNBT(NBTTagCompound tag) {
		this.stack = new ItemStack(tag.getCompoundTag("item"));
	}
}
