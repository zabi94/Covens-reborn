package com.covens.common.core.util.syncTasks;

import java.util.UUID;

import com.covens.api.CovensAPI;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import zabi.minecraft.minerva.common.data.UUIDs;
import zabi.minecraft.minerva.common.entity.EntitySyncHelper.SyncTask;

public class UnbindPlayerFromFamiliar extends SyncTask<EntityPlayer> {

	private UUID pID;
	private UUID fID;

	public UnbindPlayerFromFamiliar() {
	}

	public UnbindPlayerFromFamiliar(UUID player, UUID familiar) {
		this.pID = player;
		this.fID = familiar;
	}

	@Override
	public void execute(EntityPlayer p) {//TODO add checks
		CapabilityFamiliarOwner cap = p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null);
		cap.removeFamiliar(fID);
		CovensAPI.getAPI().removeMPExpansion(cap, p);
		CovensAPI.getAPI().expandPlayerMP(cap, p);
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
