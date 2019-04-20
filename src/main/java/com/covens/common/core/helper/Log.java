package com.covens.common.core.helper;

import static com.covens.common.lib.LibMod.MOD_NAME;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
	
	private static final Logger logger = LogManager.getLogger(MOD_NAME);

	public static void i(String s) {
		logger.info(s);
	}

	public static void w(String s) {
		logger.warn(s);
	}

	public static void e(String s) {
		logger.error(s);
	}

	public static void i(Object s) {
		logger.info(s);
	}

	public static void w(Object s) {
		logger.warn(s);
	}

	public static void e(Object s) {
		logger.error(s);
	}
	
	public static void bigMessage(Object s) {
		i("**********");
		i("");
		i("");
		i("");
		i(s);
		i("");
		i("");
		i("");
		i("**********");
	}

	public static void askForReport() {
		StringBuilder sb = new StringBuilder();
		String s = "This is a bug in the mod Covens. Update it or report it if already using the latest version";
		for (int i = 0; i < (s.length() + 10); i++) {
			sb.append("#");
		}
		String frame = sb.toString();
		e(frame);
		e("#    " + s + "    #");
		e(frame);
		Thread.dumpStack();
	}

	public static void d(String s) {
		if ("true".equals(System.getProperty("debug"))) {
			i("[DEBUG] -- " + s);
		} else {
			logger.debug(s);
		}
	}
}
