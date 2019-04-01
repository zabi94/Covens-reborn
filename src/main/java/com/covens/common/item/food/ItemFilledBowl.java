package com.covens.common.item.food;

import java.util.List;

import javax.annotation.Nullable;

import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibItemName;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry.ItemStackHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFilledBowl extends ItemModFood {
	
	
	@ItemStackHolder(value = "covens:stew", nbt = "{hunger:5,saturation:5.0f}")
	public static final ItemStack stack = null;

	public ItemFilledBowl() {
		super(LibItemName.FILLED_BOWL, 0, 0f, true);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
		this.setMaxStackSize(16);
		this.setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			items.add(stack.copy());
		}
	}
	
	@Override
	public void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound()); // not really supposed to happen ingame since you only get the stews with NBT
														// values assigned but it prevents crashing
		}
		final FoodStats foodStats = player.getFoodStats();
		foodStats.addStats(stack.getTagCompound().getInteger("hunger"), stack.getTagCompound().getFloat("saturation"));
		player.addItemStackToInventory(new ItemStack(Items.BOWL, 1));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound()) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt.getInteger("hunger") > 0) {
				float roundedSaturation = Math.round(nbt.getFloat("saturation") * 10) / 10;
				tooltip.add(I18n.format("item.stew.description.generic"));
				if (GuiScreen.isShiftKeyDown()) {
					tooltip.add(I18n.format("item.stew.description.precise", nbt.getInteger("hunger"), roundedSaturation));
				}
			} else {
				tooltip.add(I18n.format("item.stew.description.empty"));
			}
		} else {
			tooltip.add(I18n.format("item.stew.description.empty"));
		}
	}
}
