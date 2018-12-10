package com.covens.common.item.tool;

import com.covens.api.CovensAPI;
import com.covens.client.core.IModelRegister;
import com.covens.client.handler.ModelHandler;
import com.covens.common.core.helper.MobHelper;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.integration.thaumcraft.ThaumcraftCompatBridge;
import com.covens.common.item.ModMaterials;
import com.covens.common.lib.LibItemName;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Joseph on 6/14/2018.
 */
public class ItemColdIronAxe extends ItemAxe implements IModelRegister {

	public ItemColdIronAxe() {
		super(ModMaterials.TOOL_COLD_IRON, 8.0f, -3.1f);
		this.setMaxStackSize(1);
		setRegistryName(LibItemName.COLD_IRON_AXE);
		setTranslationKey(LibItemName.COLD_IRON_AXE);
		setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
		if (!target.world.isRemote) {
			if ((target.getCreatureAttribute() == CovensAPI.getAPI().DEMON || target.getCreatureAttribute() == CovensAPI.getAPI().SPIRIT || target instanceof EntityEnderman || target instanceof EntityBlaze || target instanceof EntityVex || target instanceof EntityEndermite || target instanceof EntityGhast || target instanceof EntityWither || target instanceof EntityGuardian || MobHelper.isSpirit(target) || MobHelper.isDemon(target) || ThaumcraftCompatBridge.isTCSpiritMob(target)) && attacker instanceof EntityPlayer) {
				target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 12);
				stack.damageItem(5, attacker);
			} else {
				stack.damageItem(1, attacker);
			}
		}

		return true;
	}

	public String getNameInefficiently(ItemStack stack) {
		return getTranslationKey().substring(5);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.GRAY + I18n.format("witch.tooltip." + getNameInefficiently(stack) + "_description.name"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}