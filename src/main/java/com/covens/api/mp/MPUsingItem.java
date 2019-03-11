package com.covens.api.mp;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface MPUsingItem {
	/**
	 * This class is only here to make items that have this capability show the MP
	 * bar. No additional content required
	 */
	@CapabilityInject(MPUsingItem.class)
	public static final Capability<MPUsingItem> CAPABILITY = null;

	public static class Storage implements IStorage<MPUsingItem> {

		@Override
		public NBTBase writeNBT(Capability<MPUsingItem> capability, MPUsingItem instance, EnumFacing side) {
			return new NBTTagCompound();
		}

		@Override
		public void readNBT(Capability<MPUsingItem> capability, MPUsingItem instance, EnumFacing side, NBTBase nbt) {
			// No op
		}

	}
}
