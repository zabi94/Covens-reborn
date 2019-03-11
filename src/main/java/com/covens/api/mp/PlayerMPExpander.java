package com.covens.api.mp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public interface PlayerMPExpander {
	public ResourceLocation getID();

	public int getExtraAmount(EntityPlayer p);
}
