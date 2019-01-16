package com.covens.common.content.transformation.werewolf;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import zabi.minecraft.minerva.common.capability.SimpleCapability;

public class CapabilityWerewolfStatus extends SimpleCapability {

	@CapabilityInject(CapabilityWerewolfStatus.class)
	public static final Capability<CapabilityWerewolfStatus> CAPABILITY = null;
	public static final CapabilityWerewolfStatus DEFAULT_INSTANCE = new CapabilityWerewolfStatus();

	public int currentWWForm = 0; // 0 = none, 1 = wolf, 2 = wolfman
	public int texture = 0;
	public boolean nightVision = false;

	public void setNightVision(boolean b) {
		boolean changed = false;
		if (this.nightVision != b) {
			changed = true;
		}
		this.nightVision = b;
		if (changed) {
			this.markDirty((byte) 1);
		}
	}

	public void changeForm(boolean backwards) {
		if (backwards) {
			if (this.currentWWForm == 0) {
				this.currentWWForm = 2;
			} else {
				this.currentWWForm = (this.currentWWForm - 1);
			}
		} else {
			this.currentWWForm = (this.currentWWForm + 1) % 3;
		}
		this.markDirty((byte) 2);
	}

	@Override
	public boolean isRelevantFor(Entity object) {
		return object instanceof EntityPlayer;
	}

	@Override
	public SimpleCapability getNewInstance() {
		return new CapabilityWerewolfStatus();
	}

}
