package com.covens.common.content.cauldron.behaviours;

import com.covens.common.tile.tiles.TileEntityCauldron;

public class DefaultBehaviours {

	public ICauldronBehaviour IDLE, FAILING, BREWING, CRAFTING, CLEANING, LAVA;

	public void init(TileEntityCauldron tile) {
		this.IDLE = new CauldronBehaviourIdle();
		this.LAVA = new CauldronBehaviourLava();
		this.FAILING = new CauldronBehaviourFailing();
		this.CLEANING = new CauldronBehaviourClean();
		this.BREWING = new CauldronBehaviourBrewing();
		this.CRAFTING = new CauldronBehaviourCrafting();

		this.IDLE.setCauldron(tile);
		this.LAVA.setCauldron(tile);
		this.FAILING.setCauldron(tile);
		this.CLEANING.setCauldron(tile);
		this.BREWING.setCauldron(tile);
		this.CRAFTING.setCauldron(tile);

		tile.addBehaviour(this.IDLE);
		tile.addBehaviour(this.LAVA);
		tile.addBehaviour(this.FAILING);
		tile.addBehaviour(this.CLEANING);
		tile.addBehaviour(this.BREWING);
		tile.addBehaviour(this.CRAFTING);

		tile.setBehaviour(this.IDLE);
	}
}
