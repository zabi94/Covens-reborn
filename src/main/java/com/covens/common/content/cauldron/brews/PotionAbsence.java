package com.covens.common.content.cauldron.brews;

import java.util.ArrayList;

import com.covens.common.content.cauldron.BrewMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class PotionAbsence extends BrewMod {

	public PotionAbsence() {
		super("absence", true, 0x808080, true, 0, 20);
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {
		ArrayList<PotionEffect> removalList = new ArrayList<>();
		entity.getActivePotionEffects().stream().filter(pe -> pe.getAmplifier() <= amplifier).filter(pe -> !pe.getCurativeItems().isEmpty()).forEach(pe -> removalList.add(pe));
		removalList.forEach(pe -> entity.removePotionEffect(pe.getPotion()));
	}

}
