package com.covens.common.item.equipment.baubles;

import java.util.List;

import javax.annotation.Nullable;

import com.covens.api.CovensAPI;
import com.covens.api.mp.MPContainer;
import com.covens.api.mp.PlayerMPExpander;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.item.ItemMod;
import com.covens.common.lib.LibItemName;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Joseph on 1/1/2018.
 */
public class ItemHellishBauble extends ItemMod implements IBauble, PlayerMPExpander {
	public ItemHellishBauble() {
		super(LibItemName.HELLISH_BAUBLE);
		this.setMaxStackSize(1);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private static boolean isDemon(Entity e) {
		return ((e instanceof EntityLivingBase) && (((EntityLivingBase) e).getCreatureAttribute() == CovensAPI.getAPI().DEMON));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				if (baubles.getStackInSlot(i).isEmpty() && baubles.isItemValidForSlot(i, player.getHeldItem(hand), player)) {
					baubles.setStackInSlot(i, player.getHeldItem(hand).copy());
					if (!player.capabilities.isCreativeMode) {
						player.setHeldItem(hand, ItemStack.EMPTY);
					}
					this.onEquipped(player.getHeldItem(hand), player);
					break;
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return BaubleType.TRINKET;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment == Enchantments.BINDING_CURSE;
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		IBauble.super.onEquipped(itemstack, player);
		player.world.playSound((EntityPlayer) player, player.getPosition(), SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.AMBIENT, 0.75F, 1.9f);
		CovensAPI.getAPI().expandPlayerMP(this, (EntityPlayer) player);
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		IBauble.super.onUnequipped(itemstack, player);
		if (BaublesApi.isBaubleEquipped((EntityPlayer) player, this) < 0) {
			CovensAPI.getAPI().removeMPExpansion(this, (EntityPlayer) player);
		}
	}

	public String getNameInefficiently(ItemStack stack) {
		return this.getTranslationKey().substring(5);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.DARK_RED + I18n.format("witch.tooltip." + this.getNameInefficiently(stack) + "_description.name"));
	}

	@SubscribeEvent
	public void onEntityDamage(LivingHurtEvent event) {
		if (this.hasAmulet(event.getEntityLiving()) && this.isValidDamageType(event.getSource()) && this.hasEnergy(event.getEntityLiving())) {
			event.setAmount(event.getAmount() * 0.80F);
		}
	}

	private boolean hasAmulet(EntityLivingBase entity) {
		return (entity instanceof EntityPlayer) && (BaublesApi.isBaubleEquipped((EntityPlayer) entity, this) >= 0);
	}

	private boolean isValidDamageType(DamageSource src) {
		return src.isFireDamage() || src.isExplosion() || isDemon(src.getTrueSource());
	}

	private boolean hasEnergy(EntityLivingBase entity) {
		return entity.getCapability(MPContainer.CAPABILITY, null).drain(50);
	}

	@Override
	public ResourceLocation getID() {
		return this.getRegistryName();
	}

	@Override
	public int getExtraAmount(EntityPlayer p) {
		return 100;
	}
}
