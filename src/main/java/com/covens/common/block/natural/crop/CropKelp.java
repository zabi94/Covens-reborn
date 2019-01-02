package com.covens.common.block.natural.crop;

import static net.minecraft.block.BlockLiquid.LEVEL;

import java.util.Random;

import com.covens.common.lib.LibBlockName;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

/**
 * This class was created by Arekkuusu on 02/03/2017. It's distributed as part
 * of Covens under the MIT license.
 */
public class CropKelp extends BlockCrop {

	public CropKelp() {
		super(LibBlockName.CROP_KELP);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Material getMaterial(IBlockState state) {
		return Material.WATER;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		if (this.isMaxAge(state) && this.canBlockStay(worldIn, pos, state) && this.canPlaceBlockAt(worldIn, pos.up()) && ForgeHooks.onCropsGrowPre(worldIn, pos, state, true)) {
			worldIn.setBlockState(pos.up(), this.getDefaultState(), 2);
			ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
		}
	}

	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.up()).getMaterial() == Material.WATER;
	}

	@Override
	public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.setBlockState(pos, Blocks.WATER.getDefaultState(), 2);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!this.canBlockStay(worldIn, pos, state)) {
			this.checkAndDropBlock(worldIn, pos, state);
		}
	}

	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.WATER.getDefaultState(), 11);
		}
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return false;
	}

	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		return worldIn.getBlockState(pos.down()).getMaterial().isSolid() || (worldIn.getBlockState(pos.down()).getBlock() == this);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LEVEL, AGE);
	}

}
