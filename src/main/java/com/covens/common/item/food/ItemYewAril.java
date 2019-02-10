package com.covens.common.item.food;

import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibItemName;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;


public class ItemYewAril extends ItemModFood {

	public ItemYewAril() {
		super(LibItemName.YEW_ARIL, 1, 0.5F, false);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
		this.setPotionEffect(new PotionEffect(MobEffects.POISON, 450, 0), 0.1F);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		player.getFoodStats().addStats(1, 0.2f);
	}
}
