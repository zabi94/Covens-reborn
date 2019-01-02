package com.covens.client.jei.components;

import java.util.ArrayList;
import java.util.Arrays;

import com.covens.api.cauldron.IBrewEffect;
import com.covens.common.content.cauldron.BrewData;
import com.covens.common.content.cauldron.BrewData.BrewEntry;
import com.covens.common.content.cauldron.BrewModifierListImpl;
import com.covens.common.content.cauldron.CauldronRegistry;
import com.covens.common.item.ModItems;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class BrewingWrapper implements IRecipeWrapper {

	IBrewEffect effect;

	public BrewingWrapper(IBrewEffect effect) {
		this.effect = effect;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(Arrays.asList(CauldronRegistry.getIngredientFromBrew(this.effect).orElse(Ingredient.EMPTY).getMatchingStacks())));

		BrewData data = new BrewData();
		data.addEntry(new BrewEntry(CauldronRegistry.getPotionFromBrew(this.effect), new BrewModifierListImpl()));

		ingredients.setOutputLists(VanillaTypes.ITEM, Arrays.asList(this.getListFor(data)));
	}

	private ArrayList<ItemStack> getListFor(BrewData data) {
		ArrayList<ItemStack> items = new ArrayList<>(4);
		ItemStack a = new ItemStack(ModItems.brew_phial_drink);
		data.saveToStack(a);
		items.add(a);
		a = new ItemStack(ModItems.brew_phial_splash);
		data.saveToStack(a);
		items.add(a);
		a = new ItemStack(ModItems.brew_phial_linger);
		data.saveToStack(a);
		items.add(a);
		a = new ItemStack(ModItems.brew_arrow);
		data.saveToStack(a);
		items.add(a);
		return items;
	}

}
