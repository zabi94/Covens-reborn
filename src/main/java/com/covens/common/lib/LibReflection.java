package com.covens.common.lib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class LibReflection {
	
	public static final String NETWORK_PLAYER_INFO__PLAYER_TEXTURES = "field_187107_a";
	
	public static Method method(String name_mcp, String name_srg, Class<?> clazz, Class<?> returns, Class<?>... args) {
		try {
			return ObfuscationReflectionHelper.findMethod(clazz, name_srg, returns, args);
		} catch (Exception e) {
			return ObfuscationReflectionHelper.findMethod(clazz, name_mcp, returns, args);
		}
	}
	
	public static Field field(String name_mcp, String name_srg, Class<?> clazz) {
		try {
			return ObfuscationReflectionHelper.findField(clazz, name_srg);
		} catch (Exception e) {
			return ObfuscationReflectionHelper.findField(clazz, name_mcp);
		}
	}
}
