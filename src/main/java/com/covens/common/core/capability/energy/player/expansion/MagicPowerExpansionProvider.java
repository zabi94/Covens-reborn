package com.covens.common.core.capability.energy.player.expansion;

import static com.covens.common.core.capability.energy.player.expansion.CapabilityMPExpansion.CAPABILITY;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class MagicPowerExpansionProvider implements ICapabilitySerializable<NBTBase> {

	private CapabilityMPExpansion default_capability = CAPABILITY.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CAPABILITY) {
			return CAPABILITY.<T>cast(this.default_capability);
		}
		return null;
	}

	@Override
	public NBTBase serializeNBT() {
		return CAPABILITY.getStorage().writeNBT(CAPABILITY, this.default_capability, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		CAPABILITY.getStorage().readNBT(CAPABILITY, this.default_capability, null, nbt);
	}

}
