package com.covens.common.core.helper;

import com.covens.common.Covens;

public class Log {

	public static void i(String s) {
		Covens.logger.info(s);
	}

	public static void w(String s) {
		Covens.logger.warn(s);
	}

	public static void e(String s) {
		Covens.logger.error(s);
	}

	public static void i(Object s) {
		Covens.logger.info(s);
	}

	public static void w(Object s) {
		Covens.logger.warn(s);
	}

	public static void e(Object s) {
		Covens.logger.error(s);
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
			Covens.logger.debug(s);
		}
	}
}
