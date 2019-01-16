package com.covens.common.item.magic.brew;

import java.util.List;

import com.covens.common.content.cauldron.BrewData;
import com.covens.common.content.cauldron.BrewData.BrewEntry;
import com.covens.common.content.cauldron.BrewModifierListImpl;
import com.covens.common.content.cauldron.CauldronRegistry;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.entity.EntityBrewArrow;
import com.covens.common.lib.LibItemName;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.blockmodels.IModelRegister;

public class ItemBrewArrow extends ItemArrow implements IModelRegister {

	public ItemBrewArrow() {
		super();
		this.setRegistryName(LibItemName.BREW_ARROW);
		this.setTranslationKey(LibItemName.BREW_ARROW);
		this.setCreativeTab(ModCreativeTabs.BREW_CREATIVE_TAB);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			CauldronRegistry.BREW_POTION_MAP.values().forEach(p -> this.addPotionType(items, p));
		}
	}

	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
		EntityBrewArrow brewArrow = new EntityBrewArrow(worldIn, shooter);
		brewArrow.setArrowStack(stack.copy().splitStack(1));
		return brewArrow;
	}

	private void addPotionType(NonNullList<ItemStack> items, Potion p) {
		BrewData data = new BrewData();
		BrewModifierListImpl list = new BrewModifierListImpl();
		data.addEntry(new BrewEntry(p, list));
		ItemStack stack = new ItemStack(this);
		data.saveToStack(stack);
		items.add(stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		ItemBrew.addTooltip(stack, worldIn, tooltip, flagIn);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Items.TIPPED_ARROW.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, modelResourceLocation);
	}

}
