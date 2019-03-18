package com.covens.common.content.cauldron.brews;

import com.covens.common.content.cauldron.BrewMod;
import com.covens.common.core.helper.Log;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

public class PotionSinking extends BrewMod {

	@ObjectHolder("extraalchemy:effect.sinking")
	public static final Potion freezing = null;

	public PotionSinking() {
		super("sinking", true, 0x333399, false, 60 * 20, 30);
		this.setIconIndex(3, 1);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		if (entity.isInWater()) {
			entity.motionY += -0.008d * Math.sqrt(1 + amplifier);
		}
	}

	public Potion getPotion() {
		if (freezing != null) {
			Log.i("Extra Alchemy spotted! The sinking potion entity effect will be \"borrowed\" from it");
			return freezing;
		}
		Log.i("No extra alchemy found, sinking potion will default to its potion object!");
		return this;
	}

}
