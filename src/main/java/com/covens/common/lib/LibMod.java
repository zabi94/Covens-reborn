package com.covens.common.lib;

public final class LibMod {

	// ID for MOD
	public static final String MOD_ID = "covens";

	// Name of MOD
	public static final String MOD_NAME = "Covens";

	// Version of MOD
	public static final String MOD_VER = "@VERSION@";

	// Dependency
	public static final String DEPENDENCIES = 
				"required-after:forge@[14.23.4.2796,);" + 
				"required-after:patchouli@[1.0-6,);" +
				"required-after:minerva@[1.0.8,1.0.9);" +
				"after:jei@[4.9.1.168,);" + 
				"after:waila@[1.8.24-B39_1.12,);" + 
				"required-after:baubles@[1.5.2,);" + 
				"after:thaumcraft@[6.1.BETA26,);";

	// Client proxy location
	public static final String PROXY_CLIENT = "com.covens.client.core.ClientProxy";

	// Server proxy location
	public static final String PROXY_COMMON = "com.covens.common.core.proxy.ServerProxy";

	// SHA1 fingerprint
	public static final String FINGERPRINT = "@FINGERPRINT@";

	private LibMod() {
	}
}
