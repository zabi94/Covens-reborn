package com.covens.common.content.cauldron;

import com.covens.api.cauldron.IBrewEffect;
import com.covens.api.cauldron.IBrewModifierList;
import com.covens.common.Covens;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BrewVanilla implements IBrewEffect {

	private int duration = 0;
	private int cost = 0;

	public BrewVanilla(Potion potion, int cost) {
		for (PotionType p : PotionType.REGISTRY) {
			for (PotionEffect pe : p.getEffects()) {
				if ((pe.getPotion() == potion) && (pe.getAmplifier() == 0)) {
					if ((pe.getDuration() < this.duration) || (this.duration == 0)) {
						this.duration = pe.getDuration();
					}
				}
			}
			this.cost = cost;
		}

		if (this.duration == 0) {
			Covens.logger.warn("Couldn't find the correct default duration for " + potion.getName());
			this.duration = 1800;
		}
	}

	public BrewVanilla(int duration, int cost) {
		this.duration = duration;
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
		return this.duration;
	}

	@Override
	public int getArrowDuration() {
		return this.getDefaultDuration() / 16;
	}

	@Override
	public int getLingeringDuration() {
		return this.getDefaultDuration() / 8;
	}

	@Override
	public int getCost() {
		return cost;
	}

}
