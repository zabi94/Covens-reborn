package com.covens.common.core.capability.energy;

import com.covens.api.mp.MPContainer;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class MagicPowerContainerStorage implements IStorage<MPContainer> {

	@Override
	public NBTBase writeNBT(Capability<MPContainer> capability, MPContainer instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("amount", instance.getAmount());
		tag.setInteger("maxAmount", instance.getMaxAmount());
		return tag;
	}

	@Override
	public void readNBT(Capability<MPContainer> capability, MPContainer instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		instance.setMaxAmount(tag.getInteger("maxAmount"));
		instance.setAmount(tag.getInteger("amount"));
	}

}
