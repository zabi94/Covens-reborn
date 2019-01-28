package com.covens.common.content.ritual;

import static com.covens.api.ritual.EnumGlyphType.GOLDEN;

import com.covens.api.ritual.EnumGlyphType;
import com.covens.api.ritual.IRitual;
import com.covens.common.lib.LibMod;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.minerva.common.crafting.NumberedInput;

public class RitualFactory {

	private ResourceLocation rl;
	private int time = 0, altarStartingPower = 0, tickPower = 0;
	private NonNullList<ItemStack> output  = NonNullList.create();
	private NonNullList<NumberedInput> input = NonNullList.create();
	private EnumGlyphType small = null, medium = null, big = null;
	private boolean allowRemote = true;
	
	public RitualFactory(String modId, String ritualName) {
		this(new ResourceLocation(modId, ritualName));
	}
	
	public RitualFactory(ResourceLocation registryName) {
		rl = registryName;
	}
	
	public RitualFactory(String name) {
		this(new ResourceLocation(name));
	}

	public static RitualFactory create(String covensName) {
		return new RitualFactory(LibMod.MOD_ID, covensName);
	}
	
	public RitualFactory withRunningTime(int ticks) {
		time = ticks;
		return this;
	}
	
	public RitualFactory neverEnding() {
		time = -1;
		return this;
	}
	
	public RitualFactory withSmallCircle(EnumGlyphType type) {
		small = type;
		return this;
	}
	
	public RitualFactory withMediumCircle(EnumGlyphType type) {
		medium = type;
		return this;
	}
	
	public RitualFactory withBigCircle(EnumGlyphType type) {
		big = type;
		return this;
	}
	
	public RitualFactory withAllCircles(EnumGlyphType type) {
		big = type;
		medium = type;
		small = type;
		return this;
	}
	
	public RitualFactory withStartingPower(int amount) {
		altarStartingPower = amount;
		return this;
	}
	
	public RitualFactory withTickCost(int amount) {
		tickPower = amount;
		return this;
	}
	
	public RitualFactory addInput(NumberedInput ingredient) {
		input.add(ingredient); 
		return this;
	}
	
	public RitualFactory addInput(Ingredient ingredient, int amount) {
		input.add(new NumberedInput(ingredient, amount)); 
		return this;
	}
	
	public RitualFactory addInput(Ingredient ingredient) {
		input.add(new NumberedInput(ingredient, 1)); 
		return this;
	}
	
	public RitualFactory addOutput(ItemStack stack) {
		output.add(stack);
		return this;
	}
	
	public RitualFactory disallowRemote() {
		allowRemote = false;
		return this;
	}
	
	public void buildAndRegister(IRitual ritual) {
		AdapterIRitual rit = new AdapterIRitual(ritual);
		rit.setAllowRemote(allowRemote);
		rit.setAltarStartingPower(altarStartingPower);
		rit.setCircles(small, medium, big);
		rit.setInput(input);
		rit.setOutput(output);
		rit.setRegistryName(rl);
		rit.setTickPower(tickPower);
		rit.setTime(time);
		AdapterIRitual.REGISTRY.register(rit);
	}
	
	public void buildAndRegisterSimple() {
		buildAndRegister(new IRitual() {});
	}
	
	public static int circles(EnumGlyphType small, EnumGlyphType medium, EnumGlyphType big) {
		if (small == null) {
			throw new IllegalArgumentException("Cannot have the smaller circle missing");
		}
		if ((medium == null) && (big != null)) {
			throw new IllegalArgumentException("Cannot have null middle circle when a big circle is present");
		}
		if ((small == GOLDEN) || (medium == GOLDEN) || (big == GOLDEN)) {
			throw new IllegalArgumentException("No golden circles allowed");
		}
		int circleNum = 0;
		if (medium != null) {
			circleNum++;
		}
		if (big != null) {
			circleNum++;
		}
		return circleNum | (small.meta() << 2) | (medium == null ? 0 : medium.meta() << 4) | (big == null ? 0 : big.meta() << 6);
	}
}
