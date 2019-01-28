package com.covens.common.content.ritual.rituals;

import com.covens.api.ritual.IRitual;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualPerception implements IRitual {

	@Override
	public void onUpdate(EntityPlayer player, TileEntity tile, World world, BlockPos pos, NBTTagCompound data, int ticks, BlockPos effectivePosition, int covenSize) {
		if (!world.isRemote && ((ticks % 100) == 0)) {
			world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(effectivePosition).expand(20, 20, 20).expand(-20, -20, -20)).forEach(e -> {
				e.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 110, 0, false, false));
			});
		}
	}

}
