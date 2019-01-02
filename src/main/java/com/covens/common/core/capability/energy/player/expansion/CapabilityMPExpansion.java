package com.covens.common.core.capability.energy.player.expansion;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.covens.api.mp.IMagicPowerExpander;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants.NBT;

public class CapabilityMPExpansion {

	@CapabilityInject(CapabilityMPExpansion.class)
	public static final Capability<CapabilityMPExpansion> CAPABILITY = null;

	private Map<String, Integer> increaseMap = new HashMap<String, Integer>();
	private boolean dirty = false;

	public static void init() {
		CapabilityManager.INSTANCE.register(CapabilityMPExpansion.class, new MagicPowerExpansionStorage(), () -> new CapabilityMPExpansion());
	}

	public void expand(IMagicPowerExpander exp, EntityPlayer player) {
		this.increaseMap.put(exp.getID().toString(), exp.getExtraAmount(player));
		this.dirty = true;
	}

	public void remove(ResourceLocation exp) {
		this.increaseMap.remove(exp.toString());
		this.dirty = true;
	}

	public void clean() {
		this.dirty = false;
	}

	/**
	 * @return whether or not it is synced with the player MP capability
	 */
	public boolean isDirty() {
		return this.dirty;
	}

	public int getTotalIncrease() {
		return this.increaseMap.values().parallelStream().reduce(0, (a, b) -> a + b);
	}

	public void readFromNBT(NBTTagCompound tag) {
		this.increaseMap.clear();
		tag.getTagList("exps", NBT.TAG_COMPOUND).forEach(s -> this.loadCouple(s));
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		tag.setTag("exps", list);
		this.increaseMap.entrySet().stream().forEach(entry -> this.addNewCouple(entry, list));
		return tag;
	}

	private void addNewCouple(Entry<String, Integer> entry, NBTTagList list) {
		NBTTagCompound couple = new NBTTagCompound();
		couple.setString("id", entry.getKey());
		couple.setInteger("val", entry.getValue());
		list.appendTag(couple);
	}

	private void loadCouple(NBTBase s) {
		NBTTagCompound tag = (NBTTagCompound) s;
		this.increaseMap.put(tag.getString("id"), tag.getInteger("val"));
	}
}
