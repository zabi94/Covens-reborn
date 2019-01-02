package com.covens.common.item.tool;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.client.core.IModelRegister;
import com.covens.client.handler.ModelHandler;
import com.covens.common.core.helper.MobHelper;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.item.ModMaterials;
import com.covens.common.lib.LibItemName;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Joseph on 6/14/2018.
 */
public class ItemColdIronHoe extends ItemHoe implements IModelRegister {

	public ItemColdIronHoe() {
		super(ModMaterials.TOOL_COLD_IRON);
		this.setMaxStackSize(1);
		this.setRegistryName(LibItemName.COLD_IRON_HOE);
		this.setTranslationKey(LibItemName.COLD_IRON_HOE);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
		if (!target.world.isRemote) {
			if ((MobHelper.isSpirit(target) || MobHelper.isDemon(target)) && (attacker instanceof EntityPlayer)) {
				target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 12);
				stack.damageItem(5, attacker);
			} else {
				stack.damageItem(1, attacker);
			}
		}

		return true;
	}

	public String getNameInefficiently(ItemStack stack) {
		return this.getTranslationKey().substring(5);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.GRAY + I18n.format("witch.tooltip." + this.getNameInefficiently(stack) + "_description.name"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
