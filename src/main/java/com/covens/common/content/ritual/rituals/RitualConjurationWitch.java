package com.covens.common.content.ritual.rituals;

import java.util.Optional;
import java.util.Random;

import javax.annotation.Nonnull;

import com.covens.api.ritual.IRitual;
import com.covens.common.item.ModItems;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualConjurationWitch implements IRitual {

	@Override
	public void onFinish(EntityPlayer player, TileEntity tile, World world, BlockPos pos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		if (!world.isRemote) {
			EntityWitch witch = new EntityWitch(world);
			witch.setLocationAndAngles(effectivePosition.getX(), effectivePosition.getY(), effectivePosition.getZ(), (float) (Math.random() * 360), 0);
			witch.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(witch)), (IEntityLivingData) null);
			world.spawnEntity(witch);
			if (Math.random() < 0.1) {
				witch.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 6000, 2, false, false));
			}
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
