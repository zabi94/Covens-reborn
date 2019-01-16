package com.covens.common.content.transformation.vampire.blood;

import com.covens.api.transformation.IBloodReserve;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import zabi.minecraft.minerva.common.data.UUIDs;

public class BloodReserveStorage implements IStorage<IBloodReserve> {

	@Override
	public NBTBase writeNBT(Capability<IBloodReserve> capability, IBloodReserve instance, EnumFacing side) {
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("max", instance.getMaxBlood());
		data.setInteger("val", instance.getBlood());
		if (instance.getDrinkerUUID() != null) {
			UUIDs.toExistingNBT(instance.getDrinkerUUID(), data);
		}
		return data;
	}

	@Override
	public void readNBT(Capability<IBloodReserve> capability, IBloodReserve instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound data = (NBTTagCompound) nbt;
		instance.setMaxBlood(data.getInteger("max"));
		instance.setBlood(data.getInteger("val"));
		if (data.hasKey("uuidmsb")) {
			instance.setDrinker(UUIDs.fromNBT(data));
		}
	}

}
