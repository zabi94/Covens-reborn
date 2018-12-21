package com.covens.client.jei.components;

import com.covens.common.crafting.SpinningThreadRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpinnerWrapper implements IRecipeWrapper {

	private Ingredient[] input;
	private ItemStack output;

	public SpinnerWrapper(SpinningThreadRecipe recipe) {
		setInput(recipe.getInputs());
		output = recipe.getOutput();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ArrayList<List<ItemStack>> list = new ArrayList<List<ItemStack>>();
		for (Ingredient i : getInput()) list.add(Arrays.asList(i.getMatchingStacks()));
		ingredients.setInputLists(VanillaTypes.ITEM, list);
		ingredients.setOutput(VanillaTypes.ITEM, output);
	}

	public ItemStack getOutput() {
		return output;
	}
	
	public Ingredient[] getInput() {
		return input;
	}

	public void setInput(Ingredient[] input) {
		this.input = input;
	}
}
