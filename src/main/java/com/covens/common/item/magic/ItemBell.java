package com.covens.common.item.magic;

import java.util.Random;

import javax.annotation.Nullable;

import com.covens.api.CovensAPI;
import com.covens.api.ritual.EnumGlyphType;
import com.covens.api.state.StateProperties;
import com.covens.common.block.ModBlocks;
import com.covens.common.core.helper.NBTHelper;
import com.covens.common.item.ItemMod;
import com.covens.common.lib.LibItemName;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import zabi.minecraft.minerva.common.utils.BlockStreamHelper;
import zabi.minecraft.minerva.common.utils.CollectorsUtils;

public class ItemBell extends ItemMod {

	private static final Random pitchRand = new Random();
	private static final int[] cc = {-1, 0, 1};

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
		return 200;
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
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
		if (player instanceof EntityPlayer) {
			stack.damageItem(1, player);
			if (!world.isRemote && checkChalk(world, player)) {
				Class<? extends EntityLiving> familiarClass = getEntity(world, player);
				if (familiarClass != null) {
					Entity e = EntityRegistry.getEntry(familiarClass).newInstance(world);
					BlockPos pos = BlockStreamHelper.ofPos(player.getPosition().add(5, 2, 5), player.getPosition().add(-5, -2, -5))
							.filter(bp -> isTeleportFriendlyBlock(bp, world, e))
							.collect(CollectorsUtils.randomElement());
					e.setPosition(pos.getX()+0.5, pos.getY()+1.5, pos.getZ()+0.5);
					world.spawnEntity(e);
					CovensAPI.getAPI().bindFamiliar((EntityLiving) e, (EntityPlayer) player);
					if (e instanceof EntityTameable) {
						((EntityTameable) e).setTamedBy((EntityPlayer) player);
					}
				} else {
					((EntityPlayer) player).sendStatusMessage(new TextComponentTranslation("familiar.summon.fail"), true);
				}
			}
			NBTHelper.setLong(stack, "lastFinished", world.getTotalWorldTime());
		}
		return stack;
	}

	private static boolean isTeleportFriendlyBlock(BlockPos blockpos, World world, Entity e) {
		IBlockState iblockstate = world.getBlockState(blockpos);
		return (iblockstate.getBlockFaceShape(world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID) && iblockstate.canEntitySpawn(e) && world.isAirBlock(blockpos.up()) && world.isAirBlock(blockpos.up(2));
	}

	@Nullable
	private Class<? extends EntityLiving> getEntity(World world, EntityLivingBase player) {
		return world.getBiome(player.getPosition()).getSpawnableList(EnumCreatureType.CREATURE).stream()
				.filter(e -> CovensAPI.getAPI().isValidFamiliar(EntityRegistry.getEntry(e.entityClass).newInstance(world)))
				.map(e -> e.entityClass)
				.collect(CollectorsUtils.randomElement());
	}

	private boolean checkChalk(World world, EntityLivingBase player) {
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
