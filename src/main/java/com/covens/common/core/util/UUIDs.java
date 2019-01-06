package com.covens.common.core.util;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class UUIDs {
	
	public static final UUID NULL_UUID = new UUID(0, 0);
	
	public static UUID of(Entity e) {
		if (e == null) {
			return NULL_UUID;
		}
		if (e instanceof EntityPlayer) {
			return EntityPlayer.getUUID(((EntityPlayer) e).getGameProfile());
		}
		return e.getPersistentID();
	}
	
	public static NBTTagCompound toNBT(UUID uuid) {
		NBTTagCompound tag = new NBTTagCompound();
		toExistingNBT(uuid, tag);
		return tag;
	}
	
	public static UUID fromNBT(NBTTagCompound tag) {
		UUID uuid = tag.getUniqueId("uuid");
		return uuid == null ? NULL_UUID : uuid;
	}

	public static void toExistingNBT(UUID uuid, NBTTagCompound tag) {
		tag.setUniqueId("uuid", uuid == null ? NULL_UUID : uuid);
	}
}
