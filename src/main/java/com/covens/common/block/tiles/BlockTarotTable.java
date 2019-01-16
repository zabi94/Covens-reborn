package com.covens.common.block.tiles;

import static net.minecraft.block.BlockHorizontal.FACING;

import com.covens.common.block.BlockModTileEntity;
import com.covens.common.lib.LibBlockName;
import com.covens.common.tile.tiles.TileEntityTarotsTable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * This class was created by Joseph on 3/4/2017. It's distributed as part of
 * Covens under the MIT license.
 */
public class BlockTarotTable extends BlockModTileEntity {

	public BlockTarotTable() {
		super(LibBlockName.TAROT_TABLE, Material.ROCK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setSoundType(SoundType.STONE);
		this.setResistance(3F);
		this.setHardness(3F);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		final EnumFacing facing = EnumFacing.byHorizontalIndex(meta);
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		final EnumFacing facing = state.getValue(FACING);
		return facing.getHorizontalIndex();
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		final EnumFacing enumfacing = EnumFacing.fromAngle(placer.rotationYaw);
		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityTarotsTable();
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
