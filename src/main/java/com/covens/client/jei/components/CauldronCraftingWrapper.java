package com.covens.client.jei.components;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.covens.common.crafting.CauldronRecipe;
import com.covens.common.crafting.CauldronRecipe.Wrapper;
import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraftforge.fluids.FluidStack;

public class CauldronCraftingWrapper implements IRecipeWrapper {

	private CauldronRecipe.Wrapper recipe;

	public CauldronCraftingWrapper(CauldronRecipe.Wrapper in) {
		this.recipe = in;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, this.recipe.getJEIItemStacksInput());
		ArrayList<List<FluidStack>> list = Lists.newArrayList();
		list.add(this.recipe.getJEIFluidCache());
		ingredients.setInputLists(VanillaTypes.FLUID, list);
		ingredients.setOutputLists(VanillaTypes.ITEM, this.recipe.getOutputList());
		List<FluidStack> outs_t = this.recipe.getJEIFluidCache().stream()
				.map(fs -> transform(fs, recipe))
				.collect(Collectors.toList());
		List<List<FluidStack>> outs = new ArrayList<>();
		outs.add(outs_t);
		ingredients.setOutputLists(VanillaTypes.FLUID, outs);

	}

	private FluidStack transform(FluidStack fs, Wrapper recipeIn) {
		return recipeIn.processFluid(recipeIn.getJEIItemStacksInput().get(0), fs);
	}

	public CauldronRecipe.Wrapper getOriginal() {
		return this.recipe;
	}

}
