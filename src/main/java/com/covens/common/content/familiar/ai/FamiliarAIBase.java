package com.covens.common.content.familiar.ai;

import com.covens.api.CovensAPI;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public abstract class FamiliarAIBase extends EntityAIBase {

	protected final EntityLiving familiar;
	private CapabilityFamiliarCreature capability = null;
	
	public FamiliarAIBase(EntityLiving familiarIn) {
		if (!CovensAPI.getAPI().isValidFamiliar(familiarIn)) {
			throw new IllegalArgumentException(familiarIn.getClass().getCanonicalName()+" is not a valid familiar type");
		}
		familiar = familiarIn;
	}
	
	protected final CapabilityFamiliarCreature getCap() {
		if (capability == null) {
			capability = familiar.getCapability(CapabilityFamiliarCreature.CAPABILITY, null);
		}
		return capability;
	}

}
