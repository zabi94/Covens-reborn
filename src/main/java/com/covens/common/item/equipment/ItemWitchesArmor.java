package com.covens.common.item.equipment;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.covens.api.CovensAPI;
import com.covens.api.mp.MPContainer;
import com.covens.api.mp.PlayerMPExpander;
import com.covens.client.render.entity.model.ModelWitchesArmor;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.lib.LibMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.client.blockmodels.IModelRegister;
import zabi.minecraft.minerva.client.blockmodels.ModelHandler;

public class ItemWitchesArmor extends ItemArmor implements IModelRegister {

	private static final PlayerMPExpander expander = new PlayerMPExpander() {

		private final ResourceLocation ID = new ResourceLocation(LibMod.MOD_NAME, "witch_robes");

		@Override
		public ResourceLocation getID() {
			return ID;
		}

		@Override
		public int getExtraAmount(EntityPlayer p) {
			return 200;
		}
	};

	public ItemWitchesArmor(String id, ArmorMaterial materialIn, int renderIndex, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndex, equipmentSlotIn);
		this.setMaxStackSize(1);
		this.setRegistryName(id);
		this.setTranslationKey(id);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
		if (equipmentSlotIn == EntityEquipmentSlot.HEAD) {
			MinecraftForge.EVENT_BUS.register(this);
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
					prefix = TextFormatting.LIGHT_PURPLE.toString();
					break;
				}
			}
			switch (this.armorType) {
				case CHEST:
					tooltip.add(prefix+I18n.format("item.witches_robes.effect"));
					break;
				case HEAD:
					tooltip.add(prefix+I18n.format("item.witches_hat.effect"));
					break;
				case LEGS:
					tooltip.add(prefix+I18n.format("item.witches_pants.effect"));
					break;
				default:
					break;
			}
			String fsprefix = hasFullSet(Minecraft.getMinecraft().player)?TextFormatting.DARK_PURPLE.toString():TextFormatting.DARK_GRAY.toString();
			tooltip.add(fsprefix+I18n.format("item.witches_set.effect"));

		}
	}

	private static boolean hasFullSet(EntityLivingBase p) {
		if (p != null) {
			Iterator<ItemStack> it = p.getArmorInventoryList().iterator();
			int amount = 0;
			while (it.hasNext()) {
				if (it.next().getItem() instanceof ItemWitchesArmor) {
					amount++;
				}
			}	
			return amount == 3;
		}
		return false;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote) {

			if (itemStack.getItemDamage() > 0 && player.getCapability(MPContainer.CAPABILITY, null).drain(10)) {
				itemStack.setItemDamage(itemStack.getItemDamage() - 1);
			}

			switch (this.armorType) {
				case CHEST:
					removeEffectAndDamageArmor(player, itemStack, MobEffects.POISON);
					break;
				case HEAD:
					removeEffectAndDamageArmor(player, itemStack, MobEffects.BLINDNESS);
					break;
				case LEGS:
					removeEffectAndDamageArmor(player, itemStack, MobEffects.NAUSEA);
					break;
				default:
					break;

			}
		}
	}

	private void removeEffectAndDamageArmor(EntityPlayer player, ItemStack itemStack, Potion potion) {
		PotionEffect pe = player.getActivePotionEffect(potion);
		if (pe != null) {
			player.removePotionEffect(potion);
			itemStack.attemptDamageItem(1, itemRand, (EntityPlayerMP) player);
		}
	}

	@SubscribeEvent
	public void onEquipmentChange(LivingEquipmentChangeEvent event) {
		if (event.getSlot().getSlotType() == Type.ARMOR && event.getEntityLiving() instanceof EntityPlayer && event.getSlot()!=EntityEquipmentSlot.FEET) {
			if (hasFullSet(event.getEntityLiving())) {
				CovensAPI.getAPI().expandPlayerMP(expander, (EntityPlayer) event.getEntityLiving());
			} else {
				CovensAPI.getAPI().removeMPExpansion(expander, (EntityPlayer) event.getEntityLiving());
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		if (itemStack.getItem() instanceof ItemArmor) {
			ModelWitchesArmor.INSTANCE.hat1.showModel = armorSlot == EntityEquipmentSlot.HEAD;
			ModelWitchesArmor.INSTANCE.body.showModel = armorSlot == EntityEquipmentSlot.CHEST;
			ModelWitchesArmor.INSTANCE.armLeft.showModel = armorSlot == EntityEquipmentSlot.CHEST;
			ModelWitchesArmor.INSTANCE.armRight.showModel = armorSlot == EntityEquipmentSlot.CHEST;
			ModelWitchesArmor.INSTANCE.legLeft.showModel = armorSlot == EntityEquipmentSlot.LEGS;
			ModelWitchesArmor.INSTANCE.legRight.showModel = armorSlot == EntityEquipmentSlot.LEGS;

			ModelWitchesArmor.INSTANCE.isChild = _default.isChild;
			ModelWitchesArmor.INSTANCE.isRiding = _default.isRiding;
			ModelWitchesArmor.INSTANCE.isSneak = _default.isSneak;
			ModelWitchesArmor.INSTANCE.rightArmPose = _default.rightArmPose;
			ModelWitchesArmor.INSTANCE.leftArmPose = _default.leftArmPose;

			return ModelWitchesArmor.INSTANCE;
		}
		return null;
	}

}
