package com.covens.common.core.util;

public class WatchdogTrigger extends Error {

	private static final long serialVersionUID = -8618067098472058570L;
	
	public WatchdogTrigger() {
		super("Watchdog triggered", null, true, true);
		this.setStackTrace(new StackTraceElement[0]);
	}
}