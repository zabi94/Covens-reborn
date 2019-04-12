package com.covens.common.content.incantation;

import com.covens.api.incantation.IIncantation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;


public class IncantationHeal implements IIncantation {

	@Override
	public void cast(EntityPlayer sender, String[] args) {
		if (sender.getHealth() < sender.getMaxHealth()) {
			sender.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 80, 0));
		}
	}

	@Override
	public int getCost() {
		return 1000;
	}
}
