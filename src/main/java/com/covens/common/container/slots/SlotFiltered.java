package com.covens.common.container.slots;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

public class SlotFiltered<T extends TileEntity> extends ModSlot<T> {

	private Predicate<ItemStack> ing;

	public SlotFiltered(T tileEntity, IItemHandler handler, int index, int xPosition, int yPosition, Predicate<ItemStack> ing) {
		super(tileEntity, handler, index, xPosition, yPosition);
		this.ing = ing;
	}

	@Override
	public boolean isItemValid(@Nonnull ItemStack stack) {
		return this.ing.test(stack);
	}
}
