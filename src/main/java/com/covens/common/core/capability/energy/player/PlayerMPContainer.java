package com.covens.common.core.capability.energy.player;

import com.covens.api.mp.DefaultMPContainer;

public class PlayerMPContainer extends DefaultMPContainer {

	public static final int STARTING_PLAYER_POWER = 800;

	private boolean dirty = false;

	public PlayerMPContainer() {
		super(STARTING_PLAYER_POWER);
	}

	@Override
	public void setAmount(int newAmount) {
		this.dirty = true;
		super.setAmount(newAmount);
	}

	@Override
	public void setMaxAmount(int newMaxAmount) {
		this.dirty = true;
		super.setMaxAmount(newMaxAmount);
	}

	public void setClean() {
		this.dirty = false;
	}

	public boolean isDirty() {
		return this.dirty;
	}

	public void markDirty() {
		this.dirty = true;
	}
}
