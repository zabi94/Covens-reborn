package com.covens.common.item.equipment;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.covens.api.CovensAPI;
import com.covens.client.render.entity.model.ModelVampireArmor;
import com.covens.common.content.transformation.vampire.CapabilityVampire;
import com.covens.common.core.statics.ModCreativeTabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.blockmodels.IModelRegister;
import zabi.minecraft.minerva.client.blockmodels.ModelHandler;

public class ItemVampireArmor extends ItemArmor implements IModelRegister {

	public ItemVampireArmor(String id, ArmorMaterial materialIn, int renderIndex, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndex, equipmentSlotIn);
		this.setMaxStackSize(1);
		this.setRegistryName(id);
		this.setTranslationKey(id);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
		if (equipmentSlotIn == EntityEquipmentSlot.HEAD && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote) {
			if (itemStack.getItemDamage() > 0 && CovensAPI.getAPI().addVampireBlood(player, -2)) {
				itemStack.setItemDamage(itemStack.getItemDamage() - 1);
			}
			switch (this.armorType) {
				case CHEST:
					break;
				case HEAD:
					break;
				case LEGS:
					break;
				default:
					break;

			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Minecraft.getMinecraft().player != null) {
			tooltip.add("");
			String prefix = TextFormatting.GRAY.toString();
			Iterator<ItemStack> it = Minecraft.getMinecraft().player.getArmorInventoryList().iterator();
			while (it.hasNext()) {
				if (it.next().getItem() == this) {
					prefix = TextFormatting.RED.toString();
					break;
				}
			}
			switch (this.armorType) {
				case CHEST:
					tooltip.add(prefix+I18n.format("item.vampire_vest.effect"));
					break;
				case HEAD:
					tooltip.add(prefix+I18n.format("item.vampire_hat.effect"));
					break;
				case LEGS:
					tooltip.add(prefix+I18n.format("item.vampire_pants.effect"));
					break;
				default:
					break;
			}
			
			String fsprefix = hasFullSet(Minecraft.getMinecraft().player)?TextFormatting.DARK_RED.toString():TextFormatting.DARK_GRAY.toString();
			tooltip.add(fsprefix+I18n.format("item.vampire_set.effect"));
		}
	}
	
	
	
	@SubscribeEvent
	public void onEquipmentChange(LivingEquipmentChangeEvent event) {
		if (event.getSlot().getSlotType() == Type.ARMOR && event.getEntityLiving() instanceof EntityPlayer && event.getSlot()!=EntityEquipmentSlot.FEET) {
			Iterator<ItemStack> it = event.getEntityLiving().getArmorInventoryList().iterator();
			event.getEntityLiving().getCapability(CapabilityVampire.CAPABILITY, null).armorPieces = 0;
			while (it.hasNext()) {
				if (it.next().getItem() instanceof ItemVampireArmor) {
					event.getEntityLiving().getCapability(CapabilityVampire.CAPABILITY, null).armorPieces++;
				}
			}
		}
	}
	
	private static boolean hasFullSet(EntityLivingBase p) {
		if (p != null) {
			Iterator<ItemStack> it = p.getArmorInventoryList().iterator();
			int amount = 0;
			while (it.hasNext()) {
				if (it.next().getItem() instanceof ItemVampireArmor) {
					amount++;
				}
			}	
			return amount == 3;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		if (itemStack.getItem() instanceof ItemArmor) {

			ModelVampireArmor.INSTANCE.hatAnchor.showModel = armorSlot == EntityEquipmentSlot.HEAD;
			ModelVampireArmor.INSTANCE.body.showModel = armorSlot == EntityEquipmentSlot.CHEST;
			ModelVampireArmor.INSTANCE.capeCollarBack1.showModel = armorSlot == EntityEquipmentSlot.CHEST;
			ModelVampireArmor.INSTANCE.capeCollarRight1.showModel = armorSlot == EntityEquipmentSlot.CHEST;
			ModelVampireArmor.INSTANCE.capeCollarLeft1.showModel = armorSlot == EntityEquipmentSlot.CHEST;
			ModelVampireArmor.INSTANCE.capeBack1.showModel = armorSlot == EntityEquipmentSlot.CHEST;
			ModelVampireArmor.INSTANCE.armLeft.showModel = armorSlot == EntityEquipmentSlot.CHEST;
			ModelVampireArmor.INSTANCE.armRight.showModel = armorSlot == EntityEquipmentSlot.CHEST;
			ModelVampireArmor.INSTANCE.legLeft.showModel = armorSlot == EntityEquipmentSlot.LEGS;
			ModelVampireArmor.INSTANCE.legRight.showModel = armorSlot == EntityEquipmentSlot.LEGS;

			ModelVampireArmor.INSTANCE.isChild = _default.isChild;
			ModelVampireArmor.INSTANCE.isRiding = _default.isRiding;
			ModelVampireArmor.INSTANCE.isSneak = _default.isSneak;
			ModelVampireArmor.INSTANCE.rightArmPose = _default.rightArmPose;
			ModelVampireArmor.INSTANCE.leftArmPose = _default.leftArmPose;

			return ModelVampireArmor.INSTANCE;
		}

		return null;

	}

}
