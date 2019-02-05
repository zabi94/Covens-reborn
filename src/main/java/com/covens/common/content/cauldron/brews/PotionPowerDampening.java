package com.covens.common.content.cauldron.brews;

import com.covens.api.CovensAPI;
import com.covens.api.mp.IMagicPowerExpander;
import com.covens.common.content.cauldron.BrewMod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PotionPowerDampening extends BrewMod implements IMagicPowerExpander {

	public PotionPowerDampening() {
		super("power_dampening", true, 0xFADA5E, false, 3000);
		this.setIconIndex(2, 2);
	}

	@Override
	public ResourceLocation getID() {
		return this.getRegistryName();
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase elb, AbstractAttributeMap attributeMapIn, int amplifier) {
		if (elb instanceof EntityPlayer) {
			CovensAPI.getAPI().expandPlayerMP(this, (EntityPlayer) elb);
		}
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase elb, AbstractAttributeMap attributeMapIn, int amplifier) {
		if (elb instanceof EntityPlayer) {
			CovensAPI.getAPI().removeMPExpansion(this, (EntityPlayer) elb);
		}
	}

	@Override
	public int getExtraAmount(EntityPlayer p) {
		if (p.getActivePotionEffect(this) != null) {
			return (p.getActivePotionEffect(this).getAmplifier() + 1) * -50;
		}
		return 0;
	}
}
