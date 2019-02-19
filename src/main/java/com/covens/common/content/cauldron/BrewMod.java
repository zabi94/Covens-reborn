package com.covens.common.content.cauldron;

import com.covens.api.cauldron.IBrewEffect;
import com.covens.api.cauldron.IBrewModifierList;
import com.covens.common.potion.PotionMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BrewMod extends PotionMod implements IBrewEffect {

	protected int defaultDuration;
	protected int cost;

	public BrewMod(String name, boolean isBadEffectIn, int liquidColorIn, boolean isInstant, int defaultDuration, int cost) {
		super(name, isBadEffectIn, liquidColorIn, isInstant);
		this.defaultDuration = defaultDuration;
		this.cost = cost;
	}

	@Override
	public void applyInWorld(World world, BlockPos pos, EnumFacing side, IBrewModifierList modifiers, EntityLivingBase thrower) {
	}

	@Override
	public PotionEffect onApplyToEntity(EntityLivingBase entity, PotionEffect effect, IBrewModifierList modifiers, Entity thrower) {
		return effect;
	}

	@Override
	public boolean hasInWorldEffect() {
		return false;
	}

	@Override
	public int getDefaultDuration() {
		return this.defaultDuration;
	}

	@Override
	public int getArrowDuration() {
		return this.defaultDuration / 6;
	}

	@Override
	public int getLingeringDuration() {
		return this.defaultDuration / 5;
	}

	@Override
	public int getCost() {
		return cost;
	}

}
