package com.covens.client.jei.components;

import com.covens.common.content.cauldron.CauldronCraftingRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class CauldronCraftingWrapper implements IRecipeWrapper {

	private CauldronCraftingRecipe recipe;

	public CauldronCraftingWrapper(CauldronCraftingRecipe in) {
		this.recipe = in;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, this.recipe.getJEIInput());
		ingredients.setInputLists(VanillaTypes.FLUID, this.recipe.getJEIFluidInput());
		if (this.recipe.hasItemOutput()) {
			ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getItemResult());
		} else {
			ingredients.setOutput(VanillaTypes.ITEM, ItemStack.EMPTY);
		}
		if (this.recipe.hasFluidOutput()) {
			ingredients.setOutput(VanillaTypes.FLUID, this.recipe.getFluidResult());
		}

	}

	public CauldronCraftingRecipe getOriginal() {
		return this.recipe;
	}

}
