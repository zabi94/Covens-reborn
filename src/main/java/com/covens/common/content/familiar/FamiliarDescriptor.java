package com.covens.common.content.familiar;

import java.util.UUID;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import zabi.minecraft.minerva.common.entity.UUIDs;
import zabi.minecraft.minerva.common.utils.DimensionalPosition;

public class FamiliarDescriptor implements INBTSerializable<NBTTagCompound> {
	
	private String name;
	private UUID uuid;
	private boolean available;
	private DimensionalPosition lastKnownPos;
	
	public FamiliarDescriptor(String name, UUID uuid, DimensionalPosition lastSeen, boolean available) {
		this.name = name;
		this.uuid = uuid;
		this.available = available;
		this.lastKnownPos = lastSeen;
	}
	
	public FamiliarDescriptor(NBTTagCompound tag) {
		deserializeNBT(tag);
	}

	public DimensionalPosition getLastKnownPos() {
		return this.lastKnownPos;
	}

	public String getName() {
		return this.name;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public boolean isAvailable() {
		return this.available;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("name", name);
		tag.setUniqueId("uuid", uuid);
		tag.setBoolean("available", available);
		tag.setTag("position", lastKnownPos.writeToNBT());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		name = nbt.getString("name");
		uuid = nbt.getUniqueId("uuid");
		available = nbt.getBoolean("available");
		lastKnownPos = new DimensionalPosition(nbt.getCompoundTag("position"));
	}

	public static FamiliarDescriptor of(EntityLiving in) {
		return new FamiliarDescriptor(in.getName(), UUIDs.of(in), new DimensionalPosition(in), false);
	}
	
	@Override
	public String toString() {
		return "Familiar:"+name+"|"+uuid+"|"+lastKnownPos+(available?"":"|unavailable");
	}
	
}