package com.covens.common.block.misc;

import java.util.Random;

import com.covens.common.block.ModBlocks;
import com.covens.common.integration.optifine.Optifine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class BlockCandleSmall extends BlockCandle {

	private static final AxisAlignedBB SMALL_BOX = new AxisAlignedBB(0.38, 0, 0.38, 0.62, 0.5, 0.62);

	public BlockCandleSmall(String id, boolean lit) {
		super(id, lit);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (Optifine.isLoaded()) {
			return SMALL_BOX;
		}
		return SMALL_BOX.offset(state.getOffset(source, pos));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(ModBlocks.candle_small);
	}

	@Override
	public int getType() {
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		for (int i = 0; i < 16; i++) {
			registerDecoyModel(this, i, ModBlocks.candle_small);
		}
	}
}
