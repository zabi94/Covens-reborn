package com.covens.common.core.gen;

import static com.covens.common.core.gen.WorldGenOre.OreGenBuilder.DEFAULT_STATE;

import com.covens.common.block.ModBlocks;
import com.covens.common.core.statics.ModConfig;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.GameRegistry;


public final class ModGen {

	private ModGen() {
	}

	public static void init() {
		// World generation
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder
				.forOre(ModBlocks.silver_ore, ModConfig.WORLD_GEN.silver.silver_gen_chance)
				.generateOn(Blocks.STONE)
				.setVeinSize(ModConfig.WORLD_GEN.silver.silver_min_vein, ModConfig.WORLD_GEN.silver.silver_max_vein)
				.setHeightRange(ModConfig.WORLD_GEN.silver.silver_min_height, ModConfig.WORLD_GEN.silver.silver_max_height)
				.build(DEFAULT_STATE), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder
				.forOre(ModBlocks.tourmaline_ore, ModConfig.WORLD_GEN.tourmaline.tourmaline_gen_chance)
				.generateOn(Blocks.STONE)
				.setVeinSize(ModConfig.WORLD_GEN.tourmaline.tourmaline_min_vein, ModConfig.WORLD_GEN.tourmaline.tourmaline_max_vein)
				.setHeightRange(ModConfig.WORLD_GEN.tourmaline.tourmaline_min_height, ModConfig.WORLD_GEN.tourmaline.tourmaline_max_height)
				.setBiomes(BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.WET)
				.build(DEFAULT_STATE), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder
				.forOre(ModBlocks.malachite_ore, ModConfig.WORLD_GEN.malachite.malachite_gen_chance)
				.generateOn(Blocks.STONE)
				.setVeinSize(ModConfig.WORLD_GEN.malachite.malachite_min_vein, ModConfig.WORLD_GEN.malachite.malachite_max_vein)
				.setHeightRange(ModConfig.WORLD_GEN.malachite.malachite_min_height, ModConfig.WORLD_GEN.malachite.malachite_max_height)
				.setBiomes(BiomeDictionary.Type.COLD, BiomeDictionary.Type.FOREST)
				.build(DEFAULT_STATE), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder
				.forOre(ModBlocks.tigers_eye_ore, ModConfig.WORLD_GEN.tigers_eye.tigersEye_gen_chance)
				.generateOn(Blocks.STONE)
				.setVeinSize(ModConfig.WORLD_GEN.tigers_eye.tigersEye_min_vein, ModConfig.WORLD_GEN.tigers_eye.tigersEye_max_vein)
				.setHeightRange(ModConfig.WORLD_GEN.tigers_eye.tigersEye_min_height, ModConfig.WORLD_GEN.tigers_eye.tigersEye_max_height)
				.setBiomes(BiomeDictionary.Type.MESA, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SANDY)
				.build(DEFAULT_STATE), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder
				.forOre(ModBlocks.garnet_ore, ModConfig.WORLD_GEN.garnet.garnet_gen_chance)
				.generateOn(Blocks.STONE)
				.setVeinSize(ModConfig.WORLD_GEN.garnet.garnet_min_vein, ModConfig.WORLD_GEN.garnet.garnet_max_vein)
				.setHeightRange(ModConfig.WORLD_GEN.garnet.garnet_min_height, ModConfig.WORLD_GEN.garnet.garnet_max_height)
				.setBiomes(BiomeDictionary.Type.MESA, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SANDY)
				.build(DEFAULT_STATE), 0);
		// -------------------salt-------------------//
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder
				.forOre(ModBlocks.salt_ore, ModConfig.WORLD_GEN.salt.salt_gen_chance)
				.generateOn(Blocks.STONE)
				.setVeinSize(ModConfig.WORLD_GEN.salt.salt_min_vein, ModConfig.WORLD_GEN.salt.salt_max_vein)
				.setHeightRange(ModConfig.WORLD_GEN.salt.salt_min_height, ModConfig.WORLD_GEN.salt.salt_max_height)
				.build(DEFAULT_STATE), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder
				.forOre(ModBlocks.salt_ore, ModConfig.WORLD_GEN.salt.salt_gen_chance * 2)
				.generateOn(Blocks.STONE)
				.setVeinSize(ModConfig.WORLD_GEN.salt.salt_min_vein, ModConfig.WORLD_GEN.salt.salt_max_vein)
				.setHeightRange(ModConfig.WORLD_GEN.salt.salt_min_height, ModConfig.WORLD_GEN.salt.salt_max_height)
				.setBiomes(BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS)
				.build(DEFAULT_STATE), 0);
		// -------------------salt-------------------//
		GameRegistry.registerWorldGenerator(new TreeGenerator(), -1);
		GameRegistry.registerWorldGenerator(new WorldGenBeehive(), 0);
	}
}
