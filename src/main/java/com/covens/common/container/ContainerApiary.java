package com.covens.common.container;

import com.covens.common.tile.tiles.TileEntityApiary;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import zabi.minecraft.minerva.common.network.container.ModContainer;
import zabi.minecraft.minerva.common.network.container.slot.SlotFiltered;


@SuppressWarnings("ConstantConditions")
public class ContainerApiary extends ModContainer<TileEntityApiary> {

	public ContainerApiary(InventoryPlayer playerInventory, TileEntityApiary tileEntity) {
		super(tileEntity);
		IItemHandler ih = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for (int i = 0; i < TileEntityApiary.ROWS; i++) {
			for (int j = 0; j < TileEntityApiary.COLUMNS; j++) {
				this.addSlotToContainer(new SlotOneItem(tileEntity, ih, j + (i * TileEntityApiary.COLUMNS), 8 + (j * 18), 16 + (i * 18)));
			}
		}
		this.addPlayerSlots(playerInventory);
	}

	private class SlotOneItem extends SlotFiltered<TileEntityApiary> {
		public SlotOneItem(TileEntityApiary tileEntity, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(tileEntity, itemHandler, index, xPosition, yPosition, s -> itemHandler.isItemValid(index, s));
		}

		@Override
		public int getItemStackLimit(ItemStack stack) {
			return 1;
		}
	}
}
