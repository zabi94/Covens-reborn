package com.covens.common.item.magic;

import static net.minecraft.util.math.RayTraceResult.Type.ENTITY;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.covens.common.Covens;
import com.covens.common.core.helper.NBTHelper;
import com.covens.common.core.helper.RayTraceHelper;
import com.covens.common.item.ItemMod;
import com.covens.common.lib.LibItemName;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class was created by Arekkuusu on 5/15/2017. It's distributed as part of
 * Covens under the MIT license.
 */
public class ItemTaglock extends ItemMod {

	public ItemTaglock() {
		super(LibItemName.TAGLOCK);
	}

	// Todo: Make appearance change based on whether it has a taglock or not in it.

	public static Optional<EntityLivingBase> getVictim(ItemStack stack, World world) {
		UUID uuid = NBTHelper.getUniqueID(stack, Covens.TAGLOCK_ENTITY);
		for (Entity entity : world.loadedEntityList) {
			if ((entity instanceof EntityLivingBase) && entity.getUniqueID().equals(uuid)) {
				return Optional.of((EntityLivingBase) entity);
			}
		}
		EntityPlayer victim = world.getPlayerEntityByUUID(uuid);
		return Optional.ofNullable(victim);
	}

	public static void setVictim(ItemStack stack, EntityLivingBase victim) {
		NBTHelper.setUniqueID(stack, Covens.TAGLOCK_ENTITY, victim.getUniqueID());
		NBTHelper.setString(stack, Covens.TAGLOCK_ENTITY_NAME, victim.getName());
	}

	public static void removeVictim(ItemStack stack) {
		NBTHelper.removeTag(stack, Covens.TAGLOCK_ENTITY);
		NBTHelper.removeTag(stack, Covens.TAGLOCK_ENTITY_NAME);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			RayTraceResult result = RayTraceHelper.rayTraceResult(player, RayTraceHelper.fromLookVec(player, 2), true, true);
			if ((result != null) && (result.typeOfHit == ENTITY) && (result.entityHit instanceof EntityLivingBase)) {
				setVictim(player.getHeldItem(hand), (EntityLivingBase) result.entityHit);
			}
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		if (NBTHelper.hasTag(stack, Covens.TAGLOCK_ENTITY_NAME)) {
			tooltip.add(TextFormatting.DARK_GRAY + NBTHelper.getString(stack, Covens.TAGLOCK_ENTITY_NAME));
		} else {
			tooltip.add(TextFormatting.DARK_GRAY + I18n.format("item.tag_lock.empty"));
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock().isBed(state, world, pos, player)) {
			Optional<EntityPlayer> victim = this.getPlayerFromBed(world, pos);
			if (victim.isPresent()) {
				setVictim(player.getHeldItem(hand), victim.get());
				return EnumActionResult.SUCCESS;
			}
		}

		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	private Optional<EntityPlayer> getPlayerFromBed(World world, BlockPos bed) {
		return world.playerEntities.stream().filter(player -> player.bedLocation != null).filter(player -> player.getBedLocation().equals(bed)).findAny();
	}
}
