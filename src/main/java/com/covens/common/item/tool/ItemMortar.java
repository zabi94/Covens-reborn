package com.covens.common.item.tool;

import com.covens.common.item.ItemMod;

import net.minecraft.item.ItemStack;

public class ItemMortar extends ItemMod {

	public ItemMortar(String id) {
		super(id);
		this.setMaxDamage(20);
		this.setContainerItem(this);
		this.setNoRepair();
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		ItemStack returned = itemStack.copy();
		returned.setItemDamage(returned.getItemDamage() + 1);
		if (returned.getItemDamage() > returned.getMaxDamage()) {
			returned = ItemStack.EMPTY;
		}
		return returned;
	}
	
}
