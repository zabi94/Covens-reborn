package com.covens.common.item.tool;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.common.core.capability.altar.EffectProvider;
import com.covens.common.core.helper.MobHelper;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.item.ModMaterials;
import com.covens.common.lib.LibItemName;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.blockmodels.IModelRegister;
import zabi.minecraft.minerva.client.blockmodels.ModelHandler;

/**
 * Created by Joseph on 6/14/2018.
 */
public class ItemColdIronSword extends ItemSword implements IModelRegister {
	
	public static final UUID COLD_IRON_UUID = UUID.fromString("a06cdafa-5c55-48bf-8b32-5a31c5b9ab5b");
	private static final EffectProvider cap = new EffectProvider(COLD_IRON_UUID, "altar.effect.tooltip.cold_iron");

	public ItemColdIronSword() {
		super(ModMaterials.TOOL_COLD_IRON);
		this.setMaxStackSize(1);
		this.setRegistryName(LibItemName.COLD_IRON_SWORD);
		this.setTranslationKey(LibItemName.COLD_IRON_SWORD);
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
	
	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return cap;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
