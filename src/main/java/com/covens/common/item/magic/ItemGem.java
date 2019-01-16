package com.covens.common.item.magic;

import javax.annotation.Nonnull;

import com.covens.common.block.natural.BlockGem.Gem;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.item.ItemMod;
import com.covens.common.lib.LibItemName;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zabi.minecraft.minerva.client.blockmodels.ModelHandler;

/**
 * This class was created by <Arekkuusu> on 28/06/2017. It's distributed as part
 * of Solar Epiphany under the MIT license.
 */
public class ItemGem extends ItemMod {

	public ItemGem() {
		super(LibItemName.GEM);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		if ((stack.getMetadata() < 0) || (stack.getMetadata() >= Gem.values().length)) {
			return super.getTranslationKey(stack);
		}
		return super.getTranslationKey(stack) + "_" + Gem.values()[stack.getMetadata()].getName();
	}

	@Override
	public void getSubItems(CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (int i = 0; i < Gem.values().length; ++i) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public void registerModel() {
		ModelHandler.registerModel(this, Gem.class);
	}
}
