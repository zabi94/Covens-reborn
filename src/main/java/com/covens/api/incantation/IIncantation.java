package com.covens.api.incantation;

import net.minecraft.entity.player.EntityPlayer;


public interface IIncantation {

	void cast(EntityPlayer sender, String[] args);

	int getCost();
}
