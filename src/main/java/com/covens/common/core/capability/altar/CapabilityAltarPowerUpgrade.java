package com.covens.common.core.capability.altar;

import com.covens.api.altar.IAltarPowerUpgrade;

public class CapabilityAltarPowerUpgrade implements IAltarPowerUpgrade {
	
	private double amount;

	public CapabilityAltarPowerUpgrade(double amt) {
		amount = amt;
	}
	
	@Override
	public double getAmount() {
		return amount;
	}

}
