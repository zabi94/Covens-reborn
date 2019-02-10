package com.covens.common.content.familiar.ai;

import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

public class AIFamiliarSit extends FamiliarAIBase {

	public AIFamiliarSit(EntityLiving familiarIn) {
		super(familiarIn);
		this.setMutexBits(5);
	}

	@Override
	public boolean shouldExecute() {
		if (!this.getCap().hasOwner()) {
			return false;
		} else if (this.familiar.isInWater()) {
			return false;
		} else if (!this.familiar.onGround) {
			return false;
		} else {
			EntityLivingBase owner = this.getCap().getOwner();
			if (owner == null) {
				return true;
			} else {
				return (owner.getRevengeTarget() != null) ? false : CapabilityFamiliarCreature.isSitting(this.familiar);
			}
		}
	}

	@Override
	public void startExecuting() {
		this.familiar.getNavigator().clearPath();
		CapabilityFamiliarCreature.setSitting(this.familiar, true);
	}

	@Override
	public void resetTask() {
		CapabilityFamiliarCreature.setSitting(this.familiar, false);
	}

}
