package com.covens.client.jei.components;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.covens.api.cauldron.ICauldronRecipe;
import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CauldronCraftingWrapper implements IRecipeWrapper {

	private ICauldronRecipe recipe;
	private List<List<ItemStack>> inputs;
	private List<List<FluidStack>> fluids;
	private List<List<FluidStack>> outFluids = null; 

	public CauldronCraftingWrapper(ICauldronRecipe in) {
		this.recipe = in;
		inputs = recipe.getInputList().stream()
				.map(ni -> ni.getCachedStacks())
				.collect(Collectors.toList());
		fluids = recipe.getInputFluid().stream()
				.map(f -> Lists.newArrayList(new FluidStack(f, recipe.getMinimumFluid())))
				.collect(Collectors.toList());
		if (this.recipe.getOutputFluid() != null) {
			outFluids = Collections.singletonList(Lists.newArrayList(new FluidStack(this.recipe.getOutputFluid(), recipe.getMinimumFluid()), new FluidStack(this.recipe.getOutputFluid(), recipe.getMaximumFluid())));
		}
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setInputLists(VanillaTypes.FLUID, fluids);
		if (hasItems()) {
			ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getOutput());
		}
		if (hasFluid()) {
			ingredients.setOutputLists(VanillaTypes.FLUID, outFluids);
		}
	}

	public boolean hasFluid() {
		return outFluids!=null;
	}
	
	public boolean hasItems() {
		return !this.recipe.getOutput().isEmpty();
	}

}
