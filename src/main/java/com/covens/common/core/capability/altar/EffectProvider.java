package com.covens.common.core.capability.altar;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.api.altar.IAltarSpecialEffect;
import com.covens.api.altar.UpgradeCapabilities;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class EffectProvider implements ICapabilityProvider {
	
	private IAltarSpecialEffect capb;
	
	public EffectProvider(UUID id) {
		capb = new CapabilityAltarSpecialEffect(id);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == UpgradeCapabilities.ALTAR_EFFECT_CAPABILITY;
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == UpgradeCapabilities.ALTAR_EFFECT_CAPABILITY) {
			return UpgradeCapabilities.ALTAR_EFFECT_CAPABILITY.cast(capb);
		}
		return null;
	}
}
