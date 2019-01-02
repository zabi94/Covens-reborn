package com.covens.common.integration.thaumcraft;

import net.minecraftforge.fml.common.Loader;

public class ThaumcraftLoader {

	private static final String THAUMCRAFT_ID = "thaumcraft";

	@SuppressWarnings("deprecation")
	public static void init() {
		if (Loader.isModLoaded(THAUMCRAFT_ID)) {
			ThaumcraftInternal.init();
		}
	}
}
