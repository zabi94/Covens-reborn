package com.covens.common.crafting;

import com.covens.common.item.ModItems;
import com.covens.common.lib.LibIngredients;
import com.covens.common.lib.LibMod;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class ModDistilleryRecipes {

	private static final ResourceLocation REGISTRY_LOCATION = new ResourceLocation(LibMod.MOD_ID, "distillery");
	public static final IForgeRegistry<DistilleryRecipe> REGISTRY = new RegistryBuilder<DistilleryRecipe>().disableSaving().setName(REGISTRY_LOCATION).setType(DistilleryRecipe.class).setIDRange(0, 1000).create();

	public static void init() {

		// All of these are temporary recipes! TODO

		REGISTRY.register(DistilleryRecipe.Factory.start("everchanging_presence").withBaseProcessingTime(300).withInput(LibIngredients.soulSand, LibIngredients.paper, LibIngredients.yewEssence, LibIngredients.wormwood).withOutput(new ItemStack(ModItems.everchanging_presence)).build());
		REGISTRY.register(DistilleryRecipe.Factory.start("undying_image").withBaseProcessingTime(300).withInput(LibIngredients.wax, LibIngredients.fumeReekOfDeath, LibIngredients.ghastTear, LibIngredients.obsidian).withOutput(new ItemStack(ModItems.undying_image)).build());
		REGISTRY.register(DistilleryRecipe.Factory.start("emanation_of_dishonesty").withBaseProcessingTime(300).withInput(LibIngredients.graveyardDust, LibIngredients.blazeRod, LibIngredients.oakAppleGall, LibIngredients.fumeBottledMagic).withOutput(new ItemStack(ModItems.emanation_of_dishonesty)).build());
		REGISTRY.register(DistilleryRecipe.Factory.start("otherworldy_tears").withBaseProcessingTime(300).withInput(LibIngredients.enderPearl, LibIngredients.lapisPowder, LibIngredients.fumeBirchSoul, LibIngredients.sagebrush).withOutput(new ItemStack(ModItems.otherworld_tears), new ItemStack(ModItems.dimensional_sand, 2, 0)).build());
		REGISTRY.register(DistilleryRecipe.Factory.start("demonic_elixir").withBaseProcessingTime(300) // TODO: Add demon heart item
				.withInput(LibIngredients.graveyardDust, LibIngredients.blazeRod, LibIngredients.eyes, LibIngredients.fumeCloudyOil).withOutput(new ItemStack(ModItems.demonic_dew)).build());
		REGISTRY.register(DistilleryRecipe.Factory.start("cleansing_aura").withBaseProcessingTime(300).withInput(LibIngredients.whiteSage, LibIngredients.sagebrush, LibIngredients.tulsi, LibIngredients.acaciaResin).withOutput(new ItemStack(ModItems.cleansing_aura), new ItemStack(ModItems.wood_ash)).build());
	}
}
