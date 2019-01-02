package com.covens.common.content.cauldron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public abstract class CauldronCraftingRecipe {

	private List<List<ItemStack>> jeiCache;
	private Ingredient[] ingredients;
	private Fluid fluid;
	private int fluidAmount;

	// The ingredients must be ordered from the more strict to the least strict
	// eg: if a crafting requires both a dyeBlack and an ink sack (not ore dict)
	// the ink sack index should be lower than the dye index
	public CauldronCraftingRecipe(Fluid fluid, int fluidAmount, Ingredient... ingredient) {
		this.ingredients = ingredient;
		this.fluid = fluid;
		this.fluidAmount = fluidAmount;
		this.checkInputHiding();
	}

	// I heard you like for loops and O(+inf) algorithms... (jk, it shouldn't be
	// that bad, hopefully)
	private void checkInputHiding() {
		for (int i = 0; i < (this.ingredients.length - 1); i++) {
			for (ItemStack is : this.ingredients[i].getMatchingStacks()) {
				for (int j = i + 1; j < this.ingredients.length; j++) {
					if (this.ingredients[j].apply(is)) {
						if (this.ingredients[i].getMatchingStacks().length > this.ingredients[j].getMatchingStacks().length) {
							throw new IllegalArgumentException("Ingredient " + this.ingredients[i] + " hides ingredient " + this.ingredients[j] + " if the Stack used is " + is);
						}
					}
				}
			}
		}
	}

	public boolean matches(List<ItemStack> stacks, FluidStack fluidstack) {
		if ((this.fluid != fluidstack.getFluid()) || (stacks.size() != this.ingredients.length)) {
			return false;
		}
		ArrayList<Ingredient> newIngredientList = Lists.newArrayList(this.ingredients);
		ArrayList<ItemStack> stackList = Lists.newArrayList(stacks);
		for (int i = this.ingredients.length - 1; i >= 0; i--) {
			boolean found = false;
			Ingredient ing = newIngredientList.get(i);
			for (int j = stackList.size() - 1; j >= 0; j--) {
				ItemStack is = stackList.get(j);
				if (ing.apply(is)) {
					newIngredientList.remove(i);
					stackList.remove(is);
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	public int getRequiredFluidAmount() {
		return this.fluidAmount;
	}

	public List<List<ItemStack>> getJEIInput() {
		if (this.jeiCache == null) {
			this.jeiCache = Lists.newArrayList();
			HashMap<Ingredient, Integer> sizes = new HashMap<>();
			for (Ingredient i : this.ingredients) {
				if (sizes.containsKey(i)) {
					sizes.put(i, sizes.get(i) + 1);
				} else {
					sizes.put(i, 1);
				}
			}
			for (Ingredient i : sizes.keySet()) {
				List<ItemStack> l = Lists.newArrayList();
				for (ItemStack is : i.getMatchingStacks()) {
					l.add(is.copy());
				}
				l.forEach(is -> is.setCount(sizes.get(i)));
				this.jeiCache.add(l);
			}
		}
		return this.jeiCache;
	}

	public List<List<FluidStack>> getJEIFluidInput() {
		List<List<FluidStack>> result = Lists.newArrayList();
		result.add(Lists.newArrayList(new FluidStack(this.fluid, this.fluidAmount)));
		return result;
	}

	public abstract boolean hasItemOutput();

	public abstract boolean hasFluidOutput();

	public abstract ItemStack getItemResult();

	public abstract FluidStack getFluidResult();

}
