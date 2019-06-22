package com.covens.common.crafting;

import java.util.List;

import com.covens.api.CovensAPI;
import com.covens.api.TriProcessor;
import com.covens.api.cauldron.ICauldronCraftingRecipe;
import com.covens.api.cauldron.ICauldronRecipeBuilder;
import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import zabi.minecraft.minerva.common.crafting.NumberedInput;
import zabi.minecraft.minerva.common.utils.ValidationHelper;

public class CauldronRecipeBuilder implements ICauldronRecipeBuilder {
	
	private ResourceLocation name;
	private int mpRequired = 0;
	
	private Fluid fluidInput = FluidRegistry.WATER;
	private List<NumberedInput> inputs = Lists.newArrayList();
	
	private int requiredFluidAmount = Fluid.BUCKET_VOLUME;
	
	private ItemStack output = ItemStack.EMPTY;
	private Fluid fluidOutput = FluidRegistry.WATER;
	
	private TriProcessor<FluidStack> fluidProcessor = (iss, fs, out) -> out; 
	private TriProcessor<ItemStack> itemProcessor = (iss, fs, out) -> out; 
	
	public CauldronRecipeBuilder(ResourceLocation name) {
		
		if (name == null) {
			throw new IllegalArgumentException("Registry name must not be null");
		}
		this.name = name;
	}

	@Override
	public ICauldronRecipeBuilder addInput(NumberedInput in) {
		inputs.add(in);
		return this;
	}

	@Override
	public ICauldronRecipeBuilder addInput(Ingredient in, int amount) {
		return addInput(new NumberedInput(in, amount, true));
	}


	@Override
	public ICauldronRecipeBuilder setRequiredPower(int amount) {
		mpRequired = amount;
		return this;
	}

	
	@Override
	public ICauldronCraftingRecipe build() {
		checkValidity();
		CauldronRecipe result = new CauldronRecipe();
		result.name = this.name;
		result.mpRequired = this.mpRequired;
		result.fluidOutput = this.fluidOutput;
		result.fluidInput = this.fluidInput;
		result.inputs = this.inputs;
		result.output = this.output;
		result.fluidProcessor = this.fluidProcessor; 
		result.itemProcessor = this.itemProcessor; 
		result.fluidAmount = this.requiredFluidAmount;
		return result;
	}

	private void checkValidity() {
		int nullElement = ValidationHelper.firstNullElement(fluidProcessor, itemProcessor, output);
		if (nullElement >= 0) {
			throw new IllegalStateException(name+" -- Elements must all be non null, null index: "+nullElement);
		}
		if (mpRequired < 0) {
			throw new IllegalStateException("Must specify a positive cost for the recipe");
		}
		if (inputs.size() == 0) {
			throw new IllegalStateException("At least one item input is required");
		}
	}

	@Override
	public ICauldronCraftingRecipe buildAndRegister() {
		ICauldronCraftingRecipe result = build();
		CovensAPI.getAPI().registerCauldronRecipe(result);
		return result;
	}

	@Override
	public ICauldronRecipeBuilder setFluidInput(Fluid fluid) {
		fluidInput = fluid;
		return this;
	}

	@Override
	public ICauldronRecipeBuilder setOutput(ItemStack out) {
		this.output = out;
		return this;
	}

	@Override
	public ICauldronRecipeBuilder setCustomOutputFluid(TriProcessor<FluidStack> processor) {
		fluidProcessor = processor;
		return this;
	}

	@Override
	public ICauldronRecipeBuilder setCustomOutputProcessor(TriProcessor<ItemStack> processor) {
		itemProcessor = processor;
		return this;
	}

	@Override
	public ICauldronRecipeBuilder setOutputFluid(Fluid fluid) {
		this.fluidOutput = fluid;
		return this;
	}

	@Override
	public ICauldronRecipeBuilder setFluidConsumed(int amount) {
		this.requiredFluidAmount = amount;
		return this;
	}
	
}