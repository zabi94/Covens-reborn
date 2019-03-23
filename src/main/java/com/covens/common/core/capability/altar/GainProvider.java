package com.covens.common.core.capability.altar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.api.altar.IAltarSpeedUpgrade;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class GainProvider implements ICapabilityProvider {
	
	private IAltarSpeedUpgrade gain;
	
	public GainProvider(int speed) {
		gain = new CapabilityAltarSpeedUpgrade(speed);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == AltarCapabilities.ALTAR_GAIN_CAPABILITY;
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == AltarCapabilities.ALTAR_GAIN_CAPABILITY) {
			return AltarCapabilities.ALTAR_GAIN_CAPABILITY.cast(gain);
		}
		return null;
	}
}
