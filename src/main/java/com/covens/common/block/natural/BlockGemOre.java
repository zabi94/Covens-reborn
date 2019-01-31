package com.covens.common.block.natural;

import static com.covens.common.core.statics.ModCreativeTabs.BLOCKS_CREATIVE_TAB;

import java.util.Random;

import javax.annotation.Nonnull;

import com.covens.common.block.BlockMod;
import com.covens.common.block.natural.BlockGem.Gem;
import com.covens.common.item.ModItems;
import com.covens.common.lib.LibBlockName;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zabi.minecraft.minerva.client.blockmodels.ModelHandler;

/**
 * This class was created by <Arekkuusu> on 27/06/2017. It's distributed as part
 * of Solar Epiphany under the MIT license.
 */
public class BlockGemOre extends BlockMod {

	public static final PropertyEnum<Gem> GEM = PropertyEnum.create("gem", Gem.class);

	public BlockGemOre() {
		super(LibBlockName.GEM_ORE, Material.ROCK);
		this.setHardness(2.0F);
		this.setCreativeTab(BLOCKS_CREATIVE_TAB);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta < 0 || meta >= Gem.values().length) {
			return this.getDefaultState();
		}
		return this.getDefaultState().withProperty(GEM, Gem.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(GEM).ordinal();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(GEM).ordinal();
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
		for (int i = 0; i < Gem.values().length; ++i) {
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ModItems.gem;
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

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, GEM);
	}

	@Override
	public void registerModel() {
		Gem[] values = Gem.values();
		for (int i = 0; i < values.length; i++) {
			Gem gem = values[i];
			ModelHandler.registerForgeModel(this, i, "gem=" + gem.getName());
		}
	}

}
