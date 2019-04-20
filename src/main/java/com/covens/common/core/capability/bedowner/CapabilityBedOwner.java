package com.covens.common.core.capability.bedowner;

import java.util.Optional;
import java.util.UUID;

import com.covens.common.core.capability.simple.TaglockIncarnation;
import com.covens.common.item.magic.ItemTaglock.TaglockData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import zabi.minecraft.minerva.common.entity.UUIDs;

public class CapabilityBedOwner implements INBTSerializable<NBTTagCompound> {
	
	@CapabilityInject(CapabilityBedOwner.class)
	public static final Capability<CapabilityBedOwner> CAPABILITY = null;
	
	public static void init() {
		CapabilityManager.INSTANCE.register(CapabilityBedOwner.class, new CapabilityBedOwnerStorage(), CapabilityBedOwner::new);
	}
	
	private UUID uuid = UUIDs.NULL_UUID;
	private String name = "";
	private int incarnation = -1;
	
	public void setPlayer(EntityPlayer p) {
		uuid = UUIDs.of(p);
		name = p.getName();
		incarnation = p.getCapability(TaglockIncarnation.CAPABILITY, null).incarnation;
	}
	
	public String getName() {
		return this.name;
	}
	
	public UUID getUuid() {
		return this.uuid;
	}
	
	public Optional<TaglockData> getData() {
		if (UUIDs.isNull(uuid)) {
			return Optional.empty();
		}
		return Optional.of(new TaglockData(name, uuid, incarnation));
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("name", name);
		tag.setUniqueId("id", uuid);
		tag.setInteger("incarnation", incarnation);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		name = nbt.getString("name");
		incarnation = nbt.getInteger("incarnation");
		uuid = nbt.getUniqueId("id");
	}
	
	
}
