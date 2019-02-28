package com.covens.common.item.misc;

import java.util.Iterator;

import com.covens.api.CovensAPI;
import com.covens.api.transformation.DefaultTransformations;
import com.covens.common.block.ModBlocks;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.item.ItemMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.blockmodels.ModelHandler;

public class ItemSparkDarkness extends ItemMod {

	public ItemSparkDarkness(String id) {
		super(id);
		this.setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.isCreative()) {
			becomeVampire(playerIn);
		}
		if (hasTransformation(playerIn) || isTooHealthy(playerIn) || worldIn.isDaytime() || !isInGrave(playerIn)) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
		}
		becomeVampire(playerIn);
		Iterator<MutableBlockPos> it = BlockPos.getAllInBoxMutable(playerIn.getPosition().down().east().north(), playerIn.getPosition().up(2).west().south()).iterator();
		while (it.hasNext()) {
			MutableBlockPos pos = it.next();
			worldIn.setBlockState(pos, ModBlocks.graveyard_dirt.getDefaultState(), 3);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.isCreative()?playerIn.getHeldItem(handIn):ItemStack.EMPTY);
	}
	
	private boolean isInGrave(EntityPlayer playerIn) {
		Iterator<MutableBlockPos> it = BlockPos.getAllInBoxMutable(playerIn.getPosition().down().east().north(), playerIn.getPosition().up(2).west().south()).iterator();
		while (it.hasNext()) {
			MutableBlockPos pos = it.next();
			if (pos.equals(playerIn.getPosition()) || pos.equals(playerIn.getPosition().up())) {
				continue;
			}
			if (!isDirt(playerIn.world, pos)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isDirt(World w, BlockPos p) {
		return w.getBlockState(p).getBlock() == Blocks.DIRT;
	}

	private boolean isTooHealthy(EntityPlayer playerIn) {
		return playerIn.getMaxHealth() * 0.2 < playerIn.getHealth();
	}

	private void becomeVampire(EntityPlayer playerIn) {
		playerIn.heal(20);
		CovensAPI.getAPI().setTypeAndLevel(playerIn, DefaultTransformations.VAMPIRE, 1, playerIn.world.isRemote);
		CovensAPI.getAPI().setVampireBlood(playerIn, 0);
		playerIn.world.playSound(null, playerIn.getPosition(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 3f, 0.6f);
	}
	
	private boolean hasTransformation(EntityPlayer p) {
		return p.getCapability(CapabilityTransformation.CAPABILITY, null).getType() != DefaultTransformations.NONE;
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
