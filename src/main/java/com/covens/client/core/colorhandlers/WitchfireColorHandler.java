package com.covens.client.core.colorhandlers;

import com.covens.common.block.misc.BlockWitchFire;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public final class WitchfireColorHandler implements IBlockColor {
	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
		return state.getValue(BlockWitchFire.TYPE).getColor();
	}
}
