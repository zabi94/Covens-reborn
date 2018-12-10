package com.covens.client.core;

import com.covens.common.lib.LibItemName;
import com.covens.common.lib.LibMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelResourceLocations {

	public static final ModelResourceLocation UNBOUND_LOCATION_STONE = new ModelResourceLocation(new ResourceLocation(LibMod.MOD_ID, LibItemName.LOCATION_STONE).toString());
	public static final ModelResourceLocation BOUND_LOCATION_STONE = new ModelResourceLocation(new ResourceLocation(LibMod.MOD_ID, LibItemName.LOCATION_STONE) + "_bound");

}
