package com.covens.common.core.capability.altar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.api.altar.IAltarPowerUpgrade;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class MultProvider implements ICapabilityProvider {
	
	private IAltarPowerUpgrade mult;
	
	public MultProvider(double power) {
		mult = new CapabilityAltarPowerUpgrade(power);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY;
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY) {
			return AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY.cast(mult);
		}
		return null;
	}
}
