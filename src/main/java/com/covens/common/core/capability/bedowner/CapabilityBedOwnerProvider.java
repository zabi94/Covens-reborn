package com.covens.common.core.capability.bedowner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityBedOwnerProvider implements ICapabilityProvider {
	
	private final CapabilityBedOwner INSTANCE = new CapabilityBedOwner();

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityBedOwner.CAPABILITY) {
			return true;
		}
		return false;
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityBedOwner.CAPABILITY) {
			return CapabilityBedOwner.CAPABILITY.cast(INSTANCE);
		}
		return null;
	}

}
