package com.covens.common.content.cauldron.brews;

import javax.annotation.Nullable;

import com.covens.common.content.cauldron.BrewMod;
import com.covens.common.entity.EntityBrew;
import com.covens.common.entity.EntityLingeringBrew;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class PotionLove extends BrewMod {
	public PotionLove() {
		super("love", false, 0xff69b4, true, 0, 40);
	}

	@Override
	public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {
		if (entityLivingBaseIn instanceof EntityAnimal) {
			EntityAnimal animal = (EntityAnimal) entityLivingBaseIn;
			if ((animal.getGrowingAge() == 0) && !animal.isInLove()) {
				if (source instanceof EntityLingeringBrew) {
					EntityLingeringBrew brew = (EntityLingeringBrew) source;
					animal.setInLove((EntityPlayer) brew.getOwner());
				} else if (source instanceof EntityBrew) {
					EntityBrew brew = (EntityBrew) source;
					animal.setInLove((EntityPlayer) brew.getThrower());
				}
			}
		}
	}
}
