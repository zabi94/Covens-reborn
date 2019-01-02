package com.covens.common.content.crystalBall.capability;

import javax.annotation.Nullable;

import com.covens.api.divination.IFortune;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public interface CapabilityFortune {

	@CapabilityInject(CapabilityFortune.class)
	public static final Capability<CapabilityFortune> CAPABILITY = null;

	public static void init() {
		CapabilityManager.INSTANCE.register(CapabilityFortune.class, new FortuneCapabilityStorage(), Impl::new);
	}

	public void setFortune(@Nullable IFortune fortune);

	@Nullable
	public IFortune getFortune();

	public void setActive();

	public void setRemovable();

	public boolean isActive();

	public boolean isRemovable();

	public static class Impl implements CapabilityFortune {

		private IFortune fortune = null;
		private boolean active = false, removable = false;

		@Override
		public void setFortune(IFortune fortune) {
			this.fortune = fortune;
			active = false;
			removable = false;
		}

		@Override
		public IFortune getFortune() {
			return fortune;
		}

		@Override
		public void setActive() {
			active = true;
		}

		@Override
		public void setRemovable() {
			removable = true;
		}

		@Override
		public boolean isActive() {
			return active;
		}

		@Override
		public boolean isRemovable() {
			return removable;
		}
	}
}
