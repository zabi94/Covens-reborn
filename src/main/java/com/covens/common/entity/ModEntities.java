package com.covens.common.entity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.covens.common.Covens;
import com.covens.common.core.helper.Log;
import com.covens.common.entity.living.animals.EntityLizard;
import com.covens.common.entity.living.animals.EntityOwl;
import com.covens.common.entity.living.animals.EntityRaven;
import com.covens.common.entity.living.animals.EntitySnake;
import com.covens.common.entity.living.animals.EntityToad;
import com.covens.common.lib.LibMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public final class ModEntities {

	private ModEntities() {
	}

	public static void init() {
		// Utility entities
		registerEntity("spell_carrier", EntitySpellCarrier.class, 64, 1, true);
		registerEntity("broom", EntityFlyingBroom.class, 64, 1, true);
		registerEntity("swarm", EntityBatSwarm.class, 64, 1, true);
		registerEntity("brew_arrow", EntityBrewArrow.class, 64, 1, true);
		registerEntity("brew_bottle", EntityBrew.class, 64, 1, true);
		registerEntity("brew_lingering_effect", EntityLingeringBrew.class, 64, 100, false);
		registerEntity("brew_aoe_effect", EntityAoE.class, 64, 100, false);

		// Mob entities
		registerEntity("owl", EntityOwl.class, 64, 1, true, 0xAF813F, 0x6E5127);
		registerEntity("snake", EntitySnake.class, 64, 1, true, 0x8F9779, 0x696969);
		registerEntity("raven", EntityRaven.class, 64, 1, true, 0x222222, 0x280638);
		registerEntity("toad", EntityToad.class, 64, 1, true, 0xA9BA9D, 0xC3B091);
		registerEntity("lizard", EntityLizard.class, 64, 1, true, 0x568203, 0x0070BB);

		List<Biome> validOwl = BiomeDictionary.getBiomes(Type.FOREST).stream()
				.filter(b -> BiomeDictionary.hasType(b, Type.DENSE))
				.peek(b -> Log.d("Valid owl biome found: " + b.getRegistryName()))
				.collect(Collectors.toList());
		List<Biome> validSnake = Biome.REGISTRY.getKeys().stream()
				.map(rl -> Biome.REGISTRY.getObject(rl))
				.filter(b -> BiomeDictionary.hasType(b, Type.PLAINS) || BiomeDictionary.hasType(b, Type.HILLS))
				.peek(b -> Log.d("Valid snake biome found: " + b.getRegistryName()))
				.collect(Collectors.toList());

		Set<Biome> validToad = BiomeDictionary.getBiomes(Type.SWAMP);
		validToad.forEach(b -> Log.d("Valid toad biome found: " + b.getRegistryName()));

		List<Biome> validRaven = Biome.REGISTRY.getKeys().stream()
				.map(rl -> Biome.REGISTRY.getObject(rl))
				.filter(b -> BiomeDictionary.hasType(b, Type.PLAINS) || BiomeDictionary.hasType(b, Type.WASTELAND))
				.peek(b -> Log.d("Valid raven biome found: " + b.getRegistryName()))
				.collect(Collectors.toList());

		Set<Biome> validLizard = BiomeDictionary.getBiomes(Type.FOREST);
		validLizard.forEach(b -> Log.d("Valid lizard biome found: " + b.getRegistryName()));

		Biome[] biomesOwl = new Biome[validOwl.size()];
		Biome[] biomesSnake = new Biome[validSnake.size()];
		Biome[] biomesToad = new Biome[validToad.size()];
		Biome[] biomesRaven = new Biome[validRaven.size()];
		Biome[] biomesLizard = new Biome[validLizard.size()];

		EntityRegistry.addSpawn(EntityOwl.class, 8, 1, 1, EnumCreatureType.CREATURE, validOwl.toArray(biomesOwl));
		EntityRegistry.addSpawn(EntitySnake.class, 5, 1, 1, EnumCreatureType.CREATURE, validSnake.toArray(biomesSnake));
		EntityRegistry.addSpawn(EntityToad.class, 2, 1, 1, EnumCreatureType.CREATURE, validToad.toArray(biomesToad));
		EntityRegistry.addSpawn(EntityRaven.class, 3, 1, 1, EnumCreatureType.CREATURE, validRaven.toArray(biomesRaven));
		EntityRegistry.addSpawn(EntityLizard.class, 4, 1, 1, EnumCreatureType.CREATURE, validLizard.toArray(biomesLizard));
	}

	private static ResourceLocation getResource(String name) {
		return new ResourceLocation(LibMod.MOD_ID, name);
	}
	
	private static int id = 0;
	
	private static void registerEntity(String name, Class<? extends Entity> clazz, int track_range, int update_frewuency, boolean sends_updates, int eggColor, int secundaryEggColor) {
		checkContructor(clazz);
		EntityRegistry.registerModEntity(getResource(name), clazz, name, id++, Covens.instance, track_range, update_frewuency, sends_updates, eggColor, secundaryEggColor);
	}
	
	private static void registerEntity(String name, Class<? extends Entity> clazz, int track_range, int update_frewuency, boolean sends_updates) {
		checkContructor(clazz);
		EntityRegistry.registerModEntity(getResource(name), clazz, name, id++, Covens.instance, track_range, update_frewuency, sends_updates);
	}
	
	private static void checkContructor(Class<? extends Entity> clazz) {
		try {
			clazz.getConstructor(World.class);
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodError("You need a contructor that takes a world object for the entity "+clazz.getCanonicalName());
		} catch (SecurityException e) {
			throw new RuntimeException("Exception during entity registration", e);
		}
	}
}
