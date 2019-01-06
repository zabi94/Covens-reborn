package com.covens.common.core.capability.familiar;

import java.util.UUID;

import javax.annotation.Nullable;

import com.covens.api.familiar.IFamiliarEligible;
import com.covens.api.familiar.IFamiliarUneligible;
import com.covens.common.core.capability.simple.SimpleCapability;
import com.covens.common.core.helper.PlayerHelper;
import com.covens.common.core.util.UUIDs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityFamiliarCreature extends SimpleCapability {

	@CapabilityInject(CapabilityFamiliarCreature.class)
	public static final Capability<CapabilityFamiliarCreature> CAPABILITY = null;
	public static final CapabilityFamiliarCreature DEFAULT_INSTANCE = new CapabilityFamiliarCreature();

	public UUID owner = UUIDs.NULL_UUID;
	public String ownerName = "";
	@DontSync
	public float bindingChance = 0.01f * rng.nextInt(10);
	@DontSync
	public UUID target = UUIDs.NULL_UUID;

	public boolean hasOwner() {
		return !owner.equals(UUIDs.NULL_UUID);
	}

	public void setOwner(EntityPlayer player) {
		this.owner = UUIDs.of(player);
		if (player == null) {
			this.ownerName = "";
		} else {
			this.ownerName = player.getName();
		}
		this.markDirty((byte) 1);
	}

	@Nullable
	public EntityPlayer getOwner() {
		if (this.hasOwner()) {
			return PlayerHelper.getPlayerAcrossDimensions(this.owner);
		}
		return null;
	}

	@Nullable
	public String getOwnerName() {
		if (this.hasOwner()) {
			return this.ownerName;
		}
		return null;
	}

	@Override
	public boolean isRelevantFor(Entity object) {
		return ((object instanceof EntityCreature) || (object instanceof IFamiliarEligible)) && !(object instanceof IFamiliarUneligible);
	}

	@Override
	public SimpleCapability getNewInstance() {
		return new CapabilityFamiliarCreature();
	}

}
