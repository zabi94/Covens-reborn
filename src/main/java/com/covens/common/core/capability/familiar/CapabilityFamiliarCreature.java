package com.covens.common.core.capability.familiar;

import java.util.UUID;

import javax.annotation.Nullable;

import com.covens.api.familiar.IFamiliarEligible;
import com.covens.api.familiar.IFamiliarUneligible;
import com.covens.common.core.capability.simple.SimpleCapability;
import com.covens.common.core.helper.PlayerHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityFamiliarCreature extends SimpleCapability {
	
	@CapabilityInject(CapabilityFamiliarCreature.class)
	public static final Capability<CapabilityFamiliarCreature> CAPABILITY = null;
	public static final CapabilityFamiliarCreature DEFAULT_INSTANCE = new CapabilityFamiliarCreature();
	
	public UUID owner = new UUID(0, 0);
	public String ownerName = "";
	@DontSync public float bindingChance = 0.01f * rng.nextInt(10);

	public boolean hasOwner() {
		return owner.getLeastSignificantBits() != 0 || owner.getMostSignificantBits() != 0;
	}
	
	public void setOwner(EntityPlayer player) {
		if (player == null) {
			owner = new UUID(0, 0);
			ownerName = "";
		} else {
			owner = EntityPlayer.getUUID(player.getGameProfile());
			ownerName = player.getName();
		}
		markDirty((byte) 1);
	}
	
	@Nullable
	public EntityPlayer getOwner() {
		if (hasOwner()) {
			return PlayerHelper.getPlayerAcrossDimensions(owner);
		}
		return null;
	}
	
	@Nullable
	public String getOwnerName() {
		if (hasOwner()) {
			return ownerName;
		}
		return null;
	}
	
	@Override
	public boolean isRelevantFor(Entity object) {
		return (object instanceof EntityCreature || object instanceof IFamiliarEligible) && !(object instanceof IFamiliarUneligible);
	}

	@Override
	public SimpleCapability getNewInstance() {
		return new CapabilityFamiliarCreature();
	}

}
