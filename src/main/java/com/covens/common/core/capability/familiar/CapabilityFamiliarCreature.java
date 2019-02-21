package com.covens.common.core.capability.familiar;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.api.familiar.IFamiliarEligible;
import com.covens.api.familiar.IFamiliarUneligible;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import zabi.minecraft.minerva.common.capability.SimpleCapability;
import zabi.minecraft.minerva.common.data.UUIDs;
import zabi.minecraft.minerva.common.entity.PlayerHelper;
import zabi.minecraft.minerva.common.utils.annotation.DontSync;
import zabi.minecraft.minerva.common.utils.annotation.Ignore;

public class CapabilityFamiliarCreature extends SimpleCapability {

	@CapabilityInject(CapabilityFamiliarCreature.class)
	public static final Capability<CapabilityFamiliarCreature> CAPABILITY = null;
	public static final CapabilityFamiliarCreature DEFAULT_INSTANCE = new CapabilityFamiliarCreature();
	
	@Ignore public boolean aiSet = false;
	public UUID owner = UUIDs.NULL_UUID;
	public String ownerName = "";
	@Deprecated public boolean sitting = false;
	@DontSync public float bindingChance = 0.01f * rng.nextInt(10);
	@DontSync public UUID target = UUIDs.NULL_UUID;
	@DontSync public BlockPos destination = null;

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
		return ((object instanceof EntityAnimal) || (object instanceof IFamiliarEligible)) && !(object instanceof IFamiliarUneligible) && !(object instanceof IMob);
	}
	
	@Nonnull
	public UUID getTargetUUID() {
		if (target == null) {
			target = UUIDs.NULL_UUID;
		}
		return target;
	}
	
	public static boolean isSitting(EntityLiving e) {
		if (e instanceof EntityTameable) {
			return ((EntityTameable) e).isSitting();
		}
		return e.getCapability(CAPABILITY, null).sitting;
	}
	
	public static void setSitting(EntityLiving e, boolean sitting) {
		if (e instanceof EntityTameable) {
			((EntityTameable) e).setSitting(sitting);
		} else {
			e.getCapability(CAPABILITY, null).sitting = sitting;
			e.getCapability(CAPABILITY, null).markDirty((byte) 5);
		}
	}

	@Override
	public SimpleCapability getNewInstance() {
		return new CapabilityFamiliarCreature();
	}

}
