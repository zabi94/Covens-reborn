package com.covens.common.content.incantation;

import com.covens.api.incantation.IIncantation;
import com.covens.common.block.ModBlocks;

import net.minecraft.entity.player.EntityPlayer;

public class IncantationWitchFire implements IIncantation {

	@Override
	public void cast(EntityPlayer sender, String[] args) {
		if (sender.world.isAirBlock(sender.getPosition())) {
			sender.world.setBlockState(sender.getPosition(), ModBlocks.witchfire.getDefaultState(), 3);
		}
	}

	@Override
	public int getCost() {
		return 400;
	}

}
