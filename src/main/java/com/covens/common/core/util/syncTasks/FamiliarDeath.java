package com.covens.common.core.util.syncTasks;

import java.util.UUID;

import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import zabi.minecraft.minerva.common.entity.UUIDs;
import zabi.minecraft.minerva.common.entity.synchronization.SyncTask;

public class FamiliarDeath extends SyncTask<EntityPlayer> {
	
	private UUID fam;
	private String name;
	
	public FamiliarDeath() {
		// Required
	}
	
	public FamiliarDeath(UUID familiar, String nameIn) {
		fam = familiar;
		name = nameIn;
	}

	@Override
	public void execute(EntityPlayer p) {
		p.sendStatusMessage(new TextComponentTranslation("familiar.dead", name), true);
		p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).removeFamiliar(fam, name);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		name = nbt.getString("name");
		fam = UUIDs.fromNBT(nbt);
	}

	@Override
	protected void writeToNBT(NBTTagCompound nbt) {
		UUIDs.toExistingNBT(fam, nbt);
		nbt.setString("name", name);
	}

}
