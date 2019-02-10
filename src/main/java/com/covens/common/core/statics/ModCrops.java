package com.covens.common.core.statics;

import com.covens.common.block.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;


public enum ModCrops {
	ACONITUM(Blocks.FARMLAND, 20), ASPHODEL(Blocks.FARMLAND), BELLADONNA(Blocks.FARMLAND, 20), GINGER(Blocks.FARMLAND), KELP(Blocks.WATER), MINT(Blocks.FARMLAND, 0), WHITE_SAGE(Blocks.FARMLAND), MANDRAKE(Blocks.FARMLAND, 20), LAVENDER(Blocks.FARMLAND, 0), THISTLE(Blocks.FARMLAND, 0), TULSI(Blocks.FARMLAND), KENAF(Blocks.FARMLAND), SILPHIUM(Blocks.FARMLAND), GARLIC(Blocks.FARMLAND, 0), WORMWOOD(Blocks.FARMLAND, 20), HELLEBORE(Blocks.FARMLAND, 20), SAGEBRUSH(Blocks.FARMLAND), INFESTED_WHEAT(ModBlocks.infested_farmland), CHRYSANTHEMUM(Blocks.FARMLAND);

	private final Block soil;
	private int mp_expansion_on_eat;

	ModCrops(Block soil) {
		this.soil = soil;
		this.mp_expansion_on_eat = 10;
	}

	ModCrops(Block soil, int magicValue) {
		this(soil);
		this.mp_expansion_on_eat = magicValue;
	}

	public Block getSoil() {
		return this.soil;
	}

	public int getMPExpansionOnEaten() {
		return this.mp_expansion_on_eat;
	}
}
