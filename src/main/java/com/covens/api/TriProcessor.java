package com.covens.api;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@FunctionalInterface
public interface TriProcessor<R> {
	public R process(List<ItemStack> stacks, FluidStack stack, R in);
}