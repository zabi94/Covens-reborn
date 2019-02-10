package com.covens.common.block.natural;

import com.covens.common.block.BlockMod;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibBlockName;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;


public class BlockSilverOre extends BlockMod {

	public BlockSilverOre() {
		super(LibBlockName.SILVER_ORE, Material.ROCK);
		this.setResistance(3F);
		this.setHardness(3F);
		this.setCreativeTab(ModCreativeTabs.BLOCKS_CREATIVE_TAB);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
		if (this.getItemDropped(state, RANDOM, fortune) != Item.getItemFromBlock(this)) {
			return 1 + RANDOM.nextInt(5);
		}
		return 0;
	}
}
