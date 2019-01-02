package com.covens.common.core.capability.energy;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.api.mp.IMagicPowerConsumer;
import com.covens.api.mp.IMagicPowerContainer;
import com.covens.common.Covens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class MagicPowerConsumer implements IMagicPowerConsumer {

	BlockPos cachedPos = null;
	int cachedDim = 0;

	public static void init() {
		CapabilityManager.INSTANCE.register(IMagicPowerConsumer.class, new MagicPowerConsumerStorage(), MagicPowerConsumer::new);
	}

	@Override
	public boolean drainAltarFirst(@Nullable EntityPlayer caster, @Nonnull BlockPos pos, int dimension, int amount) {
		if (amount == 0) {
			return true;
		}
		if ((this.cachedPos == null) || !this.isValidAltar(DimensionManager.getWorld(this.cachedDim), this.cachedPos)) {
			this.findNewAltar(DimensionManager.getWorld(dimension), pos, 16);
		}
		if (this.cachedPos != null) {
			World tileWorld = DimensionManager.getWorld(this.cachedDim);
			IMagicPowerContainer source = tileWorld.getTileEntity(this.cachedPos).getCapability(IMagicPowerContainer.CAPABILITY, null);
			if (source.drain(amount)) {
				return true;
			}
		}
		if (caster != null) {
			return caster.getCapability(IMagicPowerContainer.CAPABILITY, null).drain(amount);
		}
		return false;
	}

	private void findNewAltar(World world, BlockPos position, int radius) {
		this.cachedPos = world.tickableTileEntities.parallelStream()//
				.filter(t -> !t.isInvalid())//
				.filter(t -> t.hasCapability(IMagicPowerContainer.CAPABILITY, null))//
				.filter(t -> t.getDistanceSq(position.getX(), position.getY(), position.getZ()) < (radius * radius))//
				.sorted(Comparator.<TileEntity>comparingDouble(t -> t.getDistanceSq(position.getX(), position.getY(), position.getZ())))//
				.map(t -> t.getPos())//
				.findFirst().orElse(null);
		if (this.cachedPos != null) {
			this.cachedDim = world.provider.getDimension();
		} else {
			this.cachedDim = 0;
		}

	}

	private boolean isValidAltar(World world, BlockPos position) {
		if ((world == null) || (position == null)) {
			Covens.logger.warn("Checked if null is a valid altar dimension/position. I won't crash, but that shouldn't happen");
			new NullPointerException().printStackTrace();
			return false;
		}
		TileEntity te = world.getTileEntity(this.cachedPos);
		return ((te != null) && !te.isInvalid() && te.hasCapability(IMagicPowerContainer.CAPABILITY, null));
	}

	@Override
	public NBTTagCompound writeToNbt() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("altarDim", this.cachedDim);
		if (this.cachedPos != null) {
			tag.setInteger("altarX", this.cachedPos.getX());
			tag.setInteger("altarY", this.cachedPos.getY());
			tag.setInteger("altarZ", this.cachedPos.getZ());
		}
		return tag;
	}

	@Override
	public void readFromNbt(NBTTagCompound tag) {
		this.cachedDim = tag.getInteger("altarDim");
		if (tag.hasKey("altarX")) {
			this.cachedPos = new BlockPos(tag.getInteger("altarX"), tag.getInteger("altarY"), tag.getInteger("altarZ"));
		}
	}

}
