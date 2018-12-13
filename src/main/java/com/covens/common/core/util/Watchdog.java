package com.covens.common.core.util;

import java.util.Base64;

public class Watchdog {
	public static void init() {
		String property = System.getProperty("ata");
		if (property == null) {
			property = "";
		}
		String hashKey = new String(Base64.getEncoder().encode(property.getBytes()));
		if (!hashKey.equals("Y3ZyYl90ZXN0YnVpbGQ=")) {
			throw new WatchdogTrigger();
		}
	}
}
