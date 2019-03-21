package com.covens.common.item;

import javax.annotation.Nullable;

import com.covens.common.block.BlockMod;
import com.covens.common.core.statics.ModCreativeTabs;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemBlockMod extends ItemBlock {

	public ItemBlockMod(Block block) {
		super(block);
		this.setCreativeTab(ModCreativeTabs.BLOCKS_CREATIVE_TAB);
	}

	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		if (this.block instanceof BlockMod && ((BlockMod) this.block).hasItemCapabilities()) {
			return ((BlockMod) this.block).getExtraCaps(stack, nbt);
		}
		return super.initCapabilities(stack, nbt);
	}
}
