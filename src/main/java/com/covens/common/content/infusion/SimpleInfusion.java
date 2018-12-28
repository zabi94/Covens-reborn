package com.covens.common.content.infusion;

import com.covens.api.infusion.IInfusion;
import com.covens.common.lib.LibMod;
import net.minecraft.util.ResourceLocation;

public class SimpleInfusion implements IInfusion {

	private ResourceLocation rl, texture;
	private int dimension;

	public SimpleInfusion(String name, int dim) {
		dimension = dim;
		setRegistryName(new ResourceLocation(LibMod.MOD_ID, "infusion_" + name));
		texture = new ResourceLocation(LibMod.MOD_ID, "textures/gui/energy_" + name + ".png");
	}

	@Override
	public IInfusion setRegistryName(ResourceLocation name) {
		rl = name;
		return this;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return rl;
	}

	@Override
	public Class<IInfusion> getRegistryType() {
		return IInfusion.class;
	}

	@Override
	public int getDimensionAffinity() {
		return dimension;
	}

	@Override
	public ResourceLocation getTexture() {
		return texture;
	}

}
