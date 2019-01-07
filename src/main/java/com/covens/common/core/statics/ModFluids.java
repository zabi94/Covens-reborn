package com.covens.common.core.statics;

import java.util.function.Consumer;

import com.covens.common.lib.LibMod;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public final class ModFluids {

	public static final Fluid HONEY = createFluid("liquid_honey", false, fluid -> fluid.setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA).setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA).setDensity(1500).setViscosity(8000));
	public static final Fluid MUNDANE_OIL = createFluid("oil_mundane", true, fluid -> fluid.setDensity(800).setViscosity(4000));

	private static Fluid createFluid(String name, boolean hasFlowIcon, Consumer<Fluid> fluidPropertyApplier) {
		final ResourceLocation still = new ResourceLocation(LibMod.MOD_ID, "fluids/" + name + "_still");
		final ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(LibMod.MOD_ID, "fluids/" + name + "_flow") : still;
		Fluid fluid = new Fluid(name, still, flowing);
		final boolean useOwnFluid = FluidRegistry.registerFluid(fluid);
		if (useOwnFluid) {
			fluidPropertyApplier.accept(fluid);
			FluidRegistry.addBucketForFluid(fluid);
		} else {
			fluid = FluidRegistry.getFluid(name);
		}
		return fluid;
	}
}
