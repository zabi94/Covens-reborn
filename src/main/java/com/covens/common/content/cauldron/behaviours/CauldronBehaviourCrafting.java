package com.covens.common.content.cauldron.behaviours;

import java.awt.Color;
import java.util.Optional;
import java.util.Random;

import com.covens.api.cauldron.ICauldronRecipe;
import com.covens.api.mp.MPUsingMachine;
import com.covens.common.content.cauldron.CauldronRegistry;
import com.covens.common.core.helper.Log;
import com.covens.common.tile.tiles.TileEntityCauldron;
import com.covens.common.tile.util.CauldronFluidTank;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class CauldronBehaviourCrafting implements ICauldronBehaviour {

	private static final String ID = "craft";
	private static final int MAX_CRAFT_TIME = 100;

	private int craftTime = 0, color = TileEntityCauldron.DEFAULT_COLOR;
	private boolean validRecipe = false, lowEnergy = false;
	private int currentRecipeMPCost = 0;
	private TileEntityCauldron cauldron;

	@Override
	public void setCauldron(TileEntityCauldron tile) {
		this.cauldron = tile;
	}

	@Override
	public void handleParticles(boolean isActiveBehaviour) {
		if (isActiveBehaviour) {
			if (this.lowEnergy) {
				Random r = this.cauldron.getWorld().rand;
				this.cauldron.getWorld().spawnParticle(EnumParticleTypes.SPELL_MOB, this.cauldron.getPos().getX() + 0.4 + (0.2 * r.nextDouble()), this.cauldron.getPos().getY() + 0.5, this.cauldron.getPos().getZ() + 0.4 + (0.2 * r.nextDouble()), 0xFF * (0.5 + (0.3 * this.cauldron.getWorld().rand.nextDouble() * 0.5)), 0, 0);
			} else if (this.validRecipe) {
				Random r = this.cauldron.getWorld().rand;
				this.cauldron.getWorld().spawnParticle(EnumParticleTypes.SPELL_INSTANT, this.cauldron.getPos().getX() + 0.4 + (0.2 * r.nextDouble()), this.cauldron.getPos().getY() + 0.5, this.cauldron.getPos().getZ() + 0.4 + (0.2 * r.nextDouble()), 0, 0, 0);
			}
		}
	}

	@Override
	public boolean canAccept(ItemStack stack) {
		return true;
	}

	@Override
	public boolean shouldInputsBeBlocked() {
		return (this.cauldron.getCurrentBehaviour() == this) && this.validRecipe;
	}

	@Override
	public void statusChanged(boolean isActiveBehaviour) {
		if (isActiveBehaviour && !this.cauldron.getInputs().isEmpty() && this.cauldron.getFluid().isPresent() && !this.validRecipe) {
			Optional<ICauldronRecipe> optional = CauldronRegistry.getCraftingResult(this.cauldron.getFluid().get(), this.cauldron.getInputs());
			this.validRecipe = optional.isPresent();
			if (validRecipe) {
				this.currentRecipeMPCost = optional.get().getMPRequired(this.cauldron.getInputs(), this.cauldron.getFluid().get());
			}
			this.color = Color.getHSBColor(this.cauldron.getWorld().rand.nextFloat(), 0.6f + (0.4f * this.cauldron.getWorld().rand.nextFloat()), this.cauldron.getWorld().rand.nextFloat()).getRGB();
			this.cauldron.markDirty();
			this.cauldron.syncToClient();
		}
	}

	@Override
	public void update(boolean isActiveBehaviour) {
		if (isActiveBehaviour) {
			boolean wasLowEnergy = this.lowEnergy;
			if (this.validRecipe && (this.craftTime < MAX_CRAFT_TIME)) {
				if (this.cauldron.getCapability(MPUsingMachine.CAPABILITY, null).drainAltarFirst(null, this.cauldron.getPos(), this.cauldron.getWorld().provider.getDimension(), this.currentRecipeMPCost/MAX_CRAFT_TIME)) {
					this.lowEnergy = false;
				} else {
					this.lowEnergy = true;
				}

				if (!this.lowEnergy) {
					this.craftTime++;
				}
				this.cauldron.markDirty();
				if (wasLowEnergy != this.lowEnergy) {
					this.cauldron.syncToClient();
				}
			}

			if (this.validRecipe && (this.craftTime >= MAX_CRAFT_TIME)) {
				ICauldronRecipe result = CauldronRegistry.getCraftingResult(this.cauldron.getFluid().orElse(new FluidStack(FluidRegistry.WATER, 0)), this.cauldron.getInputs()).orElse(null);
				if (result == null) {
					Log.w("This shouldn't happen... Please report to Covens Reborn\nCauldronBehaviourCrafting - update()\nRecipe output is null\nCauldron status:\ncurrent setting: " + this.cauldron.getCurrentBehaviour().getID() + "\nlow energy: " + this.lowEnergy + "\nvalid recipe: " + this.validRecipe + "\ncraft time: " + this.craftTime);
					Log.w("Item inside:");
					for (ItemStack stackInside:this.cauldron.getInputs()) {
						Log.w(stackInside);
					}
					this.cauldron.setBehaviour(this.cauldron.getDefaultBehaviours().FAILING);
					this.lowEnergy = false;
					this.validRecipe = false;
					this.currentRecipeMPCost = 0;
					this.craftTime = 0;
					this.cauldron.markDirty();
					return;
				}
				this.cauldron.setTankLock(true);
				CauldronFluidTank tank = (CauldronFluidTank) this.cauldron.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
				
				ItemStack resultItem = result.processOutput(this.cauldron.getInputs(), tank.getFluid());
				if (!resultItem.isEmpty()) {
					EntityItem e = new EntityItem(this.cauldron.getWorld(), this.cauldron.getPos().getX() + 0.5, this.cauldron.getPos().getY() + 0.5, this.cauldron.getPos().getZ() + 0.5, resultItem);
					e.addTag("cauldron_drop");
					e.motionY = 0.06;
					e.motionX = 0;
					e.motionZ = 0;
					this.cauldron.getWorld().spawnEntity(e);
				}
				tank.setFluid(result.processFluid(this.cauldron.getInputs(), tank.getFluid()));
				
				this.cauldron.setBehaviour(this.cauldron.getDefaultBehaviours().IDLE);
				this.lowEnergy = false;
				this.validRecipe = false;
				this.currentRecipeMPCost = 0;
				this.craftTime = 0;
				this.cauldron.clearItemInputs();// MD & StC called here
			}
		}
	}

	@Override
	public int getColor() {
		return this.color;
	}

	@Override
	public void saveToNBT(NBTTagCompound tag) {
		this.saveToSyncNBT(tag);
		tag.setInteger("craftTime", this.craftTime);

	}

	@Override
	public void loadFromNBT(NBTTagCompound tag) {
		this.loadFromSyncNBT(tag);
		this.craftTime = tag.getInteger("craftTime");
	}

	@Override
	public void saveToSyncNBT(NBTTagCompound tag) {
		tag.setInteger("color_craft", this.color);
		tag.setBoolean("hasRecipe", this.validRecipe);
		tag.setBoolean("lowEnergy", this.lowEnergy);
	}

	@Override
	public void loadFromSyncNBT(NBTTagCompound tag) {
		this.color = tag.getInteger("color_craft");
		this.validRecipe = tag.getBoolean("hasRecipe");
		this.lowEnergy = tag.getBoolean("lowEnergy");
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public void onDeactivation() {
		this.validRecipe = false;
		this.lowEnergy = false;
		this.color = TileEntityCauldron.DEFAULT_COLOR;
		this.craftTime = 0;
		this.cauldron.markDirty();
	}

}
