package com.covens.common.core.util.syncTasks;

import java.util.HashMap;
import java.util.UUID;

import com.covens.common.content.familiar.FamiliarDescriptor;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.helper.Log;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import zabi.minecraft.minerva.common.entity.synchronization.SyncTask;
import zabi.minecraft.minerva.common.utils.DimensionalPosition;

public class FamiliarPingPosition extends SyncTask<EntityPlayer> {
	
	private UUID familiar;
	private DimensionalPosition position;
	
	public FamiliarPingPosition() {
		// Required
	}
	
	public FamiliarPingPosition(UUID fam, DimensionalPosition pos) {
		familiar = fam;
		position = pos;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		familiar = nbt.getUniqueId("id");
		position = new DimensionalPosition(nbt.getCompoundTag("pos"));
	}

	@Override
	protected void execute(EntityPlayer p) {
		HashMap<UUID, FamiliarDescriptor> hmap = p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).familiars;
		FamiliarDescriptor old = hmap.get(familiar);
		FamiliarDescriptor newFD = new FamiliarDescriptor(old.getName(), old.getUuid(), position, old.isAvailable());
		Log.i(old + " --> " + newFD);
		hmap.remove(familiar);
		hmap.put(familiar, newFD);
	}

	@Override
	protected void writeToNBT(NBTTagCompound tag) {
		tag.setUniqueId("id", familiar);
		tag.setTag("pos", position.writeToNBT());
	}
}
