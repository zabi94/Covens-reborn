package com.covens.common.block;

import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibMod;

import net.minecraft.block.material.Material;

public abstract class BlockModTileEntity extends zabi.minecraft.minerva.common.block.BlockModTileEntity {

	public BlockModTileEntity(String id, Material material) {
		super(LibMod.MOD_ID, id, material, ModCreativeTabs.BLOCKS_CREATIVE_TAB);
	}

}
