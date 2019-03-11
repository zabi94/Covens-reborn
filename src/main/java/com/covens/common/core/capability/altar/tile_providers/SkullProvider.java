package com.covens.common.core.capability.altar.tile_providers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.api.altar.IAltarPowerUpgrade;
import com.covens.api.altar.IAltarSpeedUpgrade;
import com.covens.common.core.capability.altar.AltarCapabilities;

import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class SkullProvider implements ICapabilityProvider {

	private IAltarSpeedUpgrade gain;
	private IAltarPowerUpgrade mult;
	
	public SkullProvider(TileEntitySkull skull) {
		gain = new SkullDependentGain(skull);
		mult = new SkullDependentMult(skull);
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == AltarCapabilities.ALTAR_GAIN_CAPABILITY || capability == AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY;
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == AltarCapabilities.ALTAR_GAIN_CAPABILITY) {
			return AltarCapabilities.ALTAR_GAIN_CAPABILITY.cast(gain);
		}
		if (capability == AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY) {
			return AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY.cast(mult);
		}
		return null;
	}

	private static class SkullDependentGain implements IAltarSpeedUpgrade {
		
		private int skull;
		
		public SkullDependentGain(TileEntitySkull skull) {
			this.skull = skull.getSkullType();
		}

		@Override
		public int getAmount() {
			switch (skull) {
				case 0:
				case 2:
				case 4: // Zombie, Skeleton and creeper
					return 1;
				case 1:
				case 3:// Wither skull and player skull
					return 2;
				case 5: // Dragon
					return 2;
				default:
					break;
			}
			return 0;
		}

	}
	
	private static class SkullDependentMult implements IAltarPowerUpgrade {
		
		private int skull;
		
		public SkullDependentMult(TileEntitySkull skull) {
			this.skull = skull.getSkullType();
		}

		@Override
		public double getAmount() {
			switch (skull) {
				case 0:
				case 2:
				case 4: // Zombie, Skeleton and creeper
					return 0.05;
				case 1:
				case 3:// Wither skull and player skull
					return 0.2;
				case 5: // Dragon
					return 0.4;
				default:
					break;
			}
			return 0;
		}

	}

}
