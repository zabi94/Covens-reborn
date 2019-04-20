package com.covens.common.core.util.syncTasks;

import java.util.UUID;

import com.covens.api.CovensAPI;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.lib.LibMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.minerva.common.entity.UUIDs;
import zabi.minecraft.minerva.common.entity.synchronization.SyncTask;

public class UnbindPlayerFromFamiliar extends SyncTask<EntityPlayer> {

	private UUID pID;
	private UUID fID;
	private String name;

	public UnbindPlayerFromFamiliar() {
	}

	public UnbindPlayerFromFamiliar(UUID player, UUID familiar, String name) {
		this.pID = player;
		this.fID = familiar;
		this.name = name;
	}

	@Override
	public void execute(EntityPlayer p) {//TODO add checks
		CapabilityFamiliarOwner cap = p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null);
		cap.removeFamiliar(fID, name);
		CovensAPI.getAPI().removeMPExpansion(new ResourceLocation(LibMod.MOD_ID, "familiar"+fID), p);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.pID = UUIDs.fromNBT(nbt);
	}

	@Override
	protected void writeToNBT(NBTTagCompound tag) {
		UUIDs.toExistingNBT(pID, tag);
	}
}
