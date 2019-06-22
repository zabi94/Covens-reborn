package com.covens.common.tile.util;

public enum CauldronMode {
	
	IDLE(true, true, true, 0), 
	FAILING(false, false, false, 0), 
	LAVA(true, true, true, 0), 
	CRAFTING_UNFINISHED(false, false, true, 0), 
	CRAFTING_ABSORBING(false, false, false, 200), 
	BREW_UNFINISHED(false, false, true, 0),
	BREW_ABSORBING(false, false, false, 100),
	CLEANING(false, false, false, 0);
	
	public final boolean canInsertLiquid, canExtractLiquid, canInsertItems;
	public final int work;
	
	private CauldronMode(boolean insertFluid, boolean extractFluid, boolean insertItems, int workGoal) {
		this.canInsertLiquid = insertFluid;
		this.canExtractLiquid = extractFluid;
		this.canInsertItems = insertItems;
		this.work = workGoal;
	}
}
