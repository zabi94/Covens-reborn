package com.covens.common.item.equipment.baubles;

import com.covens.api.mp.MPContainer;
import com.covens.common.item.ItemMod;
import com.covens.common.lib.LibItemName;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * Created by Joseph on 1/1/2018.
 */
public class ItemRemedyTalisman extends ItemMod implements IBauble {

	public ItemRemedyTalisman() {
		super(LibItemName.REMEDY_TALISMAN);
		this.setMaxStackSize(1);
		this.setMaxDamage(8);
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
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		// Check the condition that is easier to fail first, so it's more performant
		if ((player instanceof EntityPlayer) && ((player.ticksExisted % 40) == 0)) {
			EntityPlayer p = (EntityPlayer) player;
			boolean flag = (p.getActivePotionEffect(MobEffects.POISON) != null) || (p.getActivePotionEffect(MobEffects.NAUSEA) != null) || (p.getActivePotionEffect(MobEffects.WITHER) != null) || (p.getActivePotionEffect(MobEffects.BLINDNESS) != null) || (p.getActivePotionEffect(MobEffects.WEAKNESS) != null);
			// by putting "flag" first (in the AND operation) we don't drain MP if there was
			// nothing to cure
			// due to java lazy evaluation
			if (flag && player.getCapability(MPContainer.CAPABILITY, null).drain(50)) {
				p.removePotionEffect(MobEffects.NAUSEA);
				p.removePotionEffect(MobEffects.WITHER);
				p.removePotionEffect(MobEffects.BLINDNESS);
				p.removePotionEffect(MobEffects.POISON);
				p.removePotionEffect(MobEffects.WEAKNESS);
				itemstack.damageItem(1, player); // this also takes care of the breaking
			}
		}
	}

	@Override
	public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, .75F, 1.9f);
	}
}
