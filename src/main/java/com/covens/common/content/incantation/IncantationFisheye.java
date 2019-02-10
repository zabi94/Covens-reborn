package com.covens.common.content.incantation;

import com.covens.api.incantation.IIncantation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;


public class IncantationFisheye implements IIncantation {

	// Todo: Make this only affect vision.
	@SuppressWarnings("ConstantConditions")
	@Override
	public void cast(EntityPlayer sender, String[] args) {
		sender.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 275, 0));
		sender.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 275, 0));
	}

	@Override
	public int getCost() {
		return 800;
	}
}
