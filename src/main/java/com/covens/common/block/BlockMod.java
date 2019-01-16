package com.covens.common.block;

import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibMod;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockMod extends zabi.minecraft.minerva.common.block.BlockMod {

	public BlockMod(String id, Material material, SoundType sound) {
		super(LibMod.MOD_ID, id, material, sound, ModCreativeTabs.BLOCKS_CREATIVE_TAB);
	}

	public BlockMod(String id, Material material) {
		super(LibMod.MOD_ID, id, material, ModCreativeTabs.BLOCKS_CREATIVE_TAB);
	}

}
