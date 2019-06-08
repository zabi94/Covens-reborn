package com.covens.common.crafting;

import java.util.List;

import com.covens.api.CovensAPI;
import com.covens.api.TriProcessor;
import com.covens.api.cauldron.ICauldronRecipe;
import com.covens.api.cauldron.ICauldronRecipeBuilder;
import com.covens.common.core.helper.Log;
import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import zabi.minecraft.minerva.common.crafting.ExactRecipeHelper;
import zabi.minecraft.minerva.common.crafting.NumberedInput;
import zabi.minecraft.minerva.common.utils.ValidationHelper;

public class CauldronRecipe implements ICauldronRecipe {
	
	protected ResourceLocation name;
	protected int mpRequired = 0;
	protected int fluidMin = 0;
	protected int fluidMax = 0;
	protected Fluid fluidOutput;
	protected List<Fluid> fluidInput = Lists.newArrayList();
	protected List<NumberedInput> inputs = Lists.newArrayList();
	protected ItemStack output = ItemStack.EMPTY;
	protected TriProcessor<FluidStack> fluidProcessor = (iss, fs, out) -> out; 
	protected TriProcessor<ItemStack> itemProcessor = (iss, fs, out) -> out; 
	
	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}

	@Override
	public List<Fluid> getInputFluid() {
		return fluidInput;
	}

	@Override
	public int getMinimumFluid() {
		return fluidMax;
	}

	@Override
	public Fluid getOutputFluid() {
		return fluidOutput;
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
		Fluid f = getOutputFluid();
		if (f == null) {
			return new FluidStack(FluidRegistry.WATER, 0);
		}
		return fluidProcessor.process(input, fluid, new FluidStack(f, fluid.amount));
	}
	
	@Override
	public int getMaximumFluid() {
		return fluidMax;
	}
	
	@Override
	public ItemStack processOutput(List<ItemStack> input, FluidStack fluid) {
		return itemProcessor.process(input, fluid, getOutput());
	}

	@Override
	public int getMPRequired(List<ItemStack> input, FluidStack fluid) {
		return mpRequired;
	}
	
	public static boolean matches(ICauldronRecipe recipe, List<ItemStack> stacks, FluidStack stack) {
		boolean ingredients = ExactRecipeHelper.matches(stacks, recipe.getInputList());
		boolean fluid = recipe.getInputFluid().contains(stack.getFluid());
		boolean fluidAmount = recipe.getMinimumFluid() <= stack.amount && recipe.getMaximumFluid() >= stack.amount;
		System.out.format("%s: i: %s, f: %s, fa: %s\n", recipe.getRegistryName().toString(), ingredients, fluid, fluidAmount);
		return  ingredients && fluid && fluidAmount; 
	}
	
	public static class CauldronRecipeBuilder implements ICauldronRecipeBuilder {
		
		private ResourceLocation name;
		private int mpRequired = -1;
		private int fluidMin = -1;
		private int fluidMax = -1;
		private Fluid fluidOutput = null;
		private List<Fluid> fluidInput = Lists.newArrayList();
		private List<NumberedInput> inputs = Lists.newArrayList();
		private ItemStack output = ItemStack.EMPTY;
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
		public ICauldronRecipeBuilder setOutputFluidConvertingExisitingAmount(Fluid fluid) {
			fluidOutput = fluid;
			fluidMax = Fluid.BUCKET_VOLUME;
			fluidMin = 0;
			fluidProcessor = (iss, fs, out) -> out; 
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setConsumeAllFluid() {
			fluidOutput = FluidRegistry.WATER;
			fluidProcessor = (iss, fs, out) -> new FluidStack(out.getFluid(), 0); 
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setOutputFluidFixedAmount(Fluid fluid, int amount) {
			fluidOutput = fluid;
			fluidProcessor = (iss, fs, out) -> new FluidStack(out.getFluid(), amount); 
			return this;
		}

		@Override
		public ICauldronRecipe build() {
			checkValidity();
			CauldronRecipe result = new CauldronRecipe();
			result.name = this.name;
			result.mpRequired = this.mpRequired;
			result.fluidMin = this.fluidMin;
			result.fluidMax = this.fluidMax;
			result.fluidOutput = this.fluidOutput;
			result.fluidInput = this.fluidInput;
			result.inputs = this.inputs;
			result.output = this.output;
			result.fluidProcessor = this.fluidProcessor; 
			result.itemProcessor = this.itemProcessor; 
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
			if (fluidMin < 0) {
				Log.w("Setting minimum fluid amount to 0 in recipe "+this.name+". Please add explicit amount with ICauldronRecipeBuilder::setMinFluidRequired");
				fluidMin = 0;
			}
			if (fluidMax < 0) {
				Log.w("Setting maximum fluid amount to 1000 in recipe "+this.name+". Please add explicit amount with ICauldronRecipeBuilder::setMaxFluidRequired");
				fluidMax = Fluid.BUCKET_VOLUME;
			}
			if (inputs.size() == 0) {
				throw new IllegalStateException("At least one item input is required");
			}
		}

		@Override
		public ICauldronRecipe buildAndRegister() {
			ICauldronRecipe result = build();
			CovensAPI.getAPI().registerCauldronRecipe(result);
			return result;
		}

		@Override
		public ICauldronRecipeBuilder addFluidInput(Fluid fluid) {
			fluidInput.add(fluid);
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setExactFluidAmount(int exactAmount) {
			fluidMax = exactAmount;
			fluidMin = exactAmount;
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setMaxFluidAllowed(int maxAmount) {
			fluidMax = maxAmount;
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setMinFluidRequired(int minAmount) {
			fluidMin = minAmount;
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
		public ICauldronRecipeBuilder drainFlatAmount(Fluid output, int amount) {
			this.fluidMin = amount;
			this.fluidMax = Fluid.BUCKET_VOLUME;
			this.fluidOutput = output;
			setCustomOutputFluid((li, fl, cf) -> new FluidStack(cf.getFluid(), cf.amount - amount));
			return this;
		}
		
	}

}
