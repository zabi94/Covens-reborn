package com.covens.common.block.natural;

import java.util.Random;

import com.covens.common.block.BlockMod;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockGemOre extends BlockMod {

	private final Gem gem;
	
	public BlockGemOre(Gem gemIn) {
		super(gemIn.getOreName(), Material.ROCK);
		this.setHardness(2.0F);
		this.gem = gemIn;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return gem.getGemItem();
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		if ((fortune > 0) && (Item.getItemFromBlock(this) != this.getItemDropped(this.getBlockState().getValidStates().iterator().next(), random, fortune))) {
			int i = random.nextInt(fortune + 2) - 1;
			if (i < 0) {
				i = 0;
			}

			return this.quantityDropped(random) * (i + 1);
		}
		return this.quantityDropped(random);
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		Random rand = world instanceof World ? ((World) world).rand : new Random();
		return MathHelper.getInt(rand, 2, 5);
	}
}
