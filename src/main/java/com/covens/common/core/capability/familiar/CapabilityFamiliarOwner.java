package com.covens.common.core.capability.familiar;

import com.covens.api.mp.IMagicPowerExpander;
import com.covens.common.core.capability.simple.SimpleCapability;
import com.covens.common.lib.LibMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityFamiliarOwner extends SimpleCapability implements IMagicPowerExpander {

	@CapabilityInject(CapabilityFamiliarOwner.class)
	public static final Capability<CapabilityFamiliarOwner> CAPABILITY = null;
	public static final CapabilityFamiliarOwner DEFAULT_INSTANCE = new CapabilityFamiliarOwner();
	private static final ResourceLocation EXPANDER_ID = new ResourceLocation(LibMod.MOD_ID, "familiars");

	@DontSync
	public int familiarCount = 0;

	public void addFamiliar() {
		this.familiarCount++;
	}

	public void removeFamiliar() {
		this.familiarCount--;
	}

	@Override
	public boolean isRelevantFor(Entity object) {
		return object instanceof EntityPlayer;
	}

	@Override
	public SimpleCapability getNewInstance() {
		return new CapabilityFamiliarOwner();
	}

	@Override
	public ResourceLocation getID() {
		return EXPANDER_ID;
	}

	@Override
	public int getExtraAmount(EntityPlayer p) {
		return this.familiarCount * 100;
	}

}
