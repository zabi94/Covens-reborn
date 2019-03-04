package com.covens.common.core.util.syncTasks;

import java.util.UUID;

import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import zabi.minecraft.minerva.common.entity.UUIDs;
import zabi.minecraft.minerva.common.entity.synchronization.SyncTask;

public class UnbindFamiliarFromPlayer extends SyncTask<EntityLivingBase> {

	private UUID eID;
	
	public UnbindFamiliarFromPlayer() {
	}

	public UnbindFamiliarFromPlayer(UUID familiarID) {
		this.eID = familiarID;
	}

	@Override
	public void execute(EntityLivingBase p) {//TODO add checks
		p.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).setOwner(null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.eID = UUIDs.fromNBT(nbt);
	}

	@Override
	protected void writeToNBT(NBTTagCompound tag) {
		UUIDs.toExistingNBT(this.eID, tag);
	}
}
