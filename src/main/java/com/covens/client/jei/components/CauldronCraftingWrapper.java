package com.covens.client.jei.components;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.covens.api.cauldron.ICauldronCraftingRecipe;
import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class CauldronCraftingWrapper implements IRecipeWrapper {

	private ICauldronCraftingRecipe recipe;
	private List<List<ItemStack>> inputs;
	private List<List<FluidStack>> fluids;
	private List<List<FluidStack>> outFluids = null; 

	public CauldronCraftingWrapper(ICauldronCraftingRecipe in) {
		this.recipe = in;
		inputs = recipe.getInputList().stream()
				.map(ni -> ni.getCachedStacks())
				.collect(Collectors.toList());
		fluids = Collections.singletonList(Lists.newArrayList(new FluidStack(this.recipe.getInputFluid(), 1000)));
		if (this.recipe.getOutputFluid() != null && this.recipe.getMinimumFluidRequired() < Fluid.BUCKET_VOLUME) {
			outFluids = Collections.singletonList(Collections.singletonList(new FluidStack(this.recipe.getOutputFluid(), 1000 - recipe.getMinimumFluidRequired())));
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
