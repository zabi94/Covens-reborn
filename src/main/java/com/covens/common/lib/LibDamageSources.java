package com.covens.common.lib;

import net.minecraft.util.DamageSource;

public class LibDamageSources {
	public static final DamageSource BEES = new DamageSource("bw_bees");

	static {
		BEES.setDamageBypassesArmor();
		BEES.setDifficultyScaled();
	}
}
