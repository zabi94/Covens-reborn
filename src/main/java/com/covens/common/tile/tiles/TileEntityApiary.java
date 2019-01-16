package com.covens.common.tile.tiles;

import java.util.ArrayList;

import com.covens.common.Covens;
import com.covens.common.item.ModItems;
import com.covens.common.lib.LibGui;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import zabi.minecraft.minerva.common.tileentity.ModTileEntity;

public class TileEntityApiary extends ModTileEntity implements ITickable {

	public static final int ROWS = 3, COLUMNS = 9;

	private int beesSlots = 0;
	private ApiaryInventory hives_inventory = new ApiaryInventory() {
		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			TileEntityApiary.this.markDirty();
			int hivesCount = 0;
			for (int i = 0; i < (COLUMNS * ROWS); i++) {
				if ((TileEntityApiary.this.hives_inventory.getStackInSlot(i).getItem() == ModItems.honeycomb) || (TileEntityApiary.this.hives_inventory.getStackInSlot(i).getItem() == ModItems.empty_honeycomb)) {
					hivesCount++;
				}
			}
			if (hivesCount != TileEntityApiary.this.beesSlots) {
				TileEntityApiary.this.beesSlots = hivesCount;
				TileEntityApiary.this.syncToClient();
			}
		}
	};

	@Override
	public void update() {
		if (!this.world.isRemote && ((this.world.getTotalWorldTime() % 80) == 0)) {
			int hivesCount = 0;
			for (int i = 0; i < (COLUMNS * ROWS); i++) {
				if (rng.nextInt(500) == 0) { // this is once every 1m14s on average: (500 chance*(80 ticks/20tps))/27 slots
					this.hives_inventory.setStackInSlot(i, this.growItem(i));
					if ((this.hives_inventory.getStackInSlot(i).getItem() == ModItems.honeycomb) || (this.hives_inventory.getStackInSlot(i).getItem() == ModItems.empty_honeycomb)) {
						hivesCount++;
					}
				}
			}
			this.markDirty();
			if (hivesCount != this.beesSlots) {
				this.beesSlots = hivesCount;
				this.syncToClient();
			}
		}
	}

	private ItemStack growItem(int i) {
		ItemStack is = this.hives_inventory.getStackInSlot(i);
		Item item = is.getItem();
		if ((item == Items.AIR) && (rng.nextInt(3) == 0) && this.getNeighbors(i).stream().anyMatch(n -> (this.hives_inventory.getStackInSlot(n).getItem() != Items.AIR))) {
			return new ItemStack(ModItems.empty_honeycomb);
		}

		if (item == ModItems.empty_honeycomb) {
			return new ItemStack(ModItems.honeycomb);
		}

		if ((item == Items.PAPER) || (item == Item.getItemFromBlock(Blocks.CARPET))) {
			return new ItemStack(ModItems.empty_honeycomb);
		}
		return is;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return ((capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, facing));
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.hives_inventory);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void onBlockBroken(World worldIn, BlockPos pos, IBlockState state) {
		this.dropInventory(this.hives_inventory);
	}

	private ArrayList<Integer> getNeighbors(int slot) {
		int i = slot % COLUMNS;
		int j = slot / COLUMNS;
		ArrayList<Integer> res = Lists.newArrayList();
		if (i > 0) {
			res.add(slot - 1);
		}
		if (i < (COLUMNS - 1)) {
			res.add(slot + 1);
		}
		if (j > 0) {
			res.add(slot - COLUMNS);
		}
		if (j < (ROWS - 1)) {
			res.add(slot + COLUMNS);
		}
		return res;
	}

	@Override
	protected void readAllModDataNBT(NBTTagCompound tag) {
		this.hives_inventory.deserializeNBT(tag.getCompoundTag("hives"));
		this.readModSyncDataNBT(tag);
	}

	@Override
	protected void writeAllModDataNBT(NBTTagCompound tag) {
		tag.setTag("hives", this.hives_inventory.serializeNBT());
		this.writeModSyncDataNBT(tag);
	}

	@Override
	protected void writeModSyncDataNBT(NBTTagCompound tag) {
		tag.setInteger("hasbees", this.beesSlots);
	}

	@Override
	protected void readModSyncDataNBT(NBTTagCompound tag) {
		this.beesSlots = tag.getInteger("hasbees");
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		playerIn.openGui(Covens.instance, LibGui.APIARY.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	public boolean hasBees() {
		return this.beesSlots > 0;
	}

	static class ApiaryInventory extends ItemStackHandler {

		public ApiaryInventory() {
			super(COLUMNS * ROWS);
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			Item i = stack.getItem();
			if ((i == Items.PAPER) || (i == Item.getItemFromBlock(Blocks.CARPET)) || (i == ModItems.empty_honeycomb)) {
				return super.insertItem(slot, stack, simulate);
			}
			return stack;
		}

		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
	}
}
