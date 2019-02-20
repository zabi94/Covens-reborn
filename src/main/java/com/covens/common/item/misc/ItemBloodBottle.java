package com.covens.common.item.misc;

import java.util.List;

import javax.annotation.Nullable;

import com.covens.api.CovensAPI;
import com.covens.client.core.ModelResourceLocations;
import com.covens.common.core.helper.NBTHelper;
import com.covens.common.item.ItemMod;
import com.covens.common.item.ModItems;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBloodBottle extends ItemMod {
	
	public static final int EXPIRATION_TIME = 60 * 20 * 7; //5 minutes
	public static final String EXPIRATION_TAG = "expiration";

	public ItemBloodBottle(String id) {
		super(id);
		this.setMaxStackSize(1);
		this.setContainerItem(Items.GLASS_BOTTLE);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if (NBTHelper.fixNBT(stack).hasKey(EXPIRATION_TAG)) {
			long created = stack.getTagCompound().getLong(EXPIRATION_TAG);
			long current = getCurrentUnversalTime(DimensionManager.getWorld(0));
			long span = current - created;
			if (span < EXPIRATION_TIME && current > created) {
				double amount = 100d * span / EXPIRATION_TIME;
				if (amount < 10) {
					tooltip.add(I18n.format("item.blood_bottle.fresh"));
				} else if (amount < 40) {
					tooltip.add(I18n.format("item.blood_bottle.normal"));
				} else if (amount < 70) {
					tooltip.add(I18n.format("item.blood_bottle.old"));
				} else {
					tooltip.add(I18n.format("item.blood_bottle.stale"));
				}
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		EnumActionResult validation = validateExpiration(stack, worldIn);
		if (playerIn.isCreative()) {
			validation = EnumActionResult.SUCCESS;
		}
		switch (validation) {
			case SUCCESS: //VALID
				playerIn.setActiveHand(handIn);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			case FAIL: //NO EXPIRATION
			default:
			case PASS: //EXPIRED
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
		}
		
	}
	
	@Override
	public String getTranslationKey(ItemStack stack) {
		EnumActionResult val = validateExpiration(stack, DimensionManager.getWorld(0));
		if (val != EnumActionResult.SUCCESS) {
			return super.getTranslationKey()+"_spoiled";
		}
		return super.getTranslationKey(stack);
	}
	
	public static ItemStack getNewStack(World world) {
		ItemStack result = new ItemStack(ModItems.blood_bottle);
		NBTHelper.setLong(result, EXPIRATION_TAG, getCurrentUnversalTime(world));
		return result;
	}
	
	private static EnumActionResult validateExpiration(ItemStack stack, World world) {
		if (NBTHelper.fixNBT(stack).hasKey(EXPIRATION_TAG)) {
			long created = stack.getTagCompound().getLong(EXPIRATION_TAG);
			long current = getCurrentUnversalTime(world);
			if (current - created < EXPIRATION_TIME && current > created) {
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.PASS;
		}
		return EnumActionResult.FAIL;
	}
	
	private static long getCurrentUnversalTime(World world) {
		return DimensionManager.getWorld(0).getTotalWorldTime();
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 60;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		EntityPlayer p = (EntityPlayer) entityLiving;
		if (CovensAPI.getAPI().addVampireBlood(p, 120)) {
			if (p.isCreative()) {
				return stack;
			}
			ItemStack result = new ItemStack(Items.GLASS_BOTTLE);
			if (!worldIn.isRemote && !p.addItemStackToInventory(result)) {
				p.dropItem(result, false);
			}
			return ItemStack.EMPTY;
		} else {
			return stack;
		}
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelBakery.registerItemVariants(this, ModelResourceLocations.BLOOD_BOTTLE, ModelResourceLocations.BLOOD_BOTTLE_SPOILED);
		ModelLoader.setCustomMeshDefinition(this, ItemBloodBottle::getModel);
	}
	
	@SideOnly(Side.CLIENT)
	public static ModelResourceLocation getModel(ItemStack stack) {
		if (validateExpiration(stack, DimensionManager.getWorld(0)) == EnumActionResult.SUCCESS) {
			return ModelResourceLocations.BLOOD_BOTTLE;
		}
		return ModelResourceLocations.BLOOD_BOTTLE_SPOILED;
	}
}
