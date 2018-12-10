package com.covens.common.block.decorations;

import com.covens.client.core.IModelRegister;
import com.covens.client.handler.ModelHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Joseph on 9/3/2017.
 */
public class BlockFakeIceSlabHalf extends BlockFakeIceSlab implements IModelRegister {

	//FIXME: Make these less screwy.

	public BlockFakeIceSlabHalf(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	public boolean isDouble() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
