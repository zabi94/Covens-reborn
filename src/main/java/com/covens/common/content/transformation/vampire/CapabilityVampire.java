package com.covens.common.content.transformation.vampire;

import com.covens.common.content.transformation.CapabilityTransformation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import zabi.minecraft.minerva.common.capability.SimpleCapability;

public class CapabilityVampire extends SimpleCapability {

	@CapabilityInject(CapabilityVampire.class)
	public static final Capability<CapabilityVampire> CAPABILITY = null;
	public static final CapabilityVampire DEFAULT_INSTANCE = new CapabilityVampire();

	public int blood = 0;
	public boolean nightVision = false;

	@Override
	public boolean isRelevantFor(Entity object) {
		return object instanceof EntityPlayer;
	}

	@Override
	public SimpleCapability getNewInstance() {
		return new CapabilityVampire();
	}

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

	public boolean addVampireBlood(int amount, EntityPlayer p) {
		if ((this.getBlood() >= this.getMaxBlood(p)) && (amount > 0)) {
			return false;
		}
		if ((amount + this.getBlood()) < 0) {
			return false;
		}
		this.blood += amount;
		this.markDirty((byte) 2);
		return true;
	}

	public int getBlood() {
		return this.blood;
	}

	public int getMaxBlood(EntityPlayer p) {
		int max = 50 + (155 * p.getCapability(CapabilityTransformation.CAPABILITY, null).getLevel());
		if (this.getBlood() > max) {
			this.setBlood(max);
		}
		return max; // lvl 0: 50, lvl 5: 825, lvl 10: 1600
	}

	public void setBlood(int blood) {
		this.blood = blood;
		this.markDirty((byte) 2);
	}

}
