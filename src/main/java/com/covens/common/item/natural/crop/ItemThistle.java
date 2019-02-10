package com.covens.common.item.natural.crop;

import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibItemName;

import net.minecraft.init.MobEffects;


public class ItemThistle extends ItemCropFood {

	public ItemThistle() {
		super(LibItemName.THISTLE, 4, 0.8F, false);
		this.addPotion(MobEffects.STRENGTH);
		this.setCreativeTab(ModCreativeTabs.PLANTS_CREATIVE_TAB);
	}
}
