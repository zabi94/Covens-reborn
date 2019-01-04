package com.covens.common.core.util.syncTasks;

import java.util.UUID;

import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.util.EntitySyncHelper.SyncTask;

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
		this.eID = new UUID(nbt.getLong("msb"), nbt.getLong("lsb"));
	}

	@Override
	protected void writeToNBT(NBTTagCompound tag) {
		tag.setLong("msb", this.eID.getMostSignificantBits());
		tag.setLong("lsb", this.eID.getLeastSignificantBits());
	}

	@Override
	protected void onEntityConnected(EntityLivingBase entity) {
		p = (EntityLiving) entity;
	}

}
