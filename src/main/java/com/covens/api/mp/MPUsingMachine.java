package com.covens.api.mp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * I highly suggest not to implement this interface on a class, but to have a
 * field on your machine and initializing it to the default implementation,
 * then using the provided methods to save and load the field to/from nbt
 *
 * @author zabi94
 */
public interface MPUsingMachine {

	@CapabilityInject(MPUsingMachine.class)
	public static final Capability<MPUsingMachine> CAPABILITY = null;

	public boolean drainAltarFirst(@Nullable EntityPlayer caster, @Nonnull BlockPos pos, int dimension, int amount);

	public NBTTagCompound writeToNbt();

	public void readFromNbt(NBTTagCompound tag);

	default boolean drainPlayerFirst(@Nullable EntityPlayer caster, @Nonnull BlockPos pos, int dimension, int amount) {
		if ((caster != null) && caster.getCapability(MPContainer.CAPABILITY, null).drain(amount)) {
			return true;
		}
		return drainAltarFirst(null, pos, dimension, amount);
	}

}
