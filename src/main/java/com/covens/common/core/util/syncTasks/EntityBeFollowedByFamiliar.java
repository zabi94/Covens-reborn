package com.covens.common.core.util.syncTasks;

import java.util.UUID;

import com.covens.common.core.util.EntitySyncHelper;
import com.covens.common.core.util.EntitySyncHelper.SyncTask;
import com.covens.common.core.util.UUIDs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class EntityBeFollowedByFamiliar extends SyncTask<EntityLivingBase> {

	private UUID familiar;
	private UUID target;
	
	public EntityBeFollowedByFamiliar(UUID fam, UUID tgt) {
		this.familiar = fam;
		this.target = tgt;
	}
	
	@Override
	public void execute(EntityLivingBase entity) {
		EntitySyncHelper.executeOnEntityAvailable(familiar, new FamiliarFollowEntity(familiar, target));
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		familiar = UUIDs.fromNBT(nbt.getCompoundTag("familiar"));
		target = UUIDs.fromNBT(nbt.getCompoundTag("target"));
	}

	@Override
	protected void writeToNBT(NBTTagCompound tag) {
		tag.setTag("familiar", UUIDs.toNBT(familiar));
		tag.setTag("target", UUIDs.toNBT(target));
	}
}
