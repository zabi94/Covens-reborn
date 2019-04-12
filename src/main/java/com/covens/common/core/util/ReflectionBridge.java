package com.covens.common.core.util;

import java.lang.reflect.Field;
import java.util.Set;

import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ReflectionBridge {
	
	private static final Field executingTaskEntries = ReflectionHelper.findField(EntityAITasks.class, "executingTaskEntries", "field_75780_b");

	@SuppressWarnings("unchecked")
	public static Set<EntityAITasks.EntityAITaskEntry> getExecutingTaskEntries(EntityAITasks tasks) {
		try {
			return (Set<EntityAITaskEntry>) executingTaskEntries.get(tasks);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
	
}
