package com.covens.api.cauldron;

public interface ICauldronRecipe {
	
	public static final ICauldronRecipe NONE = () -> 0;
	
	public int getCostPerTick();
	
}
