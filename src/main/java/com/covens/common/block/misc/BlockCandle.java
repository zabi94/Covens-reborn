package com.covens.common.block.misc;

import java.util.Random;

import com.covens.api.state.StateProperties;
import com.covens.common.block.BlockMod;
import com.covens.common.block.ModBlocks;
import com.covens.common.core.capability.altar.GainProvider;
import com.covens.common.integration.optifine.Optifine;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.crafting.IInfusionStabiliserExt;


@Optional.Interface(iface = "thaumcraft.api.crafting.IInfusionStabiliserExt", modid = "thaumcraft")
public abstract class BlockCandle extends BlockMod implements IInfusionStabiliserExt {

	private boolean isLit;
	private static final GainProvider gain_lit = new GainProvider(2);
	private static final GainProvider gain_unlit = new GainProvider(1);

	public BlockCandle(String id, boolean lit) {
		super(id, Material.CLOTH);
		this.setSoundType(SoundType.CLOTH);
		this.setDefaultState(this.blockState.getBaseState().withProperty(StateProperties.COLOR, EnumDyeColor.WHITE));
		this.isLit = lit;
		if (this.isLit) {
			this.setCreativeTab(null); // No need for them to appear twice
		}
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return MapColor.getBlockColor(state.getValue(StateProperties.COLOR));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(StateProperties.COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(StateProperties.COLOR).getMetadata();
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
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(state);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (state.getBlock() == ModBlocks.candle_medium_lit) {
			world.setBlockState(pos, ModBlocks.candle_medium.getDefaultState().withProperty(StateProperties.COLOR, state.getValue(StateProperties.COLOR)), 3);
		} else if (state.getBlock() == ModBlocks.candle_small_lit) {
			world.setBlockState(pos, ModBlocks.candle_small.getDefaultState().withProperty(StateProperties.COLOR, state.getValue(StateProperties.COLOR)), 3);
		} else {
			ItemStack heldItem = playerIn.getHeldItem(hand);
			if (!heldItem.isEmpty() && (heldItem.getItem() == Items.FLINT_AND_STEEL)) {
				heldItem.damageItem(1, playerIn);
				if (state.getBlock() == ModBlocks.candle_medium) {
					world.setBlockState(pos, ModBlocks.candle_medium_lit.getDefaultState().withProperty(StateProperties.COLOR, state.getValue(StateProperties.COLOR)), 3);
				} else if (state.getBlock() == ModBlocks.candle_small) {
					world.setBlockState(pos, ModBlocks.candle_small_lit.getDefaultState().withProperty(StateProperties.COLOR, state.getValue(StateProperties.COLOR)), 3);
				}
			}
		}
		return true;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, StateProperties.COLOR);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return this.isLit ? 9 + (this.getType() * 5) : 0;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(StateProperties.COLOR, EnumDyeColor.byMetadata(meta));
	}

	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (this.isLit) {
			Vec3d offset = this.getOffset(state, world, pos);
			world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5 + offset.x, pos.getY() + 0.7 + (this.getType() * 0.25), pos.getZ() + 0.5 + offset.z, 0, 0, 0);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!this.canPlaceBlockAt(worldIn, pos)) {
			worldIn.destroyBlock(pos, true);
		}
	}

	@Override
	public EnumPushReaction getPushReaction(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	public abstract int getType();

	@Override
	public EnumOffsetType getOffsetType() {
		if (Optifine.isLoaded()) {
			return EnumOffsetType.NONE;
		}
		return EnumOffsetType.XZ;
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (int i = 0; i < 16; ++i) {
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		if (this.isLit) {
			return new ItemStack(state.getBlock() == ModBlocks.candle_medium_lit ? ModBlocks.candle_medium : ModBlocks.candle_small, 1, state.getBlock().getMetaFromState(state));
		}
		return super.getPickBlock(state, target, world, pos, player);
	}

	public boolean isLit() {
		return this.isLit;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	@Optional.Method(modid = "thaumcraft")
	public boolean canStabaliseInfusion(World world, BlockPos pos) {
		return true;
	}

	@Override
	@Optional.Method(modid = "thaumcraft")
	public float getStabilizationAmount(World world, BlockPos pos) {
		return 0;
	}

	@Override
	public boolean hasItemCapabilities() {
		return true;
	}
	
	@Override
	public ICapabilityProvider getExtraCaps() {
		return isLit()?gain_lit:gain_unlit;
	}
	
	@SideOnly(Side.CLIENT)
	protected static void registerDecoyModel(Block block, int meta, Block as) {
		Item itemAs = Item.getItemFromBlock(as);
		if (itemAs == Items.AIR) {
			throw new UnsupportedOperationException("The decoy block has no Item!");
		}
		Item itemReal = Item.getItemFromBlock(block);
		if (itemReal == Items.AIR) {
			throw new UnsupportedOperationException("This block has no Item!");
		}
		ModelLoader.setCustomModelResourceLocation(itemReal, meta, new ModelResourceLocation(itemAs.getRegistryName(), "inventory"));
	}
}
