package com.covens.common.content.cauldron.brews;

import java.util.ArrayList;

import com.covens.common.content.cauldron.BrewMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class PotionPurification extends BrewMod {

	public PotionPurification() {
		super("purify", false, 0xFAEBD7, true, 0, 70);
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {
		ArrayList<PotionEffect> removalList = new ArrayList<>();
		entity.getActivePotionEffects().stream().filter(pe -> pe.getPotion().isBadEffect()).filter(pe -> pe.getAmplifier() <= amplifier).filter(pe -> !pe.getCurativeItems().isEmpty()).forEach(pe -> removalList.add(pe));
		removalList.forEach(pe -> entity.removePotionEffect(pe.getPotion()));
	}

}
