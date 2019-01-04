package com.covens.common.content.cauldron.brews;

import javax.annotation.Nullable;

import com.covens.common.content.cauldron.BrewMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;

public class PotionRevealing extends BrewMod {
	public PotionRevealing() {
		super("revealing", false, 0x00FFFF, true, 0);
	}

	@Override
	public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {
		if (!entityLivingBaseIn.getEntityWorld().isRemote) {
			if (entityLivingBaseIn.isPotionActive(MobEffects.INVISIBILITY)) {
				entityLivingBaseIn.removePotionEffect(MobEffects.INVISIBILITY);
			}
			//TODO readd this when skin stealing is actually implemented
//			if (entityLivingBaseIn.hasCapability(CapabilityMimicData.CAPABILITY, null)) {
//				final IMimicData capability = entityLivingBaseIn.getCapability(CapabilityMimicData.CAPABILITY, null);
//				if (capability.isMimicking()) {
//					capability.setMimicking(false, (EntityPlayer) entityLivingBaseIn);
//				}
//				NetworkHandler.HANDLER.sendToAll(new PlayerMimicDataChanged((EntityPlayer) entityLivingBaseIn));
//			}
		}
	}
}
