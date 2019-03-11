package com.covens.common.core.capability.energy;

import com.covens.api.mp.MPUsingItem;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class MagicPowerUsingItem {
	private static final MPUsingItem INSTANCE = new MPUsingItem() {
	};

	public static void init() {
		CapabilityManager.INSTANCE.register(MPUsingItem.class, new MPUsingItem.Storage(), () -> INSTANCE);
	}
}
