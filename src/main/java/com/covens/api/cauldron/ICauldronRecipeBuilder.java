package com.covens.api.cauldron;

import com.covens.api.TriProcessor;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import zabi.minecraft.minerva.common.crafting.NumberedInput;

public interface ICauldronRecipeBuilder {
	
	public ICauldronRecipeBuilder addInput(NumberedInput in);
	
	public ICauldronRecipeBuilder addInput(Ingredient in, int amount);
	
	public ICauldronRecipeBuilder setFluidInput(Fluid fluid);
	
	public ICauldronRecipeBuilder setOutput(ItemStack out);
	
	public ICauldronRecipeBuilder setRequiredPower(int amount);
	
	public ICauldronRecipeBuilder setOutputFluid(Fluid fluid);
	
	public ICauldronRecipeBuilder setFluidConsumed(int amount);
	
	public ICauldronRecipeBuilder setCustomOutputFluid(TriProcessor<FluidStack> processor);
	
	public ICauldronRecipeBuilder setCustomOutputProcessor(TriProcessor<ItemStack> processor);
	
	public ICauldronCraftingRecipe build();
	
	public ICauldronCraftingRecipe buildAndRegister();

}
