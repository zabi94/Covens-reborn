package com.covens.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;


public class ItemBlockColor extends ItemBlock {

	public ItemBlockColor(Block block) {
		super(block);
		this.setRegistryName(block.getRegistryName());
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey() + "." + EnumDyeColor.byMetadata(stack.getMetadata());
	}
}
