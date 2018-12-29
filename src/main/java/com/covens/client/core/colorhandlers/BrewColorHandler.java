package com.covens.client.core.colorhandlers;

import com.covens.common.content.cauldron.BrewData;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public final class BrewColorHandler implements IItemColor {
	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		if (tintIndex == 0) {
			return BrewData.fromStack(stack).getColor();
		}
		return -1;
	}
}