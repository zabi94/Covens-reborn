package com.covens.common.content.infusion.capability;

import com.covens.api.infusion.DefaultInfusions;
import com.covens.api.infusion.IInfusion;
import com.covens.api.infusion.IInfusionCapability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class InfusionCapability implements IInfusionCapability {

	@CapabilityInject(IInfusionCapability.class)
	public static final Capability<IInfusionCapability> CAPABILITY = null;

	private IInfusion current = DefaultInfusions.NONE;

	public static void init() {
		CapabilityManager.INSTANCE.register(IInfusionCapability.class, new InfusionStorage(), InfusionCapability::new);
	}

	@Override
	public IInfusion getType() {
		return this.current == null ? DefaultInfusions.NONE : this.current;
	}

	@Override
	public void setType(IInfusion infusion) {
		this.current = infusion;
	}

}
