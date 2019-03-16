

package com.covens.common.item.block;

import com.covens.common.block.ModBlocks;
import com.covens.common.block.natural.tree.BlockModSapling.EnumSaplingType;
import com.covens.common.item.ItemBlockMod;

import net.minecraft.item.ItemStack;

public class ItemBlockSapling extends ItemBlockMod {

	public ItemBlockSapling() {
		super(ModBlocks.sapling);
		this.setRegistryName(ModBlocks.sapling.getRegistryName());
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		if (stack.getMetadata() >= EnumSaplingType.values().length) {
			return super.getTranslationKey(stack);
		}
		return super.getTranslationKey(stack) + "_" + EnumSaplingType.values()[stack.getMetadata()].getName();
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public boolean getHasSubtypes() {
		return true;
	}

	@Override
	public int getItemBurnTime(ItemStack itemStack) {
		return 100;
	}
}
