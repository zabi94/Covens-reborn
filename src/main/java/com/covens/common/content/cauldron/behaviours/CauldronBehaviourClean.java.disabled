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
	private TileEntityCauldron cauldron;

	@Override
	public void setCauldron(TileEntityCauldron tile) {
		this.cauldron = tile;
	}

	@Override
	public void handleParticles(boolean isActiveBehaviour) {
		if (isActiveBehaviour) {
			for (int i = 0; i < 10; i++) {
				double px = this.cauldron.getPos().getX() + 0.5 + (this.cauldron.getWorld().rand.nextGaussian() * 0.5);
				double py = this.cauldron.getPos().getY() + 0.5 + this.cauldron.getWorld().rand.nextGaussian();
				double pz = this.cauldron.getPos().getZ() + 0.5 + (this.cauldron.getWorld().rand.nextGaussian() * 0.5);
				Covens.proxy.spawnParticle(ParticleF.CAULDRON_BUBBLE, px, py, pz, 0, 0.1, 0, this.cauldron.getColorRGB());
			}
		}
	}

	@Override
	public boolean canAccept(ItemStack stack) {
		return false;
	}

	@Override
	public boolean shouldInputsBeBlocked() {
		return this.cauldron.getCurrentBehaviour() == this;
	}

	@Override
	public void statusChanged(boolean isActiveBehaviour) {
		// NO-OP
	}

	@Override
	public void update(boolean isActiveBehaviour) {
		if (isActiveBehaviour && ((this.cauldron.getWorld().getTotalWorldTime() % 10) == 0)) {
			this.cauldron.setTankLock(true);
			this.cauldron.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).drain(100, true);
			this.cauldron.setTankLock(false);
			if (!this.cauldron.getFluid().isPresent() || (this.cauldron.getFluid().get().amount == 0)) {
				this.cauldron.clearItemInputs();
				this.cauldron.clearTanks();
				this.cauldron.setBehaviour(this.cauldron.getDefaultBehaviours().IDLE);
			}
		}
	}

	@Override
	public int getColor() {
		if (this.cauldron.getFluid().isPresent() && (this.cauldron.getFluid().get().getFluid() == FluidRegistry.LAVA)) {
			return this.cauldron.getDefaultBehaviours().LAVA.getColor();
		}
		return 0xb708d8;
	}

	@Override
	public void saveToNBT(NBTTagCompound tag) {
		// NO-OP
	}

	@Override
	public void loadFromNBT(NBTTagCompound tag) {
		// NO-OP
	}

	@Override
	public void saveToSyncNBT(NBTTagCompound tag) {
		// NO-OP
	}

	@Override
	public void loadFromSyncNBT(NBTTagCompound tag) {
		// NO-OP
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public void onDeactivation() {
		// NO-OP
	}

}
