package com.covens.api.incantation;

import net.minecraft.entity.player.EntityPlayer;

/**
 * This class was created by Arekkuusu on 19/04/2017.
 * It's distributed as part of Covens under
 * the MIT license.
 */
public interface IIncantation {

	void cast(EntityPlayer sender, String[] args);

	int getCost();
}
