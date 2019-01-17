package com.covens.common.core.util.syncTasks;

import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import zabi.minecraft.minerva.common.utils.entity.EntitySyncHelper.SyncTask;

public class FamiliarOrderGoto extends SyncTask<EntityLivingBase> {
	
	private BlockPos dest;
	
	public FamiliarOrderGoto() {
		// Required
	}

	public FamiliarOrderGoto(BlockPos destination) {
		this.dest = destination;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		dest = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
	}

	@Override
	protected void execute(EntityLivingBase familiar) {
		familiar.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).destination = dest;
	}

	@Override
	protected void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("x", dest.getX());
		nbt.setInteger("y", dest.getY());
		nbt.setInteger("z", dest.getZ());
	}

}
