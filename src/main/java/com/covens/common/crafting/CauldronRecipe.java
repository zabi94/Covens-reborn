package com.covens.common.crafting;

import java.util.List;

import com.covens.api.TriProcessor;
import com.covens.api.cauldron.ICauldronCraftingRecipe;
import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import zabi.minecraft.minerva.common.crafting.ExactRecipeHelper;
import zabi.minecraft.minerva.common.crafting.NumberedInput;

public class CauldronRecipe implements ICauldronCraftingRecipe {
	
	protected ResourceLocation name;
	protected int mpRequired = 0;
	
	protected Fluid fluidOutput;
	protected ItemStack output = ItemStack.EMPTY;
	
	protected Fluid fluidInput = FluidRegistry.WATER;
	protected List<NumberedInput> inputs = Lists.newArrayList();
	
	protected int fluidAmount = 0;
	
	protected TriProcessor<FluidStack> fluidProcessor = (iss, fs, out) -> out; 
	protected TriProcessor<ItemStack> itemProcessor = (iss, fs, out) -> out; 
	
	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}

	@Override
	public Fluid getInputFluid() {
		return fluidInput;
	}

	@Override
	public List<NumberedInput> getInputList() {
		return inputs;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public FluidStack processFluid(List<ItemStack> input, FluidStack fluid) {
		return fluidProcessor.process(input, fluid, new FluidStack(getOutputFluid(), fluid.amount - getMinimumFluidRequired()));
	}
	
	@Override
	public ItemStack processOutput(List<ItemStack> input, FluidStack fluid) {
		return itemProcessor.process(input, fluid, getOutput());
	}

	@Override
	public int getCostPerTick() {
		return mpRequired;
	}
	
	@Override
	public int getMinimumFluidRequired() {
		return fluidAmount;
	}

	@Override
	public Fluid getOutputFluid() {
		return fluidOutput;
	}
	
	public static boolean matches(ICauldronCraftingRecipe recipe, List<ItemStack> stacks, FluidStack stack) {
		boolean ingredients = ExactRecipeHelper.matches(stacks, recipe.getInputList());
		boolean fluid = recipe.getInputFluid().equals(stack.getFluid());
		boolean amount = stack.amount >= recipe.getMinimumFluidRequired();
		return  ingredients && fluid && amount; 
	}

}
