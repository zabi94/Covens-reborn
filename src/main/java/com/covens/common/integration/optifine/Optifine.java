package com.covens.common.integration.optifine;

import net.minecraftforge.fml.common.Loader;

public class Optifine {
	private static boolean isLoaded = false;
	
	public static void init() {
		isLoaded = Loader.isModLoaded("optifine");
	}
	
	public static boolean isLoaded() {
		return isLoaded;
	}
}
