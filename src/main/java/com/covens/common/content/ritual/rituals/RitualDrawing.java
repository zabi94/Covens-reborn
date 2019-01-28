package com.covens.common.content.ritual.rituals;

import java.util.ArrayList;
import java.util.List;

import com.covens.api.ritual.EnumGlyphType;
import com.covens.api.ritual.IRitual;
import com.covens.api.state.StateProperties;
import com.covens.common.block.ModBlocks;
import com.covens.common.item.ModItems;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualDrawing implements IRitual {

	ArrayList<int[]> coords;

	public RitualDrawing(ArrayList<int[]> coords) {
		this.coords = coords;
	}

	@Override
	public void onFinish(EntityPlayer player, TileEntity tile, World world, BlockPos pos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		final IBlockState state = ModBlocks.ritual_glyphs.getExtendedState(ModBlocks.ritual_glyphs.getDefaultState(), world, pos).withProperty(BlockHorizontal.FACING, EnumFacing.HORIZONTALS[(int) (Math.random() * 4)]).withProperty(StateProperties.GLYPH_TYPE, EnumGlyphType.values()[data.getInteger("chalkType")]);
		this.coords.forEach(rc -> {
			world.setBlockState(pos.add(rc[0], 0, rc[1]), state, 3);
		});
	}

	@Override
	public void onStarted(EntityPlayer player, TileEntity tile, World world, BlockPos pos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		ItemStack chalk = player.getHeldItemOffhand();
		data.setInteger("chalkType", chalk.getMetadata());
		if (!player.isCreative()) {
			int usesLeft = chalk.getTagCompound().getInteger("usesLeft") - this.coords.size();
			chalk.getTagCompound().setInteger("usesLeft", usesLeft);
			if (usesLeft < 1) {
				chalk.setCount(0);
			}
		}
		player.setHeldItem(EnumHand.OFF_HAND, chalk);
	}

	@Override
	public boolean isValid(EntityPlayer player, World world, BlockPos pos, List<ItemStack> recipe, BlockPos effectivePosition, int covenSize) {
		for (int[] rc : this.coords) {
			BlockPos pos2 = pos.add(rc[0], 0, rc[1]);
			if (!world.isAirBlock(pos2) && !world.getBlockState(pos2).getBlock().isReplaceable(world, pos2) && (world.getBlockState(pos2).getBlock() != ModBlocks.ritual_glyphs)) {
				return false;
			}
		}

		return (player.getHeldItemOffhand().getItem() == ModItems.ritual_chalk) && (player.getHeldItemOffhand().getMetadata() != 1) && (player.isCreative() || (player.getHeldItemOffhand().getTagCompound().getInteger("usesLeft") >= this.coords.size()));
	}
}
