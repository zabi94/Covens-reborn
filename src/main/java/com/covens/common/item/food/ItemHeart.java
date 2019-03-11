package com.covens.common.item.food;

import com.covens.api.CovensAPI;
import com.covens.api.mp.PlayerMPExpander;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibItemName;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;


public class ItemHeart extends ItemModFood implements PlayerMPExpander {

	public ItemHeart() {
		super(LibItemName.HEART, 8, 1.6F, true);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
		this.setAlwaysEdible();
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 750, 3));
		player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 750, 3));
		CovensAPI.getAPI().expandPlayerMP(this, player);
	}

	@Override
	public ResourceLocation getID() {
		return this.getRegistryName();
	}

	@Override
	public int getExtraAmount(EntityPlayer p) {
		return 40;
	}
}
