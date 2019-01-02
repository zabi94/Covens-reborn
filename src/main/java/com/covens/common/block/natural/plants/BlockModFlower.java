package com.covens.common.block.natural.plants;

import com.covens.client.core.IModelRegister;
import com.covens.client.handler.ModelHandler;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibMod;

import net.minecraft.block.BlockBush;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockModFlower extends BlockBush implements IModelRegister {

	public BlockModFlower(String name) {
		this.setRegistryName(LibMod.MOD_ID, name);
		this.setTranslationKey(name);
		this.setCreativeTab(ModCreativeTabs.PLANTS_CREATIVE_TAB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
