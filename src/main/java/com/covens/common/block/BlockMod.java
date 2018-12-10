/**
 * This class was created by <ArekkuusuJerii>. It's distributed as
 * part of the Grimoire Of Alice Mod. Get the Source Code in github:
 * https://github.com/ArekkuusuJerii/Grimore-Of-Alice
 * <p>
 * Grimore Of Alice is Open Source and distributed under the
 * Grimore Of Alice license: https://github.com/ArekkuusuJerii/Grimore-Of-Alice/blob/master/LICENSE.md
 */
package com.covens.common.block;

import com.covens.client.core.IModelRegister;
import com.covens.client.handler.ModelHandler;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class was created by <Arekkuusu> on 26/02/2017.
 * It's distributed as part of Covens under
 * the MIT license.
 */
public class BlockMod extends Block implements IModelRegister {

	public BlockMod(String id, Material material) {
		super(material);
		setTranslationKey(id);
		setDefaultState(blockState.getBaseState());
		setRegistryName(LibMod.MOD_ID, id);
		setCreativeTab(ModCreativeTabs.BLOCKS_CREATIVE_TAB);
	}

	public BlockMod(String id, Material material, SoundType sound) {
		this(id, material);
		this.setSoundType(sound);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
