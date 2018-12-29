package com.covens.client.core.colorhandlers;

import com.covens.api.spell.ISpell;
import com.covens.common.item.magic.ItemSpellPage;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public final class SpellPageColorHandler implements IItemColor {
	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		if (tintIndex == 0) {
			ISpell s = ItemSpellPage.getSpellFromItemStack(stack);
			if (s != null)
				return s.getColor();
		}
		return -1;
	}
}