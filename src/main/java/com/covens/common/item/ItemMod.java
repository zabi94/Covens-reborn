/**
 * This class was created by <ArekkuusuJerii>. It's distributed as
 * part of the Grimoire Of Alice Mod. Get the Source Code in github:
 * https://github.com/ArekkuusuJerii/Grimore-Of-Alice
 * <p>
 * Grimore Of Alice is Open Source and distributed under the
 * Grimore Of Alice license: https://github.com/ArekkuusuJerii/Grimore-Of-Alice/blob/master/LICENSE.md
 */
package com.covens.common.item;

import com.covens.client.core.IModelRegister;
import com.covens.client.handler.ModelHandler;
import com.covens.common.core.statics.ModCreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class was created by <Arekkuusu> on 26/02/2017.
 * It's distributed as part of Covens under
 * the MIT license.
 */
public class ItemMod extends Item implements IModelRegister {

	public ItemMod(String id) {
		super();
		setRegistryName(id);
		setTranslationKey(id);
		setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
