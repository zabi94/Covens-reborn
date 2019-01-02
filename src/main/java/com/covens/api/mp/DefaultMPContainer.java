package com.covens.api.mp;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Add one of these in any object that hold MP for an easy solution
 *
 * @author zabi94
 */
public class DefaultMPContainer implements IMagicPowerContainer {

	private int amount = 0;
	private int maxAmount;

	public DefaultMPContainer(int max) {
		this.maxAmount = max;
		this.amount = max;
	}

	@Override
	public int getAmount() {
		return this.amount;
	}

	@Override
	public int getMaxAmount() {
		return this.maxAmount;
	}

	@Override
	public boolean fill(int in) {
		if (this.getAmount() < this.getMaxAmount()) {
			this.setAmount(Math.min(this.getAmount() + in, this.getMaxAmount()));
			return true;
		}
		return false;
	}

	@Override
	public boolean drain(int out) {
		if (this.getAmount() >= out) {
			this.setAmount(this.getAmount() - out);
			return true;
		}
		return false;
	}

	@Override
	public void setAmount(int newAmount) {
		if (newAmount > this.getMaxAmount()) {
			throw new MPManipulationException(String.format("The amount set (%d) is greater than the maximum amount (%d)", newAmount, this.getMaxAmount()));
		}
		if (newAmount < 0) {
			throw new MPManipulationException("The amount must be 0 or greater");
		}
		this.amount = newAmount;
	}

	@Override
	public void setMaxAmount(int newMaxAmount) {
		if (newMaxAmount < 0) {
			throw new MPManipulationException("The amount must be 0 or greater");
		}
		this.maxAmount = newMaxAmount;
		if (this.getAmount() > this.maxAmount) {
			this.setAmount(this.maxAmount);
		}
	}

	public NBTTagCompound saveNBTTag() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("current", this.getAmount());
		tag.setInteger("max", this.getMaxAmount());
		return tag;
	}

	public void loadFromNBT(NBTTagCompound tag) {
		this.setMaxAmount(tag.getInteger("max"));
		this.setAmount(tag.getInteger("current"));
	}

}
