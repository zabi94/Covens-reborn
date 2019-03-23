package com.covens.common.core.capability.altar;

import java.util.UUID;

import com.covens.api.altar.IAltarSpecialEffect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CapabilityAltarSpecialEffect implements IAltarSpecialEffect {
	
	private UUID id;
	private String descrKey;

	public CapabilityAltarSpecialEffect(UUID amt, String tooltip) {
		id = amt;
		descrKey = tooltip;
	}

	@Override
	public void onApply(World world, BlockPos position) {
		// No op
	}

	@Override
	public UUID getIdentifier() {
		return id;
	}
	
	@Override
	public String getDescriptionTranslationKey() {
		return descrKey;
	}
	
}
