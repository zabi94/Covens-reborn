package com.covens.common.content.transformation.vampire.blood;

import static com.covens.common.content.transformation.vampire.blood.CapabilityBloodReserve.CAPABILITY;

import com.covens.api.transformation.IBloodReserve;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class BloodReserveProvider implements ICapabilitySerializable<NBTBase> {

	private IBloodReserve default_capability = CAPABILITY.getDefaultInstance();

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
