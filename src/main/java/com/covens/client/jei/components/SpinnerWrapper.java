package com.covens.client.jei.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.covens.common.crafting.SpinningThreadRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class SpinnerWrapper implements IRecipeWrapper {

	private Ingredient[] input;
	private ItemStack output;

	public SpinnerWrapper(SpinningThreadRecipe recipe) {
		this.setInput(recipe.getInputs());
		this.output = recipe.getOutput();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ArrayList<List<ItemStack>> list = new ArrayList<List<ItemStack>>();
		for (Ingredient i : this.getInput()) {
			list.add(Arrays.asList(i.getMatchingStacks()));
		}
		ingredients.setInputLists(VanillaTypes.ITEM, list);
		ingredients.setOutput(VanillaTypes.ITEM, this.output);
	}

	public ItemStack getOutput() {
		return this.output;
	}

	public Ingredient[] getInput() {
		return this.input;
	}

	public void setInput(Ingredient[] input) {
		this.input = input;
	}
}
