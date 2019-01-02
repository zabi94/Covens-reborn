package com.covens.common.core.util;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderException;

public class Watchdog {
	public static void init() {
		// OpenEye uses non lowercase modid, I can't put it inside the standard deps
		if (!"true".equals(System.getProperty("ignoreMissingOpenEye")) && !Loader.isModLoaded("OpenEye")) {
			LoaderException l = new LoaderException("\n\n\n\n\nMod OpenEye is required for alpha testing of Covens Reborn\n\n\n\n\n");
			l.setStackTrace(new StackTraceElement[0]);
			throw l;
		}
	}
}
