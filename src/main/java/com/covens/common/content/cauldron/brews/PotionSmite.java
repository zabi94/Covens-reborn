package com.covens.common.content.cauldron.brews;

import com.covens.common.core.helper.MobHelper;

import net.minecraft.entity.EntityLivingBase;

public class PotionSmite extends GenericBrewDamageVS {

	public PotionSmite() {
		super("smite", 0x8DA399, 30);
	}

	@Override
	protected boolean shouldAffect(EntityLivingBase entity) {
		return MobHelper.isSpirit(entity) || MobHelper.isDemon(entity) || MobHelper.isCorporealUndead(entity);
	}

	@Override
	protected float getDamage(int amplifier) {
		return 2 + (amplifier * 1.5f);
	}

}
