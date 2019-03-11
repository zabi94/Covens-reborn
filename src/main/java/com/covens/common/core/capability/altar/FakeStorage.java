package com.covens.common.core.capability.altar;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class FakeStorage<T> implements IStorage<T> {

	@Override
	@Nullable
	public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
		
	}

}
