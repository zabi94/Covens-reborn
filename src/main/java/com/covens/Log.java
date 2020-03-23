package com.covens;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
	
	private static final Logger LOGGER = LogManager.getFormatterLogger(Covens.ID);
	
	public static void i(Object obj) {
		LOGGER.info(obj);
	}
	
	public static void e(Object obj) {
		LOGGER.error(obj);
	}
	
	public static void w(Object obj) {
		LOGGER.warn(obj);
	}
	
	public static void d(Object obj) {
		LOGGER.debug(obj);
	}
	
	public static Logger getLogger() {
		return LOGGER;
	}
}
