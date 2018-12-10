package com.covens.common.item.natural.crop;

import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibItemName;
import net.minecraft.init.MobEffects;

/**
 * This class was created by Joseph on 02/03/2017.
 * It's distributed as part of Covens under
 * the MIT license.
 */
public class ItemThistle extends ItemCropFood {

	public ItemThistle() {
		super(LibItemName.THISTLE, 4, 0.8F, false);
		addPotion(MobEffects.STRENGTH);
		setCreativeTab(ModCreativeTabs.PLANTS_CREATIVE_TAB);
	}
}
