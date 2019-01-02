package com.covens.common.content.transformation;

import com.covens.api.transformation.ITransformation;
import com.covens.common.lib.LibMod;

import net.minecraft.util.ResourceLocation;

public class SimpleTransformation implements ITransformation {

	private ResourceLocation rn;
	private boolean crossSalt;

	public SimpleTransformation(String name, boolean salt) {
		this.setRegistryName(new ResourceLocation(LibMod.MOD_ID, name));
		this.crossSalt = salt;
	}

	@Override
	public ITransformation setRegistryName(ResourceLocation name) {
		this.rn = name;
		return this;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return this.rn;
	}

	@Override
	public Class<ITransformation> getRegistryType() {
		return ITransformation.class;
	}

	@Override
	public boolean canCrossSalt() {
		return this.crossSalt;
	}

}
