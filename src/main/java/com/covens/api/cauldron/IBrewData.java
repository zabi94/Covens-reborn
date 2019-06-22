package com.covens.api.cauldron;

import java.util.List;

import com.covens.common.content.cauldron.IBrewEntry;

public interface IBrewData extends ICauldronRecipe {
	public List<IBrewEntry> getEffects();
	public int getCostPerTick();
}
