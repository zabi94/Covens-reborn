

package com.covens.common.item.block;

import com.covens.common.block.ModBlocks;
import com.covens.common.block.natural.tree.BlockModSapling;
import com.covens.common.block.natural.tree.BlockModSapling.EnumSaplingType;
import com.covens.common.item.ItemBlockMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (BlockModSapling.EnumSaplingType t:BlockModSapling.EnumSaplingType.values()) {
				items.add(new ItemStack(this, 1, t.ordinal()));
			}
		}
	}

	@Override
	public int getItemBurnTime(ItemStack itemStack) {
		return 100;
	}
}
