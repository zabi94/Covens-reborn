package com.covens.common.block.misc;

import java.util.Random;

import com.covens.api.state.StateProperties;
import com.covens.common.block.BlockMod;
import com.covens.common.block.ModBlocks;
import com.covens.common.core.capability.altar.GainProvider;
import com.covens.common.lib.LibBlockName;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class BlockLantern extends BlockMod {

	private static final AxisAlignedBB bounding_box = new AxisAlignedBB(0.2, 0, 0.2, 0.8, 0.9375, 0.8);
	private static final GainProvider gain_lit = new GainProvider(3);
	private static final GainProvider gain_unlit = new GainProvider(2);

	private boolean lit;

	public BlockLantern(boolean lit) {
		super(lit ? LibBlockName.REVEALING_LANTERN : LibBlockName.LANTERN, Material.IRON);
		this.setHarvestLevel("pickaxe", 0);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(StateProperties.COLOR, EnumDyeColor.WHITE));
		this.lit = lit;
		this.setHardness(1.5f);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return this.lit ? 15 : 0;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return bounding_box;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (this.lit) {
			world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 0, 0, 0);
		}
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return (state.getValue(StateProperties.COLOR)).ordinal();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, state.getValue(StateProperties.COLOR).ordinal());
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, StateProperties.COLOR);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (int i = 0; i < EnumDyeColor.values().length; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(StateProperties.COLOR, EnumDyeColor.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(StateProperties.COLOR).ordinal();
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void registerModel() {
		for (int i = 0; i < EnumDyeColor.values().length; i++) {
			ModelResourceLocation mrl = new ModelResourceLocation(ModBlocks.revealing_lantern.getRegistryName(), "inventory");
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, mrl);
		}
	}
	
	@Override
	public boolean hasItemCapabilities() {
		return true;
	}
	
	@Override
	public ICapabilityProvider getExtraCaps() {
		return lit?gain_lit:gain_unlit;
	}
}
