package com.covens.common.item.block;

import com.covens.common.item.ItemBlockMod;

import net.minecraft.block.Block;
import zabi.minecraft.minerva.client.blockmodels.IModelRegister;
import zabi.minecraft.minerva.client.blockmodels.ModelHandler;

public class ItemGemOre extends ItemBlockMod implements IModelRegister {

	public ItemGemOre(Block block) {
		super(block);
		this.setRegistryName(block.getRegistryName());
		this.setMaxDamage(0);
	}

	@Override
	public void registerModel() {
		ModelHandler.registerForgeModel(this, 0, "inventory");
	}
}
