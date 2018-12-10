package com.covens.common.content.cauldron.behaviours;

import com.covens.client.fx.ParticleF;
import com.covens.common.Covens;
import com.covens.common.tile.tiles.TileEntityCauldron;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class CauldronBehaviourClean implements ICauldronBehaviour {

	private static final String ID = "clean";
	TileEntityCauldron cauldron;

	@Override
	public void setCauldron(TileEntityCauldron tile) {
		cauldron = tile;
	}

	@Override
	public void handleParticles(boolean isActiveBehaviour) {
		if (isActiveBehaviour) {
			for (int i = 0; i < 10; i++) {
				double px = cauldron.getPos().getX() + 0.5 + cauldron.getWorld().rand.nextGaussian() * 0.5;
				double py = cauldron.getPos().getY() + 0.5 + cauldron.getWorld().rand.nextGaussian();
				double pz = cauldron.getPos().getZ() + 0.5 + cauldron.getWorld().rand.nextGaussian() * 0.5;
				Covens.proxy.spawnParticle(ParticleF.CAULDRON_BUBBLE, px, py, pz, 0, 0.1, 0, cauldron.getColorRGB());
			}
		}
	}

	@Override
	public boolean canAccept(ItemStack stack) {
		return false;
	}

	@Override
	public boolean shouldInputsBeBlocked() {
		return cauldron.getCurrentBehaviour() == this;
	}

	@Override
	public void statusChanged(boolean isActiveBehaviour) {

	}

	@Override
	public void update(boolean isActiveBehaviour) {
		if (isActiveBehaviour && cauldron.getWorld().getTotalWorldTime() % 10 == 0) {
			cauldron.setTankLock(true);
			cauldron.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).drain(100, true);
			cauldron.setTankLock(false);
			if (!cauldron.getFluid().isPresent() || cauldron.getFluid().get().amount == 0) {
				cauldron.clearItemInputs();
				cauldron.clearTanks();
				cauldron.setBehaviour(cauldron.getDefaultBehaviours().IDLE);
			}
		}
	}

	@Override
	public int getColor() {
		if (cauldron.getFluid().isPresent() && cauldron.getFluid().get().getFluid() == FluidRegistry.LAVA) {
			return cauldron.getDefaultBehaviours().LAVA.getColor();
		}
		return 0xb708d8;
	}

	@Override
	public void saveToNBT(NBTTagCompound tag) {

	}

	@Override
	public void loadFromNBT(NBTTagCompound tag) {

	}

	@Override
	public void saveToSyncNBT(NBTTagCompound tag) {

	}

	@Override
	public void loadFromSyncNBT(NBTTagCompound tag) {

	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public void onDeactivation() {

	}

}
