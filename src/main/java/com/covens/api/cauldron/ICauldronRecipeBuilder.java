package com.covens.api.cauldron;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import zabi.minecraft.minerva.common.crafting.NumberedInput;

public interface ICauldronRecipeBuilder {
	
	public ICauldronRecipeBuilder addInput(NumberedInput in);
	
	public ICauldronRecipeBuilder addInput(Ingredient in, int amount);
	
	public ICauldronRecipeBuilder addOutput(ItemStack out);
	
	public ICauldronRecipeBuilder addOutput(List<ItemStack> out);
	
	public ICauldronRecipeBuilder setCustomFluidChecker(Predicate<FluidStack> checker);
	
	public ICauldronRecipeBuilder setValidFluid(Fluid fluid, int amount);
	
	public ICauldronRecipeBuilder setValidFluid(Predicate<Fluid> fluid, int amount);
	
	public ICauldronRecipeBuilder setValidFluid(Fluid fluid);
	
	public ICauldronRecipeBuilder setValidFluid(Predicate<Fluid> fluid, List<FluidStack> JEICache);
	
	public ICauldronRecipeBuilder setRequiredPower(int amount);
	
	public ICauldronRecipeBuilder setOutputFluidConvertingExisitingAmount(Fluid fluid);
	
	public ICauldronRecipeBuilder setConsumeAllFluid();
	
	public ICauldronRecipeBuilder setOutputFluidFixedAmount(Fluid fluid, int amount);
	
	public ICauldronRecipeBuilder setCustomOutputProcessor(BiFunction<List<ItemStack>, FluidStack, List<ItemStack>> processor);
	
	public ICauldronRecipeBuilder setCustomFluidProcessor(BiFunction<List<ItemStack>, FluidStack, FluidStack> processor, List<FluidStack> JEICache);
	
	public ICauldronRecipe build();
	
	public ICauldronRecipe buildAndRegister();

}
