package com.covens.common.item.tool;

import javax.annotation.Nonnull;

import com.covens.common.core.helper.MobHelper;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.item.ModMaterials;
import com.covens.common.lib.LibItemName;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.blockmodels.IModelRegister;
import zabi.minecraft.minerva.client.blockmodels.ModelHandler;

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

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
