package com.covens.common.core.capability.mimic;

import java.util.UUID;

import com.covens.common.Covens;
import com.covens.common.core.util.UUIDs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityMimicData implements IMimicData {

	@CapabilityInject(IMimicData.class)
	public static final Capability<IMimicData> CAPABILITY = null;

	private boolean mimicking;
	private UUID mimickedPlayerID;
	private String mimickedPlayerName;

	public CapabilityMimicData() {
		this.mimicking = false;
		this.mimickedPlayerID = UUIDs.NULL_UUID;
		this.mimickedPlayerName = "";
	}

	public static void init() {
		CapabilityManager.INSTANCE.register(IMimicData.class, new MimicDataStorage(), CapabilityMimicData::new);
	}

	@Override
	public boolean isMimicking() {
		return this.mimicking;
	}

	@Override
	public void setMimicking(boolean mimickingIn, EntityPlayer p) {
		this.setMimickingDirect(mimickingIn);
		if (!mimickingIn) {
			Covens.proxy.stopMimicking(p);
		}
	}

	@Override
	public UUID getMimickedPlayerID() {
		return this.mimickedPlayerID;
	}

	@Override
	public void setMimickedPlayerID(UUID mimickedPlayerID) {
		this.mimickedPlayerID = mimickedPlayerID;
	}

	@Override
	public String getMimickedPlayerName() {
		return this.mimickedPlayerName;
	}

	@Override
	public void setMimickedPlayerName(String mimickedPlayerName) {
		this.mimickedPlayerName = mimickedPlayerName;
	}

	/**
	 * Calling this won't call cleanup methods, and should only be used when
	 * restoring data from NBT Prefer the use of
	 * {@link IMimicData#setMimicking(boolean, EntityPlayer)}
	 */
	@Override
	public void setMimickingDirect(boolean mimickingIn) {
		this.mimicking = mimickingIn;
	}
}
