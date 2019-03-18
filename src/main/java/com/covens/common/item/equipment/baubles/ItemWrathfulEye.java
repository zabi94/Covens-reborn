package com.covens.common.item.equipment.baubles;

import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.item.ItemMod;
import com.covens.common.lib.LibItemName;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemWrathfulEye extends ItemMod implements IBauble {

	public ItemWrathfulEye() {
		super(LibItemName.WRATHFUL_EYE);
		this.setMaxStackSize(1);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
	}

	@Override
	public boolean isDamageable() {
		return true;
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
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment == Enchantments.BINDING_CURSE || enchantment.type.canEnchantItem(this);
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.PLAYERS, .75F, 1.9f);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return BaubleType.AMULET;
	}

//	private boolean doesPlayerHaveAmulet(EntityPlayer e) {
//		return BaublesApi.isBaubleEquipped(e, this) > 0;
//	}

}
