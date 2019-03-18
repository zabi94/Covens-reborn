package com.covens.common.content.cauldron.brews;

import com.covens.api.cauldron.IBrewModifierList;
import com.covens.common.content.cauldron.BrewMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PotionGrace extends BrewMod {

	public PotionGrace() {
		super("grace", false, 0x483C32, false, 900, 10);
		this.setIconIndex(6, 0);
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		this.apply(entity, amplifier);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyInWorld(World world, BlockPos pos, EnumFacing side, IBrewModifierList modifiers, EntityLivingBase thrower) {
	}

	private void apply(Entity entity, int amplifier) {
		entity.fallDistance = 0;
		if (entity.motionY <= -0.08) {
			float mult = 0.9f - (0.05f * amplifier);
			entity.motionY *= mult;
		}
	}

}
