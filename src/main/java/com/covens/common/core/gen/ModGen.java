package com.covens.common.core.gen;

import static com.covens.common.core.gen.WorldGenOre.OreGenBuilder.DEFAULT_STATE;

import com.covens.common.block.ModBlocks;
import com.covens.common.block.natural.BlockGem.Gem;
import com.covens.common.core.statics.ModConfig;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.GameRegistry;


public final class ModGen {

	private ModGen() {
	}

	public static void init() {
		// World generation
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.silver_ore, ModConfig.WORLD_GEN.silver.silver_gen_chance).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.silver.silver_min_vein, ModConfig.WORLD_GEN.silver.silver_max_vein).setHeightRange(ModConfig.WORLD_GEN.silver.silver_min_height, ModConfig.WORLD_GEN.silver.silver_max_height).build(DEFAULT_STATE), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.gem_ore, ModConfig.WORLD_GEN.blood_stone.bloodStone_gen_chance).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.blood_stone.bloodStone_min_vein, ModConfig.WORLD_GEN.blood_stone.bloodStone_max_vein).setHeightRange(ModConfig.WORLD_GEN.blood_stone.bloodStone_min_height, ModConfig.WORLD_GEN.blood_stone.bloodStone_max_height).build(block -> Block.getStateById(Gem.BLOODSTONE.ordinal())), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.gem_ore, ModConfig.WORLD_GEN.tourmaline.tourmaline_gen_chance).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.tourmaline.tourmaline_min_vein, ModConfig.WORLD_GEN.tourmaline.tourmaline_max_vein).setHeightRange(ModConfig.WORLD_GEN.tourmaline.tourmaline_min_height, ModConfig.WORLD_GEN.tourmaline.tourmaline_max_height).setBiomes(BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.WET).build(block -> Block.getStateById(Gem.TOURMALINE.ordinal())), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.gem_ore, ModConfig.WORLD_GEN.malachite.malachite_gen_chance).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.malachite.malachite_min_vein, ModConfig.WORLD_GEN.malachite.malachite_max_vein).setHeightRange(ModConfig.WORLD_GEN.malachite.malachite_min_height, ModConfig.WORLD_GEN.malachite.malachite_max_height).setBiomes(BiomeDictionary.Type.COLD, BiomeDictionary.Type.FOREST).build(block -> Block.getStateById(Gem.MALACHITE.ordinal())), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.gem_ore, ModConfig.WORLD_GEN.tigers_eye.tigersEye_gen_chance).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.tigers_eye.tigersEye_min_vein, ModConfig.WORLD_GEN.tigers_eye.tigersEye_max_vein).setHeightRange(ModConfig.WORLD_GEN.tigers_eye.tigersEye_min_height, ModConfig.WORLD_GEN.tigers_eye.tigersEye_max_height).setBiomes(BiomeDictionary.Type.MESA, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SANDY).build(block -> Block.getStateById(Gem.TIGERS_EYE.ordinal())), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.gem_ore, ModConfig.WORLD_GEN.nuummite.nuummite_gen_chance).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.nuummite.nuummite_min_vein, ModConfig.WORLD_GEN.nuummite.nuummite_max_vein).setHeightRange(ModConfig.WORLD_GEN.nuummite.nuummite_min_height, ModConfig.WORLD_GEN.nuummite.nuummite_max_height).setBiomes(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY).build(block -> Block.getStateById(Gem.NUUMMITE.ordinal())), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.gem_ore, ModConfig.WORLD_GEN.garnet.garnet_gen_chance).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.garnet.garnet_min_vein, ModConfig.WORLD_GEN.garnet.garnet_max_vein).setHeightRange(ModConfig.WORLD_GEN.garnet.garnet_min_height, ModConfig.WORLD_GEN.garnet.garnet_max_height).setBiomes(BiomeDictionary.Type.MESA, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SANDY).build(block -> Block.getStateById(Gem.GARNET.ordinal())), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.gem_ore, ModConfig.WORLD_GEN.jasper.jasper_gen_chance).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.jasper.jasper_min_vein, ModConfig.WORLD_GEN.jasper.jasper_max_vein).setHeightRange(ModConfig.WORLD_GEN.jasper.jasper_min_height, ModConfig.WORLD_GEN.jasper.jasper_max_height).setBiomes(BiomeDictionary.Type.SANDY, BiomeDictionary.Type.MESA, BiomeDictionary.Type.DRY).build(block -> Block.getStateById(Gem.JASPER.ordinal())), 0);
		// -------------------salt-------------------//
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.salt_ore, ModConfig.WORLD_GEN.salt.salt_gen_chance).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.salt.salt_min_vein, ModConfig.WORLD_GEN.salt.salt_max_vein).setHeightRange(ModConfig.WORLD_GEN.salt.salt_min_height, ModConfig.WORLD_GEN.salt.salt_max_height).build(DEFAULT_STATE), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.salt_ore, ModConfig.WORLD_GEN.salt.salt_gen_chance * 2).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.salt.salt_min_vein, ModConfig.WORLD_GEN.salt.salt_max_vein).setHeightRange(ModConfig.WORLD_GEN.salt.salt_min_height, ModConfig.WORLD_GEN.salt.salt_max_height).setBiomes(BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HILLS).build(DEFAULT_STATE), 0);
		// -------------------salt-------------------//
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.gem_ore, ModConfig.WORLD_GEN.amethyst.amethyst_gen_chance).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.amethyst.amethyst_min_vein, ModConfig.WORLD_GEN.amethyst.amethyst_max_vein).setHeightRange(ModConfig.WORLD_GEN.amethyst.amethyst_min_height, ModConfig.WORLD_GEN.amethyst.amethyst_max_height).build(block -> Block.getStateById(Gem.AMETHYST.ordinal())), 0);
		GameRegistry.registerWorldGenerator(WorldGenOre.OreGenBuilder.forOre(ModBlocks.gem_ore, ModConfig.WORLD_GEN.alexandrite.alexandrite_gen_chance).generateOn(Blocks.STONE).setVeinSize(ModConfig.WORLD_GEN.alexandrite.alexandrite_min_vein, ModConfig.WORLD_GEN.alexandrite.alexandrite_max_vein).setHeightRange(ModConfig.WORLD_GEN.alexandrite.alexandrite_min_height, ModConfig.WORLD_GEN.alexandrite.alexandrite_max_height).setBiomes(BiomeDictionary.Type.JUNGLE).build(block -> Block.getStateById(Gem.ALEXANDRITE.ordinal())), 0);
		GameRegistry.registerWorldGenerator(new TreeGenerator(), -1);
		GameRegistry.registerWorldGenerator(new WorldGenBeehive(), 0);
	}
}
