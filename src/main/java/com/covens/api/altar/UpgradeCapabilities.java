package com.covens.api.altar;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class UpgradeCapabilities {

	@CapabilityInject(IAltarPowerUpgrade.class)
	public static Capability<IAltarPowerUpgrade> ALTAR_MULTIPLIER_CAPABILITY;
	
	@CapabilityInject(IAltarSpeedUpgrade.class)
	public static Capability<IAltarSpeedUpgrade> ALTAR_GAIN_CAPABILITY;
	
	@CapabilityInject(IAltarSpecialEffect.class)
	public static Capability<IAltarSpecialEffect> ALTAR_EFFECT_CAPABILITY;
}
