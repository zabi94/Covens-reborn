package com.covens.client.jei.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.covens.common.crafting.OvenSmeltingRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class OvenWrapper implements IRecipeWrapper {

	private Ingredient input;
	private ItemStack[] output;

	public OvenWrapper(OvenSmeltingRecipe recipe) {
		this.input = recipe.getInput();
		this.output = new ItemStack[2];
		this.output[0] = recipe.getOutput();
		this.output[1] = recipe.getFumes();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ArrayList<List<ItemStack>> inputs = new ArrayList<>();
		inputs.add(Arrays.asList(this.input.getMatchingStacks()));
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ArrayList<List<ItemStack>> outputs = new ArrayList<>();
		outputs.add(Arrays.asList(this.output));
		ingredients.setOutputLists(VanillaTypes.ITEM, outputs);
	}

	public Ingredient getInput() {
		return this.input;
	}

	public ItemStack[] getOutput() {
		return this.output;
	}
}
