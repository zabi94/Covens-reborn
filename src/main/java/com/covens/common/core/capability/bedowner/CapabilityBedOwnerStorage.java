package com.covens.common.core.capability.bedowner;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityBedOwnerStorage implements Capability.IStorage<CapabilityBedOwner> {

	@Override
	@Nullable
	public NBTBase writeNBT(Capability<CapabilityBedOwner> capability, CapabilityBedOwner instance, EnumFacing side) {
		return instance.serializeNBT();
	}

	@Override
	public void readNBT(Capability<CapabilityBedOwner> capability, CapabilityBedOwner instance, EnumFacing side, NBTBase nbt) {
		instance.deserializeNBT((NBTTagCompound) nbt);
	}


}
