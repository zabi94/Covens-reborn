package com.covens.common.crafting;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import com.covens.common.item.ModItems;
import com.covens.common.lib.LibIngredients;
import com.covens.common.lib.LibMod;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class FrostFireRecipe extends IForgeRegistryEntry.Impl<FrostFireRecipe> {

	public static final IForgeRegistry<FrostFireRecipe> REGISTRY = new RegistryBuilder<FrostFireRecipe>().disableSaving().setName(new ResourceLocation(LibMod.MOD_ID, "frostfire")).setType(FrostFireRecipe.class).setIDRange(0, 200).create();

	private static final Random rng = new Random();
	private static final ItemStack packedIce = new ItemStack(Blocks.PACKED_ICE, 1);
	private Ingredient input;
	private Supplier<ItemStack> output;

	public FrostFireRecipe(ResourceLocation name, Ingredient inputIn, Supplier<ItemStack> outputIn) {
		this.setRegistryName(name);
		this.input = inputIn;
		this.output = outputIn;
	}

	public static void init() {
		REGISTRY.register(new FrostFireRecipe(new ResourceLocation(LibMod.MOD_ID, "cold_iron"), LibIngredients.ironOre, () -> new ItemStack(ModItems.cold_iron_nugget, 1 + rng.nextInt(4))));
		REGISTRY.register(new FrostFireRecipe(new ResourceLocation(LibMod.MOD_ID, "packed_ice"), LibIngredients.iceBlock, () -> packedIce));
		REGISTRY.register(new FrostFireRecipe(new ResourceLocation(LibMod.MOD_ID, "cold_iron_dust_small"), LibIngredients.coldIronDustSmall, () -> new ItemStack(ModItems.cold_iron_nugget, 1)));
		REGISTRY.register(new FrostFireRecipe(new ResourceLocation(LibMod.MOD_ID, "cold_iron_dust"), LibIngredients.coldIronDust, () -> new ItemStack(ModItems.cold_iron_ingot, 1)));
	}

	public static Optional<ItemStack> getOutput(ItemStack in) {
		return REGISTRY.getValuesCollection().stream().filter(r -> r.input.apply(in)).map(ff -> ff.getOutput().copy()).findAny();
	}

	public ItemStack getOutput() {
		return this.output.get();
	}
}
