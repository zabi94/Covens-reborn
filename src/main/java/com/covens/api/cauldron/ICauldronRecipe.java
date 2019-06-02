package com.covens.api.cauldron;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import zabi.minecraft.minerva.common.crafting.NumberedInput;

/**
 * It's highly suggested to use the builder instead of implementing this directly.<br>
 * You can get an instance from com.covens.api.CovensAPI::getNewCauldronRecipeBuilder
 * @author zabi94
 */
public interface ICauldronRecipe {
	
	/**
	 * @return a unique ResourceLocation for this cauldron recipe
	 */
	public ResourceLocation getRegistryName();
	
	/**
	 * @param in The fluid currently present inside the cauldron, before any processing
	 * @return true if the fluid matches this recipe, false otherwise. This should match both the fluid type <b>and</b> the fluid amount
	 */
	public boolean isValidFluid(FluidStack in);
	
	/**
	 * @return a list of inputs to match exactly for this recipe to work. Order is irrelevant.
	 */
	public List<NumberedInput> getInputList();
	
	/**
	 * Mainly used to provide JEI and book representation
	 * 
	 * @return a list with an element for each stack the output contains. The element should be another list with
	 * all the stacks accepted for that element
	 */
	public List<List<ItemStack>> getOutputList();
	
	/**
	 * @param input The itemstacks used to trigger this recipe
	 * @param fluid The fluid present in the cauldron <i>before</i> this recipe is processed
	 * @return the fluidstack that should be left in the cauldron after this recipe has been processed
	 */
	public FluidStack processFluid(List<ItemStack> input, FluidStack fluid);
	
	/**
	 * @param input The itemstacks used to trigger this recipe
	 * @param fluid The fluid present in the cauldron <i>before</i> this recipe is processed
	 * @return a list of ItemStacks to be spit out from the cauldron after this recipe has been processed
	 */
	public List<ItemStack> getOutputs(List<ItemStack> input, FluidStack fluid);
	
	/**
	 * @param input The itemstacks used to trigger this recipe
	 * @param fluid The fluid present in the cauldron <i>before</i> this recipe is processed
	 * @return the amount of power to be drained by this recipe.
	 */
	public int getMPRequired(List<ItemStack> input, FluidStack fluid);
	
	/**
	 * @return a list of fluids that should be shown in JEI and books
	 */
	public List<FluidStack> getJEIFluidCache();
}
