package com.covens.common.crafting;

import static com.covens.common.lib.LibIngredients.feather;
import static com.covens.common.lib.LibIngredients.fumeCleansingAura;
import static com.covens.common.lib.LibIngredients.fumeEmanationDishonesty;
import static com.covens.common.lib.LibIngredients.fumeEverchangingPresence;
import static com.covens.common.lib.LibIngredients.fumeFieryBreeze;
import static com.covens.common.lib.LibIngredients.fumeUndyingImage;
import static com.covens.common.lib.LibIngredients.hay;
import static com.covens.common.lib.LibIngredients.soul_string;
import static com.covens.common.lib.LibIngredients.spider_web;
import static com.covens.common.lib.LibIngredients.string;
import static com.covens.common.lib.LibIngredients.wheat;
import static com.covens.common.lib.LibIngredients.witches_stitching;

import com.covens.common.item.ModItems;
import com.covens.common.lib.LibMod;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModSpinningThreadRecipes {

	public static void init() {
		SpinningThreadRecipe.REGISTRY.registerAll(
				new SpinningThreadRecipe(rl("spider_web"),  new ItemStack(Blocks.WEB), string, string, string), 
				new SpinningThreadRecipe(rl("regal_silk"), new ItemStack(ModItems.regal_silk, 12, 0), feather, spider_web, spider_web, fumeEverchangingPresence), 
				new SpinningThreadRecipe(rl("gold_thread"), new ItemStack(ModItems.golden_thread, 3, 0), wheat, wheat, hay, fumeEverchangingPresence), 
				new SpinningThreadRecipe(rl("witches_stitching"), new ItemStack(ModItems.witches_stitching, 4), string, string, fumeEverchangingPresence, fumeEverchangingPresence), 
				new SpinningThreadRecipe(rl("diabolic_vein"),  new ItemStack(ModItems.diabolic_vein, 4), soul_string, soul_string, fumeEmanationDishonesty, fumeFieryBreeze), 
				new SpinningThreadRecipe(rl("pure_filament"), new ItemStack(ModItems.pure_filament, 4), witches_stitching, witches_stitching, fumeCleansingAura, fumeCleansingAura), 
				new SpinningThreadRecipe(rl("soulstring"), new ItemStack(ModItems.soul_string, 2), witches_stitching, witches_stitching, fumeUndyingImage));
	}

	private static ResourceLocation rl(String name) {
		return new ResourceLocation(LibMod.MOD_ID, name);
	}
}
