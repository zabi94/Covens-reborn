package com.covens.client.jei.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.covens.common.crafting.DistilleryRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class DistilleryWrapper implements IRecipeWrapper {

	List<Ingredient> input;
	List<ItemStack> output;

	public DistilleryWrapper(DistilleryRecipe recipe) {
		this.input = recipe.getInputs();
		this.output = recipe.getOutputs();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ArrayList<List<ItemStack>> list = new ArrayList<List<ItemStack>>();
		for (Ingredient i : this.input) {
			list.add(Arrays.asList(i.getMatchingStacks()));
		}
		ingredients.setInputLists(VanillaTypes.ITEM, list);
		ingredients.setOutputs(VanillaTypes.ITEM, this.output);
	}
}
