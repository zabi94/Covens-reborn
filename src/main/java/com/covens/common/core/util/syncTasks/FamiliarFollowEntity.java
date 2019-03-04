package com.covens.common.core.util.syncTasks;

import java.util.UUID;

import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import zabi.minecraft.minerva.common.entity.EntityHelper;
import zabi.minecraft.minerva.common.entity.UUIDs;
import zabi.minecraft.minerva.common.entity.synchronization.SyncManager;
import zabi.minecraft.minerva.common.entity.synchronization.SyncTask;
import zabi.minecraft.minerva.common.utils.AttributeModifierModeHelper;

public class FamiliarFollowEntity extends SyncTask<EntityLivingBase> {
	
	public static final UUID FOLLOW_RANGE_UUID = UUID.fromString("9a5dc290-90f1-45b3-bb5c-b6f7fa464686");
	private UUID familiar;
	private UUID target;
	
	public FamiliarFollowEntity() {
		// Required
	}
	
	public FamiliarFollowEntity(UUID fam, UUID tgt) {
		this.familiar = fam;
		this.target = tgt;
	}

	@Override
	public void execute(EntityLivingBase famEnt) {
		EntityLivingBase tgtEnt = EntityHelper.getEntityAcrossDimensions(target);
		if (tgtEnt == null) {
			SyncManager.executeOnEntityAvailable(target, new EntityBeFollowedByFamiliar(familiar, target));
		} else {
			CapabilityFamiliarCreature cap = famEnt.getCapability(CapabilityFamiliarCreature.CAPABILITY, null); 
			CapabilityFamiliarCreature.setSitting((EntityLiving) famEnt, false);
			IAttributeInstance follow = famEnt.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
			if (follow != null) {
				follow.removeModifier(FOLLOW_RANGE_UUID);
				follow.applyModifier(new AttributeModifier(FOLLOW_RANGE_UUID, "familiar_increase", 10, AttributeModifierModeHelper.SCALE));
			}
			cap.target = target;
			cap.destination = null;
		}
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
