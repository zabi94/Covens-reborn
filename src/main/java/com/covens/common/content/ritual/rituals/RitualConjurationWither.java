package com.covens.common.content.ritual.rituals;

import java.util.Optional;
import java.util.Random;

import javax.annotation.Nonnull;

import com.covens.api.ritual.IRitual;
import com.covens.common.item.ModItems;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualConjurationWither implements IRitual {

	@Override
	public void onFinish(EntityPlayer player, TileEntity tile, World world, BlockPos pos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		if (!world.isRemote) {
			EntityWither entitywither = new EntityWither(world);
			BlockPos blockpos1 = effectivePosition.add(0, 0, 0);
			entitywither.setLocationAndAngles(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.55D, blockpos1.getZ() + 0.5D, 0F, 0F);
			for (EntityPlayerMP entityplayermp : world.getEntitiesWithinAABB(EntityPlayerMP.class, entitywither.getEntityBoundingBox().grow(50.0D))) {
				CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitywither);
			}
			world.spawnEntity(entitywither);
			entitywither.ignite();
		}
	}

	@Override
	@Nonnull
	public ItemStack modifyOutput(ItemStack originalOutput, NonNullList<ItemStack> input, NBTTagCompound tag) {
		if (originalOutput.getItem() == ModItems.athame) {
			Optional<ItemStack> oldBoline = input.parallelStream().filter(is -> is.getItem() == ModItems.athame).findFirst();
			if (oldBoline.isPresent()) {
				ItemStack res = oldBoline.get().copy();
				if (res.attemptDamageItem(50, new Random(), null)) {
					return ItemStack.EMPTY;
				}
				return res;
			} else {
				return ItemStack.EMPTY;
			}
		}
		return originalOutput;
	}

}
