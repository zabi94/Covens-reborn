package com.covens.api.cauldron;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import zabi.minecraft.minerva.common.crafting.NumberedInput;

/**
 * It's highly suggested to use the builder instead of implementing this directly.<br>
 * You can get an instance from com.covens.api.CovensAPI::getNewCauldronRecipeBuilder
 * @author zabi94
 */
public interface ICauldronCraftingRecipe extends ICauldronRecipe {
	
	/**
	 * @return a unique ResourceLocation for this cauldron recipe
	 */
	public ResourceLocation getRegistryName();
	
	/**
	 * @return the fluid that will trigger this recipe
	 */
	public Fluid getInputFluid();
	
	/**
	 * By default the output fluid will have the same amount of the fluid inside the cauldron minus the amount required.
	 * 
	 * @return the output fluid to replace the one in the cauldron
	 */
	public Fluid getOutputFluid();
	
	
	/**
	 * @return the minimum amount of fluid in the cauldron required to trigger a recipe
	 */
	public int getMinimumFluidRequired();
	
	/**
	 * @return a list of inputs to match exactly for this recipe to work. Order is irrelevant.
	 */
	public List<NumberedInput> getInputList();
	
	/**
	 * This method should be used to decide what to show in JEI, so you can have a 
	 * pre-formatted output here, and modify the actual output through {@link ICauldronCraftingRecipe#processOutput(List, FluidStack)}
	 * 
	 * @return a simple item to be spit out by the cauldron
	 */
	public ItemStack getOutput();
	
	/**
	 * This is the chance to apply NBT to the fluid
	 * 
	 * @param input The itemstacks used to trigger this recipe
	 * @param fluid The fluid present in the cauldron <i>before</i> this recipe is processed
	 * @return the fluidstack that should be left in the cauldron after this recipe has been processed
	 */
	public FluidStack processFluid(List<ItemStack> input, FluidStack fluid);
	
	/**
	 * This is the chance to apply NBT or other item transformations to the output
	 * 
	 * @param input The itemstacks used to trigger this recipe
	 * @param fluid The fluid present in the cauldron <i>before</i> this recipe is processed
	 * @return the ItemStack to be produced by this recipe 
	 */
	default ItemStack processOutput(List<ItemStack> input, FluidStack fluid) {
		return getOutput();
	}
	
}
