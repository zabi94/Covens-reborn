package com.covens.common.block;

import javax.annotation.Nullable;

import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibMod;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class BlockMod extends zabi.minecraft.minerva.common.block.BlockMod {
	
	private boolean hasCaps = false;

	public BlockMod(String id, Material material, SoundType sound) {
		super(LibMod.MOD_ID, id, material, sound, ModCreativeTabs.BLOCKS_CREATIVE_TAB);
	}

	public BlockMod(String id, Material material) {
		super(LibMod.MOD_ID, id, material, ModCreativeTabs.BLOCKS_CREATIVE_TAB);
	}

	public boolean hasItemCapabilities() {
		return hasCaps;
	}
	
	public BlockMod setHasCaps() {
		hasCaps = true;
		return this;
	}
	
	public ICapabilityProvider getExtraCaps() {
		return null;
	}
	
	public ICapabilityProvider getExtraCaps(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return getExtraCaps();
	}

}
