package com.covens.common.core.capability.altar.tile_providers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.common.core.capability.altar.MultProvider;

import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class FlowerPotProvider extends MultProvider {
	
	private TileEntityFlowerPot pot;
	
	public FlowerPotProvider(TileEntityFlowerPot flowerPot) {
		super(0.12);
		this.pot = flowerPot;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		if (!pot.getFlowerItemStack().isEmpty()) {
			return super.hasCapability(capability, facing);
		}
		return false;
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (!pot.getFlowerItemStack().isEmpty()) {
			return super.getCapability(capability, facing);
		}
		return null;
	}

}
