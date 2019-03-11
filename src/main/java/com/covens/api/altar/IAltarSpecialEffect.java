package com.covens.api.altar;

import java.util.UUID;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IAltarSpecialEffect { 
	
	public void onApply(World world, BlockPos position);

	public UUID getIdentifier();
	 
}
