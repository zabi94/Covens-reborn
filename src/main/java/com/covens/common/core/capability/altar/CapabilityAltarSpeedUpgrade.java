package com.covens.common.core.capability.altar;

import com.covens.api.altar.IAltarSpeedUpgrade;

public class CapabilityAltarSpeedUpgrade implements IAltarSpeedUpgrade {
	
	private int amount;

	public CapabilityAltarSpeedUpgrade(int amt) {
		amount = amt;
	}
	
	@Override
	public int getAmount() {
		return amount;
	}

}
