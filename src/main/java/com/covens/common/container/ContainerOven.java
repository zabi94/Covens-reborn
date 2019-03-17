package com.covens.common.container;

import com.covens.common.block.natural.tree.BlockModSapling;
import com.covens.common.item.ModItems;
import com.covens.common.tile.tiles.TileEntityOven;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import zabi.minecraft.minerva.common.network.container.ModContainer;
import zabi.minecraft.minerva.common.network.container.TransferRule;
import zabi.minecraft.minerva.common.network.container.slot.ModSlot;
import zabi.minecraft.minerva.common.network.container.slot.SlotFiltered;
import zabi.minecraft.minerva.common.network.container.slot.SlotOutput;

/**
 * Created by Joseph on 7/17/2017.
 */
public class ContainerOven extends ModContainer<TileEntityOven> {

	public int[] gui_data = new int[4];

	public ContainerOven(InventoryPlayer playerInventory, TileEntityOven tileEntity) {
		super(tileEntity);
		IItemHandler handlerUp = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		IItemHandler handlerDown = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		
		this.addRule(new TransferRule(0, 2, ContainerOven::isSapling));

		// input slot
		this.addSlotToContainer(new ModSlot<>(tileEntity, handlerUp, 0, 44, 19));
		// fuel slot
		this.addSlotToContainer(new SlotFiltered<>(tileEntity, handlerUp, 1, 44, 55, TileEntityFurnace::isItemFuel));
		// jar slot
		this.addSlotToContainer(new SlotFiltered<>(tileEntity, handlerUp, 2, 80, 55, stack -> stack.getItem() == ModItems.empty_jar));
		// output slot
		this.addSlotToContainer(new SlotOutput<>(tileEntity, handlerDown, 0, 116, 19));
		// fume slot
		this.addSlotToContainer(new SlotOutput<>(tileEntity, handlerDown, 1, 116, 55));

		this.addPlayerSlots(playerInventory);
	}

	@Override
	public int getFieldFromTile(int id) {
		if (id == 0) {
			return this.getTileEntity().getBurnTime();
		} else if (id == 1) {
			return this.getTileEntity().getItemBurnTime();
		} else if (id == 2) {
			return this.getTileEntity().getWork();
		} else if (id == 3) {
			return this.getTileEntity().isBurning() ? 1 : 0;
		}
		return -1;
	}

	@Override
	protected int getFieldsToSync() {
		return 4;
	}

	@Override
	protected void onFieldUpdated(int id, int data) {
		if ((id >= 0) && (id < 4)) {
			this.gui_data[id] = data;
		}
	}
	
	private static boolean isSapling(Slot t) {
		Block b = Block.getBlockFromItem(t.getStack().getItem());
		return b instanceof BlockSapling || b instanceof BlockModSapling;
	}
}
