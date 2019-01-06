package com.covens.common.core.util.syncTasks;

import java.util.UUID;

import com.covens.api.CovensAPI;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.util.EntitySyncHelper.SyncTask;
import com.covens.common.core.util.UUIDs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class UnbindPlayerFromFamiliar extends SyncTask<EntityPlayer> {

	private UUID pID;
	private UUID fID;
	private EntityPlayer p = null;

	public UnbindPlayerFromFamiliar() {
	}

	public UnbindPlayerFromFamiliar(UUID player, UUID familiar) {
		this.pID = player;
		this.fID = familiar;
	}

	@Override
	public void run() {//TODO add checks
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
	
	@Override
	protected void onEntityConnected(EntityLivingBase entity) {
		p = (EntityPlayer) entity;
	}

}
