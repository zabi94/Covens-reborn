package com.covens.common.item.natural.seed;

import com.covens.common.block.ModBlocks;
import com.covens.common.core.statics.ModCrops;
import com.covens.common.lib.LibItemName;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;

/**
 * This class was created by Arekkuusu on 02/03/2017.
 * It's distributed as part of Covens under
 * the MIT license.
 */
public class ItemKelpSeed extends ItemSeed {

	public ItemKelpSeed() {
		super(LibItemName.SEED_KELP, ModBlocks.crop_kelp, ModCrops.KELP.getSoil());
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		final RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, false);
		ItemStack stack = playerIn.getHeldItem(hand);
		if (raytraceresult == null) {
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}
		if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
			final BlockPos blockpos = raytraceresult.getBlockPos();

			if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, stack)) {
				return ActionResult.newResult(EnumActionResult.FAIL, stack);
			}

			final BlockPos up = blockpos.up();
			final IBlockState iblockstate = worldIn.getBlockState(blockpos);

			if (iblockstate.getMaterial().isSolid()) {
				final BlockSnapshot blocksnapshot = BlockSnapshot.getBlockSnapshot(worldIn, up);
				if (net.minecraftforge.event.ForgeEventFactory.onPlayerBlockPlace(playerIn, blocksnapshot, net.minecraft.util.EnumFacing.UP, hand).isCanceled()) {
					blocksnapshot.restore(true, false);
					return ActionResult.newResult(EnumActionResult.FAIL, stack);
				}
				worldIn.setBlockState(up, this.crop.getDefaultState(), 11);
				if (!playerIn.capabilities.isCreativeMode) {
					stack.shrink(1);
				}
				worldIn.playSound(playerIn, blockpos, SoundEvents.BLOCK_WATERLILY_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}
		}

		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return EnumActionResult.FAIL;
	}
}
