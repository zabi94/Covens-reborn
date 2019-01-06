package com.covens.common.core.util.syncTasks;

import java.util.UUID;

import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.util.EntitySyncHelper.SyncTask;
import com.covens.common.core.util.UUIDs;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class UnbindFamiliarFromPlayer extends SyncTask<EntityLiving> {

	private UUID eID;
	
	private EntityLiving p = null;

	public UnbindFamiliarFromPlayer() {
	}

	public UnbindFamiliarFromPlayer(UUID familiarID) {
		this.eID = familiarID;
	}

	@Override
	public void run() {//TODO add checks
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

	@Override
	protected void onEntityConnected(EntityLivingBase entity) {
		p = (EntityLiving) entity;
	}

}
