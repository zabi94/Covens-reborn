package com.covens.common.item.magic;

import java.util.List;
import java.util.Random;

import com.covens.api.ritual.EnumGlyphType;
import com.covens.api.state.StateProperties;
import com.covens.common.block.ModBlocks;
import com.covens.common.core.helper.NBTHelper;
import com.covens.common.item.ItemMod;
import com.covens.common.lib.LibItemName;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;

public class ItemBell extends ItemMod {

	private static final Random pitchRand = new Random();

	public ItemBell() {
		super(LibItemName.BELL);
		this.setMaxStackSize(1);
		this.setMaxDamage(20);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BLOCK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 100;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (worldIn.getTotalWorldTime() - NBTHelper.getLong(playerIn.getHeldItem(handIn), "lastFinished") > 30) {
			playerIn.setActiveHand(handIn);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		if ((count % 8) == 0) {
			pitchRand.setSeed(player.getUniqueID().getMostSignificantBits() / count);
			float extraPitch = count % 4 == 0?0.4f:0.6f;
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1f, extraPitch + (pitchRand.nextFloat() * 0.9f));
			for (int i = 0; i < 5; i++) {
				player.world.spawnParticle(EnumParticleTypes.SPELL, player.posX + player.getRNG().nextGaussian(), player.posY, player.posZ + player.getRNG().nextGaussian(), 0, 0.1, 0);
			}
		}
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase player) {
		stack.damageItem(1, player);
		if (!world.isRemote && checkChalk(world, player)) {
			Class<?> familiarClass = getEntity(world, player);
			String result = familiarClass==null?"null":familiarClass.getName();
			((EntityPlayer) player).sendStatusMessage(new TextComponentString(result), false);
		}
		NBTHelper.setLong(stack, "lastFinished", world.getTotalWorldTime());
		return stack;
	}

	private Class<?> getEntity(World world, EntityLivingBase player) {
		List<SpawnListEntry> list = world.getBiome(player.getPosition()).getSpawnableList(EnumCreatureType.CREATURE);
		if (!list.isEmpty()) {
			return list.get(0).entityClass;
//			list.stream().filter(p -> p.entityClass)
		}
		return null;
	}

	private boolean checkChalk(World world, EntityLivingBase player) {
		int[] cc = {-1, 0, 1};
		for (int a:cc) {
			for (int b:cc) {
				if (a!=0 && b!=0) {
					IBlockState bs = world.getBlockState(player.getPosition().add(a, 0, b));
					if (!bs.getBlock().equals(ModBlocks.ritual_glyphs) || bs.getValue(StateProperties.GLYPH_TYPE)!=EnumGlyphType.NORMAL) {
						return false;
					}
				}
			}
		}
		return world.getBlockState(player.getPosition()).getMaterial()==Material.AIR;
	}

}
