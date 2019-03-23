package com.covens.common.core.capability.altar;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.api.altar.IAltarPowerUpgrade;
import com.covens.api.altar.IAltarSpecialEffect;
import com.covens.api.altar.IAltarSpeedUpgrade;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import zabi.minecraft.minerva.common.entity.UUIDs;

public class FakeProvider implements ICapabilityProvider {
	
	private static final IAltarSpeedUpgrade gain = new IAltarSpeedUpgrade() {
		@Override
		public int getAmount() {
			return 0;
		}
	};
	
	private static final IAltarPowerUpgrade mult = new IAltarPowerUpgrade() {
		@Override
		public double getAmount() {
			return 0;
		}
	};
	
	private IAltarSpecialEffect effect;
	
	private boolean hasEf;
	private boolean hasGn;
	private boolean hasMl;
	
	public FakeProvider(boolean hasGain, boolean hasMult, String effectDescr) {
		this(hasGain, hasMult);
		hasEf = true;
		effect = new IAltarSpecialEffect() {
			@Override
			public void onApply(World world, BlockPos position) {
				//NO-OP
			}
			
			@Override
			public UUID getIdentifier() {
				return UUIDs.NULL_UUID;
			}
			
			@Override
			public String getDescriptionTranslationKey() {
				return effectDescr;
			}
		};
	}
	
	public FakeProvider(boolean hasGain, boolean hasMult) {
		hasEf = false;
		hasGn = hasGain;
		hasMl = hasMult;
		effect = null;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == AltarCapabilities.ALTAR_GAIN_CAPABILITY) {
			return hasGn;
		}
		if (capability == AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY) {
			return hasMl;
		}
		if (capability == AltarCapabilities.ALTAR_EFFECT_CAPABILITY) {
			return hasEf;
		}
		return false;
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == AltarCapabilities.ALTAR_GAIN_CAPABILITY && hasGn) {
			return AltarCapabilities.ALTAR_GAIN_CAPABILITY.cast(gain);
		}
		if (capability == AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY && hasMl) {
			return AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY.cast(mult);
		}
		if (capability == AltarCapabilities.ALTAR_EFFECT_CAPABILITY && hasEf) {
			return AltarCapabilities.ALTAR_EFFECT_CAPABILITY.cast(effect);
		}
		return null;
	}
}
