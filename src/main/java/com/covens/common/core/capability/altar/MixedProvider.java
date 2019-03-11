package com.covens.common.core.capability.altar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.api.altar.IAltarPowerUpgrade;
import com.covens.api.altar.IAltarSpeedUpgrade;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class MixedProvider implements ICapabilityProvider {
	
	private IAltarSpeedUpgrade gain;
	private IAltarPowerUpgrade mult;
	
	public MixedProvider(int speed, double power) {
		gain = new CapabilityAltarSpeedUpgrade(speed);
		mult = new CapabilityAltarPowerUpgrade(power);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == AltarCapabilities.ALTAR_GAIN_CAPABILITY || capability == AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY;
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == AltarCapabilities.ALTAR_GAIN_CAPABILITY) {
			return AltarCapabilities.ALTAR_GAIN_CAPABILITY.cast(gain);
		}
		if (capability == AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY) {
			return AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY.cast(mult);
		}
		return null;
	}
}
