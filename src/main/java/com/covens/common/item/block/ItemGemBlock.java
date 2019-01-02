package com.covens.common.item.block;

import javax.annotation.Nonnull;

import com.covens.client.core.IModelRegister;
import com.covens.client.handler.ModelHandler;
import com.covens.common.block.natural.BlockGem;
import com.covens.common.block.natural.BlockGem.Gem;
import com.covens.common.core.statics.ModCreativeTabs;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemGemBlock extends ItemBlock implements IModelRegister {
	public ItemGemBlock(Block block) {
		super(block);
		this.setRegistryName(block.getRegistryName());
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(ModCreativeTabs.BLOCKS_CREATIVE_TAB);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey() + "_" + Gem.values()[stack.getMetadata()].getName();
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (int i = 0; i < Gem.values().length; ++i) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public void registerModel() {
		BlockGem.Gem[] values = BlockGem.Gem.values();
		for (int i = 0; i < values.length; i++) {
			BlockGem.Gem gem = values[i];
			ModelHandler.registerForgeModel(this, i, "gem=" + gem.getName());
		}
	}
}
