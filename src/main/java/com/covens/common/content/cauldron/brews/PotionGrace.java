package com.covens.common.content.cauldron.brews;

import com.covens.api.cauldron.IBrewModifierList;
import com.covens.common.content.cauldron.BrewData.BrewEntry;
import com.covens.common.content.cauldron.BrewMod;
import com.covens.common.content.cauldron.BrewModifierListImpl;
import com.covens.common.entity.EntityAoE;
import com.covens.common.entity.EntityAoE.IBrewEffectAoEOverTime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PotionGrace extends BrewMod implements IBrewEffectAoEOverTime {

	public PotionGrace() {
		super("grace", false, 0x483C32, false, 900);
		this.setIconIndex(6, 0);
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		apply(entity, amplifier);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyInWorld(World world, BlockPos pos, EnumFacing side, IBrewModifierList modifiers, EntityLivingBase thrower) {
		if (!world.isRemote) {
			EntityAoE aoe = new EntityAoE(world, new BrewEntry(this, new BrewModifierListImpl(modifiers)), pos);
			world.spawnEntity(aoe);
		}
	}

	@Override
	public void performEffectAoEOverTime(Entity entity, IBrewModifierList modifiers) {
		apply(entity, 0);
	}

	private void apply(Entity entity, int amplifier) {
		entity.fallDistance = 0;
		if (entity.motionY <= -0.08) {
			float mult = 0.9f - (0.05f * amplifier);
			entity.motionY *= mult;
		}
	}

}
