package com.covens.common.content.cauldron;

import com.covens.api.cauldron.IBrewModifierList;

import net.minecraft.potion.Potion;

public interface IBrewEntry {

	public Potion getPotion();

	public IBrewModifierList getModifierList();
}
