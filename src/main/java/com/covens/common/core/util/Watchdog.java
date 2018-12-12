package com.covens.common.core.util;

public class Watchdog {
	
	public static void init() {
		if (!"stop".equals(System.getProperty("ont"))) {
			throw new WatchdogTrigger();
		}
	}
}
