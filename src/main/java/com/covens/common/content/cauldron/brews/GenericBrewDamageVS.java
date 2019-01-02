package com.covens.common.content.cauldron.brews;

import com.covens.common.content.cauldron.BrewMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public abstract class GenericBrewDamageVS extends BrewMod {

	public GenericBrewDamageVS(String name, int liquidColorIn) {
		super(name, true, liquidColorIn, true, 0);
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {
		if (this.shouldAffect(entityLivingBaseIn)) {
			entityLivingBaseIn.attackEntityFrom(DamageSource.causeIndirectMagicDamage(source, indirectSource), this.getDamage(amplifier));
			this.applyExtraEffect(entityLivingBaseIn, amplifier);
		}
	}

	protected abstract boolean shouldAffect(EntityLivingBase entity);

	protected void applyExtraEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
		// Override this when necessary
	}

	protected float getDamage(int amplifier) {
		return 4 + (amplifier * 3);
	}

}
