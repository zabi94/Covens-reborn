package com.covens.client.core.colorhandlers;

import com.covens.api.ritual.EnumGlyphType;
import com.covens.api.state.StateProperties;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public final class GlyphColorHandler implements IBlockColor {
	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
		EnumGlyphType type = state.getValue(StateProperties.GLYPH_TYPE);
		switch (type) {
			case ENDER:
				return 0x770077;
			case GOLDEN:
				return 0xe3dc3c;
			case NETHER:
				return 0xbb0000;
			default:
			case NORMAL:
				return 0xFFFFFF;
			case ANY:
				return 0xc5850b;
		}
	}
}
