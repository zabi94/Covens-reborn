package com.covens.common.core.capability.mimic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class MimicDataProvider implements ICapabilitySerializable<NBTBase> {
	private IMimicData default_capability = CapabilityMimicData.CAPABILITY.getDefaultInstance();

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing) {
		return capability == CapabilityMimicData.CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing) {
		if (capability == CapabilityMimicData.CAPABILITY) {
			return CapabilityMimicData.CAPABILITY.cast(this.default_capability);
		}
		return null;
	}

	@Override
	public NBTBase serializeNBT() {
		return CapabilityMimicData.CAPABILITY.getStorage().writeNBT(CapabilityMimicData.CAPABILITY, this.default_capability, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbtBase) {
		CapabilityMimicData.CAPABILITY.getStorage().readNBT(CapabilityMimicData.CAPABILITY, this.default_capability, null, nbtBase);
	}
}
