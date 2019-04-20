package com.covens.common.core.capability.simple;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import zabi.minecraft.minerva.common.capability.SimpleCapability;
import zabi.minecraft.minerva.common.utils.annotation.DontSync;

public class TaglockIncarnation extends SimpleCapability {
	
	@CapabilityInject(TaglockIncarnation.class)
	public static final Capability<TaglockIncarnation> CAPABILITY = null;
	
	@DontSync public int incarnation = 0;
	
	public void changeIncarnation() {
		incarnation++;
	}

	@Override
	public SimpleCapability getNewInstance() {
		return new TaglockIncarnation();
	}

	@Override
	public boolean isRelevantFor(Entity arg0) {
		return arg0 instanceof EntityLivingBase;
	}

}
