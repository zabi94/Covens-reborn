package com.covens.common.content.cauldron.behaviours;

import java.util.Random;

import com.covens.client.fx.ParticleF;
import com.covens.common.Covens;
import com.covens.common.content.cauldron.CauldronRegistry;
import com.covens.common.core.statics.ModFluids;
import com.covens.common.core.statics.ModSounds;
import com.covens.common.item.ModItems;
import com.covens.common.tile.tiles.TileEntityCauldron;
import com.covens.common.tile.util.CauldronFluidTank;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class CauldronBehaviourIdle implements ICauldronBehaviour {

	private static final int MAX_HEAT = 150, BOIL_THRESHOLD = 100;
	private static final String ID = "idle";

	private TileEntityCauldron cauldron;
	private int heat = 0;

	@Override
	public void setCauldron(TileEntityCauldron tile) {
		this.cauldron = tile;
	}

	@Override
	public void handleParticles(boolean active) {
		BlockPos pos = this.cauldron.getPos();
		World world = this.cauldron.getWorld();
		Random rand = world.rand;
		CauldronFluidTank tank = (CauldronFluidTank) this.cauldron.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
		float level = tank.getFluidAmount() / (Fluid.BUCKET_VOLUME * 2F);
		level = pos.getY() + 0.1F + level;
		if (this.heat >= BOIL_THRESHOLD) {
			Fluid fluid = tank.getInnerFluid();
			if ((fluid == FluidRegistry.WATER) || (fluid == ModFluids.MUNDANE_OIL) || (fluid == ModFluids.HONEY)) {
				for (int i = 0; i < 2; i++) {
					double posX = pos.getX() + 0.2D + (world.rand.nextDouble() * 0.6D);
					double posZ = pos.getZ() + 0.2D + (world.rand.nextDouble() * 0.6D);
					Covens.proxy.spawnParticle(ParticleF.CAULDRON_BUBBLE, posX, level, posZ, 0, 0, 0, this.cauldron.getColorRGB());
				}
				if (rand.nextInt(4) == 0) {
					world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.BOIL, SoundCategory.BLOCKS, rand.nextFloat() * 0.1F, 0.5F + (rand.nextFloat() * 0.8f), true);
				}
			} else if (fluid == FluidRegistry.LAVA) {
				if (rand.nextInt(5) == 0) {
					double posX = pos.getX() + 0.2D + (world.rand.nextDouble() * 0.6D);
					double posZ = pos.getZ() + 0.2D + (world.rand.nextDouble() * 0.6D);
					world.spawnParticle(EnumParticleTypes.LAVA, posX, level, posZ, 0, 0.1, 0);
				}
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + (rand.nextFloat() * 0.6F), 0.9F + (rand.nextFloat() * 0.15F), false);
			} else {
				if (rand.nextInt(4) == 0) {
					world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.BOIL, SoundCategory.BLOCKS, 0.2F + (rand.nextFloat() * 0.2F), 1F + (rand.nextFloat() * 0.8f), true);
				}
			}
		}
	}

	@Override
	public boolean canAccept(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canAccept(EntityItem itemEntity) {
		return ICauldronBehaviour.super.canAccept(itemEntity) && !itemEntity.getTags().contains("cauldron_drop");
	}

	@Override
	public boolean shouldInputsBeBlocked() {
		return (this.heat < BOIL_THRESHOLD) || !this.cauldron.getFluid().isPresent() || (this.cauldron.getFluid().get().amount <= 0);
	}

	@Override
	public void update(boolean isActive) {
		IBlockState below = this.cauldron.getWorld().getBlockState(this.cauldron.getPos().down());
		boolean wasBoiling = this.heat >= MAX_HEAT;
		if (this.cauldron.getFluid().isPresent()) {
			if ((below.getMaterial() == Material.FIRE) || (below.getMaterial() == Material.LAVA)) {
				if (this.heat < MAX_HEAT) {
					this.heat++;
				}
			} else {
				if (this.heat > 0) {
					this.heat--;
				}
			}
			if (this.cauldron.getFluid().get().getFluid().getTemperature() > 800) {
				this.heat = MAX_HEAT;
			}
			this.cauldron.markDirty();
			boolean isBoilingNow = this.heat >= MAX_HEAT;
			if (isBoilingNow != wasBoiling) {
				this.cauldron.syncToClient();
			}
		}
	}

	@Override
	public int getColor() {
		return TileEntityCauldron.DEFAULT_COLOR;
	}

	@Override
	public void saveToNBT(NBTTagCompound tag) {
		tag.setInteger("heat", this.heat);
	}

	@Override
	public void loadFromNBT(NBTTagCompound tag) {
		this.heat = tag.getInteger("heat");
	}

	@Override
	public void saveToSyncNBT(NBTTagCompound tag) {
		this.saveToNBT(tag);
	}

	@Override
	public void loadFromSyncNBT(NBTTagCompound tag) {
		this.loadFromNBT(tag);
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public void statusChanged(boolean isActive) {
		if ((this.cauldron.getInputs().size() > 0) && (this.cauldron.getInputs().get(this.cauldron.getInputs().size() - 1).getItem() == ModItems.wood_ash)) {
			this.cauldron.setBehaviour(this.cauldron.getDefaultBehaviours().CLEANING);
			this.cauldron.setTankLock(false);
		} else if (isActive) {
			if (this.cauldron.getInputs().size() > 0) {
				ItemStack stack = this.cauldron.getInputs().get(this.cauldron.getInputs().size() - 1);
				if (this.cauldron.getFluid().isPresent() && (this.cauldron.getFluid().get().getFluid() == FluidRegistry.LAVA)) {
					this.cauldron.setBehaviour(this.cauldron.getDefaultBehaviours().LAVA);
				} else if ((stack.getItem() == Items.NETHER_WART) && (this.cauldron.getFluid().get().getFluid() == FluidRegistry.WATER)) {
					this.cauldron.setBehaviour(this.cauldron.getDefaultBehaviours().BREWING);
					this.cauldron.setTankLock(false);
				} else if ((CauldronRegistry.getCauldronFoodValue(stack) != null) && (this.cauldron.getFluid().get().getFluid() == FluidRegistry.WATER)) {
					this.cauldron.setBehaviour(this.cauldron.getDefaultBehaviours().STEW);
					this.cauldron.setTankLock(false);
				} else {
					this.cauldron.setBehaviour(this.cauldron.getDefaultBehaviours().CRAFTING);
					this.cauldron.setTankLock(false);
				}
			}
		}

		if (!this.cauldron.getFluid().isPresent() || (this.cauldron.getFluid().get().amount <= 0)) {
			if (this.cauldron.getInputs().size() > 0) {
				this.cauldron.clearItemInputs();
			}
			this.heat = 0;
			this.cauldron.setBehaviour(this);
			this.cauldron.setTankLock(true);
			this.cauldron.markDirty();
			this.cauldron.syncToClient();
		}
	}

	@Override
	public void onDeactivation() {

	}

}
