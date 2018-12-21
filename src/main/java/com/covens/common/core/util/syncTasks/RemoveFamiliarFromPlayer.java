package com.covens.common.core.util.syncTasks;

import java.util.UUID;

import com.covens.api.CovensAPI;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.helper.PlayerHelper;
import com.covens.common.core.util.CreatureSyncHelper.SyncTask;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class RemoveFamiliarFromPlayer extends SyncTask<EntityPlayer> {
	
	private UUID pID;

	public RemoveFamiliarFromPlayer() {
	}
	
	public RemoveFamiliarFromPlayer(UUID player) {
		pID = player;
	}
	
	@Override
	public void run() {
		EntityPlayer p = PlayerHelper.getPlayerAcrossDimensions(pID);
		if (p == null) {
			throw new RuntimeException("Player was not found after sync task was triggered");
		}
		CapabilityFamiliarOwner cap = p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null);
		cap.removeFamiliar();
		CovensAPI.getAPI().removeMPExpansion(cap, p);
		CovensAPI.getAPI().expandPlayerMP(cap, p);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		pID = new UUID(nbt.getLong("msb"), nbt.getLong("lsb"));
	}

	@Override
	protected void writeToNBT(NBTTagCompound tag) {
		tag.setLong("msb", pID.getMostSignificantBits());
		tag.setLong("lsb", pID.getLeastSignificantBits());
	}

}
