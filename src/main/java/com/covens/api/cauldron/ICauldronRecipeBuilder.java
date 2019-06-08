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
	
	public ICauldronRecipeBuilder addFluidInput(Fluid fluid);
	
	public ICauldronRecipeBuilder drainFlatAmount(Fluid output, int amountDrained);
	
	public ICauldronRecipeBuilder setExactFluidAmount(int exactAmount);
	
	public ICauldronRecipeBuilder setMaxFluidAllowed(int maxAmount);
	
	public ICauldronRecipeBuilder setMinFluidRequired(int minAmount);
	
	public ICauldronRecipeBuilder setOutput(ItemStack out);
	
	public ICauldronRecipeBuilder setRequiredPower(int amount);
	
	public ICauldronRecipeBuilder setOutputFluidConvertingExisitingAmount(Fluid fluid);
	
	public ICauldronRecipeBuilder setConsumeAllFluid();
	
	public ICauldronRecipeBuilder setOutputFluidFixedAmount(Fluid fluid, int amount);
	
	public ICauldronRecipeBuilder setCustomOutputFluid(TriProcessor<FluidStack> processor);
	
	public ICauldronRecipeBuilder setCustomOutputProcessor(TriProcessor<ItemStack> processor);
	
	public ICauldronRecipe build();
	
	public ICauldronRecipe buildAndRegister();

}
