package com.covens.common.core.capability.energy.player;

import com.covens.api.mp.MPContainer;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerMPContainerProvider implements ICapabilitySerializable<NBTBase> {

	private MPContainer container = new PlayerMPContainer();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == MPContainer.CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == MPContainer.CAPABILITY) {
			return MPContainer.CAPABILITY.cast(this.container);
		}
		return null;
	}

	@Override
	public NBTBase serializeNBT() {
		return MPContainer.CAPABILITY.getStorage().writeNBT(MPContainer.CAPABILITY, this.container, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		MPContainer.CAPABILITY.getStorage().readNBT(MPContainer.CAPABILITY, this.container, null, nbt);
	}

}
