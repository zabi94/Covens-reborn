package com.covens.client.core.event.custom;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class MimicEvent extends PlayerEvent {
	private UUID victimID;
	private String victimName;
	private boolean reverting;

	public MimicEvent(EntityPlayer player, UUID victimID, String victimName, boolean reverting) {
		super(player);
		this.victimID = victimID;
		this.victimName = victimName;
		this.reverting = reverting;
	}

	public UUID getVictimID() {
		return this.victimID;
	}

	public String getVictimName() {
		return this.victimName;
	}

	public boolean isReverting() {
		return this.reverting;
	}
}
