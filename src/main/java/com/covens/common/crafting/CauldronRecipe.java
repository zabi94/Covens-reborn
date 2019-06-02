package com.covens.common.crafting;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.covens.api.CovensAPI;
import com.covens.api.cauldron.ICauldronRecipe;
import com.covens.api.cauldron.ICauldronRecipeBuilder;
import com.google.common.base.Predicates;
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
	protected Predicate<FluidStack> fluidChecker = Predicates.alwaysTrue();
	protected List<NumberedInput> inputs = Lists.newArrayList();
	protected List<List<ItemStack>> outputs = Lists.newArrayList();
	protected BiFunction<List<ItemStack>, FluidStack, List<ItemStack>> outputProcessor = (s, f) -> Lists.newArrayList();
	protected BiFunction<List<ItemStack>, FluidStack, FluidStack> outputFluidProcessor = (s, f) -> new FluidStack(FluidRegistry.WATER, 0);
	protected List<FluidStack> jeiCache = Lists.newArrayList();

	public void setJEIFluidCache(List<FluidStack> JEIFluidCache) {
		jeiCache = JEIFluidCache;
	}
	
	public void setRegistryName(ResourceLocation nameIn) {
		name = nameIn;
	}
	
	public void setRequiredMP(int mpIn) {
		mpRequired = mpIn;
	}
	
	public void setFluidChecker(Predicate<FluidStack> checkerIn) {
		fluidChecker = checkerIn;
	}
	
	public void setInputs(List<NumberedInput> inputs) {
		this.inputs = inputs;
	}
	
	public void setOutputProcessor(BiFunction<List<ItemStack>, FluidStack, List<ItemStack>> outputProcessor) {
		this.outputProcessor = outputProcessor;
	}
	
	public void setOutputFluidProcessor(BiFunction<List<ItemStack>, FluidStack, FluidStack> outputFluidProcessor) {
		this.outputFluidProcessor = outputFluidProcessor;
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}

	@Override
	public boolean isValidFluid(FluidStack in) {
		return fluidChecker.test(in);
	}

	@Override
	public List<NumberedInput> getInputList() {
		return inputs;
	}
	
	@Override
	public List<List<ItemStack>> getOutputList() {
		return outputs;
	}
	
	public void setOutputs(List<List<ItemStack>> outputs) {
		this.outputs = outputs;
	}
	
	@Override
	public FluidStack processFluid(List<ItemStack> input, FluidStack fluid) {
		return outputFluidProcessor.apply(input, fluid);
	}

	@Override
	public List<ItemStack> getOutputs(List<ItemStack> input, FluidStack fluid) {
		return outputProcessor.apply(input, fluid);
	}

	@Override
	public int getMPRequired(List<ItemStack> input, FluidStack fluid) {
		return mpRequired;
	}

	@Override
	public List<FluidStack> getJEIFluidCache() {
		return jeiCache;
	}
	
	public boolean matches(List<ItemStack> stacks) {
		return ExactRecipeHelper.matches(stacks, getInputList());
	}
	
	public static class CauldronRecipeBuilder implements ICauldronRecipeBuilder {
		
		private ResourceLocation name;
		private int mpRequired = -1;
		private Predicate<FluidStack> fluidChecker;
		private List<NumberedInput> inputs = Lists.newArrayList();
		private List<List<ItemStack>> outputs = Lists.newArrayList();
		private BiFunction<List<ItemStack>, FluidStack, List<ItemStack>> outputProcessor;
		private BiFunction<List<ItemStack>, FluidStack, FluidStack> outputFluidProcessor;
		
		private List<FluidStack> JEIFluidCache = Lists.newArrayList();
		
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
		public ICauldronRecipeBuilder addOutput(ItemStack out) {
			outputs.add(Lists.newArrayList(out));
			return this;
		}
		
		@Override
		public ICauldronRecipeBuilder addOutput(List<ItemStack> out) {
			outputs.add(out);
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setCustomFluidChecker(Predicate<FluidStack> checker) {
			fluidChecker = checker;
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setValidFluid(Fluid fluid, int amount) {
			return setValidFluid(f -> f.equals(fluid), amount);
		}

		@Override
		public ICauldronRecipeBuilder setValidFluid(Predicate<Fluid> fluidPredicate, int amount) {
			fluidChecker = fs -> fluidPredicate.test(fs.getFluid()) && fs.amount >= amount;
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setValidFluid(Fluid fluid) {
			return setValidFluid(fluid, Fluid.BUCKET_VOLUME);
		}

		@Override
		public ICauldronRecipeBuilder setValidFluid(Predicate<Fluid> fluidPredicate, List<FluidStack> JEICache) {
			this.JEIFluidCache = JEICache;
			return setValidFluid(fluidPredicate, Fluid.BUCKET_VOLUME);
		}

		@Override
		public ICauldronRecipeBuilder setRequiredPower(int amount) {
			mpRequired = amount;
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setOutputFluidConvertingExisitingAmount(Fluid fluid) {
			outputFluidProcessor = (s, fs) -> new FluidStack(fluid, fs.amount);
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setConsumeAllFluid() {
			outputFluidProcessor = (s, fs) -> new FluidStack(FluidRegistry.WATER, 0);
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setOutputFluidFixedAmount(Fluid fluid, int amount) {
			outputFluidProcessor = (s, fs) -> new FluidStack(fluid, amount);
			this.JEIFluidCache = Lists.newArrayList(new FluidStack(fluid, amount));
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setCustomOutputProcessor(BiFunction<List<ItemStack>, FluidStack, List<ItemStack>> processor) {
			this.outputProcessor = processor;
			return this;
		}

		@Override
		public ICauldronRecipeBuilder setCustomFluidProcessor(BiFunction<List<ItemStack>, FluidStack, FluidStack> processor, List<FluidStack> JEICache) {
			this.JEIFluidCache = JEICache;
			this.outputFluidProcessor = processor;
			return this;
		}

		@Override
		public ICauldronRecipe build() {
			checkValidity();
			CauldronRecipe result = new CauldronRecipe();
			result.setRegistryName(name);
			result.setFluidChecker(fluidChecker);
			result.setInputs(inputs);
			result.setOutputs(outputs);
			result.setOutputFluidProcessor(outputFluidProcessor);
			result.setOutputProcessor(outputProcessor);
			result.setRequiredMP(mpRequired);
			result.setJEIFluidCache(JEIFluidCache);
			return result;
		}

		private void checkValidity() {
			int nullElement = ValidationHelper.firstNullElement(null, fluidChecker, inputs, outputs, outputFluidProcessor, outputProcessor);
			if (nullElement >= 0) {
				throw new IllegalStateException(name+" -- Elements must all be non null, null index: "+nullElement);
			}
			if (mpRequired < 0) {
				throw new IllegalStateException("Must specify a positive cost for the recipe");
			}
		}

		@Override
		public ICauldronRecipe buildAndRegister() {
			ICauldronRecipe result = build();
			CovensAPI.getAPI().registerCauldronRecipe(result);
			return result;
		}
		
	}
	
	public static class Wrapper implements ICauldronRecipe {
		
		private ICauldronRecipe base;
		private List<List<ItemStack>> inputs = null;
		
		public Wrapper(ICauldronRecipe recipe) {
			base = recipe;
		}

		@Override
		public ResourceLocation getRegistryName() {
			return base.getRegistryName();
		}

		@Override
		public boolean isValidFluid(FluidStack in) {
			return base.isValidFluid(in);
		}

		@Override
		public List<NumberedInput> getInputList() {
			return base.getInputList();
		}

		@Override
		public List<List<ItemStack>> getOutputList() {
			return base.getOutputList();
		}

		@Override
		public FluidStack processFluid(List<ItemStack> input, FluidStack fluid) {
			return base.processFluid(input, fluid);
		}

		@Override
		public List<ItemStack> getOutputs(List<ItemStack> input, FluidStack fluid) {
			return base.getOutputs(input, fluid);
		}

		@Override
		public int getMPRequired(List<ItemStack> input, FluidStack fluid) {
			return base.getMPRequired(input, fluid);
		}

		@Override
		public List<FluidStack> getJEIFluidCache() {
			return base.getJEIFluidCache();
		}
		
		public boolean matches(List<ItemStack> stacks) {
			return ExactRecipeHelper.matches(stacks, getInputList());
		}
		
		public List<List<ItemStack>> getJEIItemStacksInput() {
			if (inputs == null) {
				inputs = getInputList().stream().map(ni -> ni.getCachedStacks()).collect(Collectors.toList());
			}
			return inputs;
		}
		
	}
}
