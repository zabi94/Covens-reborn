package com.covens.common.core.util.syncTasks;

import java.util.UUID;

import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.util.CreatureSyncHelper;
import com.covens.common.core.util.CreatureSyncHelper.SyncTask;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;

public class DisengageFamiliar extends SyncTask<EntityLiving> {

	UUID eID;
	
	public DisengageFamiliar() {
	}
	
	public DisengageFamiliar(UUID familiarID) {
		eID = familiarID;
	}
	
	@Override
	public void run() {
		EntityLiving p = CreatureSyncHelper.getEntityAcrossDimensions(eID);
		if (p == null) {
			throw new RuntimeException("Entity was not found after sync task was triggered");
		}
		p.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).setOwner(null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		eID = new UUID(nbt.getLong("msb"), nbt.getLong("lsb"));
	}

	@Override
	protected void writeToNBT(NBTTagCompound tag) {
		tag.setLong("msb", eID.getMostSignificantBits());
		tag.setLong("lsb", eID.getLeastSignificantBits());
	}

}
