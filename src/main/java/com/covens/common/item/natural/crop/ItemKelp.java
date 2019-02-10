package com.covens.common.item.natural.crop;

import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibItemName;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;


public class ItemKelp extends ItemCropFood {

	public ItemKelp() {
		super(LibItemName.KELP, 3, 0.8F, false);
		this.setCreativeTab(ModCreativeTabs.PLANTS_CREATIVE_TAB);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 10, 0));
	}
}
