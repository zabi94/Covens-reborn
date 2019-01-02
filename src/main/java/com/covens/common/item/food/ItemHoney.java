package com.covens.common.item.food;

import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibItemName;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * This class was created by Joseph on 3/4/2017. It's distributed as part of
 * Covens under the MIT license.
 */
public class ItemHoney extends ItemModFood {

	public ItemHoney() {
		super(LibItemName.HONEY, 2, 4F, false);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 450, 0));
		player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 450, 0));
	}
}
