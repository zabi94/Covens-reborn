package com.covens.common.container;

import com.covens.common.container.slots.ModSlot;
import com.covens.common.container.slots.SlotOutput;
import com.covens.common.tile.tiles.TileEntityThreadSpinner;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerThreadSpinner extends ModContainer<TileEntityThreadSpinner> {

	public ContainerThreadSpinner(InventoryPlayer pi, TileEntityThreadSpinner tileEntity) {
		super(tileEntity);
		IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		this.addSlotToContainer(new SlotOutput<>(tileEntity, handler, 0, 116, 34));
		this.addSlotToContainer(new ModSlot<>(tileEntity, handler, 1, 44, 25));
		this.addSlotToContainer(new ModSlot<>(tileEntity, handler, 2, 62, 25));
		this.addSlotToContainer(new ModSlot<>(tileEntity, handler, 3, 44, 43));
		this.addSlotToContainer(new ModSlot<>(tileEntity, handler, 4, 62, 43));
		this.addPlayerSlots(pi);
	}
}
