package com.covens.common.core.capability.energy;

import com.covens.api.mp.MPUsingMachine;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class MagicPowerConsumerStorage implements IStorage<MPUsingMachine> {

	@Override
	public NBTBase writeNBT(Capability<MPUsingMachine> capability, MPUsingMachine instance, EnumFacing side) {
		return instance.writeToNbt();
	}

	@Override
	public void readNBT(Capability<MPUsingMachine> capability, MPUsingMachine instance, EnumFacing side, NBTBase nbt) {
		instance.readFromNbt((NBTTagCompound) nbt);
	}

}
