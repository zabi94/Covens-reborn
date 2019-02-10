package com.covens.common.block.natural.crop;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.blockmodels.IModelRegister;


public class BlockCrop extends BlockCrops implements IModelRegister {

	private int maxAge;
	private Item seed;
	private Item crop;

	public BlockCrop(String id) {
		super();
		this.setTranslationKey(id);
		this.setRegistryName(id);
		this.setCreativeTab(null);
		this.maxAge = 7;
	}

	public BlockCrop(String id, int maxAge) {
		super();
		this.setTranslationKey(id);
		this.setRegistryName(id);
		this.setCreativeTab(null);
		this.maxAge = maxAge;
	}

	@Override
	public int getMaxAge() {
		return this.maxAge;
	}

	@Override
	public Item getSeed() {
		return this.seed;
	}

	public void setSeed(Item seed) {
		this.seed = seed;
	}

	@Override
	public Item getCrop() {
		return this.crop;
	}

	public void setCrop(Item crop) {
		this.crop = crop;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		// NO-OP since crops don't have an itemBlock to texture
	}
}
