package com.covens.common.block.natural;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

public enum Gem implements IStringSerializable {
	
	GARNET, 
//	NUUMMITE, //NUKED
	TIGERS_EYE, 
	TOURMALINE, 
//	BLOODSTONE, //NUKED (?)
//	JASPER, //NUKED
	MALACHITE; 
//	AMETHYST, //NUKED
//	ALEXANDRITE; //NUKED
	
	private Block gemBlock;
	private Block oreBlock;
	private Item gemItem;
	
	public Item setGemItem(Item itemIn) {
		if (this.gemItem != null) {
			throw new IllegalStateException("Can't set the gem item twice for Gem "+this.getName());
		}
		this.gemItem = itemIn;
		return itemIn;
	}
	
	public Block setOreBlock(Block blockIn) {
		if (this.oreBlock != null) {
			throw new IllegalStateException("Can't set the ore block twice for Gem "+this.getName());
		}
		this.oreBlock = blockIn;
		return blockIn;
	}
	
	public Block setGemBlock(Block blockIn) {
		if (this.gemBlock != null) {
			throw new IllegalStateException("Can't set the gem block twice for Gem "+this.getName());
		}
		this.gemBlock = blockIn;
		return blockIn;
	}

	@Override
	public String getName() {
		return this.name().toLowerCase();
	}
	
	public String getBlockName() {
		return getName()+"_block";
	}
	
	public String getGemName() {
		return "gem_"+getName();
	}
	
	public String getOreName() {
		return getName()+"_ore";
	}
	
	public Block getGemBlock() {
		return this.gemBlock;
	}

	public Block getOreBlock() {
		return this.oreBlock;
	}

	public Item getGemItem() {
		return this.gemItem;
	}
	
}