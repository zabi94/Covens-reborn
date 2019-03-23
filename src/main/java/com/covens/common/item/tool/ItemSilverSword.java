package com.covens.common.item.tool;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.covens.common.core.capability.altar.EffectProvider;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.item.ModMaterials;
import com.covens.common.lib.LibItemName;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
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

public class ItemSilverSword extends ItemSword implements IModelRegister {

	public static final UUID SILVER_UUID = UUID.fromString("a06cdaba-5c55-48bf-8b36-5a31c5b9ab5b");
	private static final EffectProvider cap = new EffectProvider(SILVER_UUID, "altar.effect.tooltip.silver");

	public ItemSilverSword() {
		super(ModMaterials.TOOL_SILVER);
		this.setMaxStackSize(1);
		this.setRegistryName(LibItemName.SILVER_SWORD);
		this.setTranslationKey(LibItemName.SILVER_SWORD);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
		if (!target.world.isRemote) {
			if ((target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) && (attacker instanceof EntityPlayer)) {
				target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 12);
				stack.damageItem(25, attacker);
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
	
	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return cap;
	}
}
