package com.covens.common.item.equipment.baubles;

import org.lwjgl.opengl.GL11;

import com.covens.client.render.entity.model.ModelGirdleOfTheWooded;
import com.covens.client.render.entity.model.ModelGirdleOfTheWoodedArmor;
import com.covens.common.core.capability.simple.BarkCapability;
import com.covens.common.item.ItemMod;
import com.covens.common.potion.ModPotions;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.render.IRenderBauble;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGirdleOfTheWooded extends ItemMod implements IBauble, IRenderBauble {

	private static final BaubleType BAUBTYPE = BaubleType.BELT;

	@SideOnly(Side.CLIENT)
	private static ModelGirdleOfTheWooded model;
	@SideOnly(Side.CLIENT)
	private static ModelGirdleOfTheWooded model_with_armor;

	public ItemGirdleOfTheWooded(String id) {
		super(id);
		this.setMaxStackSize(1);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static boolean buildBark(EntityPlayer player) {
		BarkCapability bark = player.getCapability(BarkCapability.CAPABILITY, null);
		int oldAmount = bark.pieces;
		bark.pieces++;
		fixBark(player);
		if (oldAmount < bark.pieces) {
			bark.markDirty((byte) 1);
			return true;
		}
		return false;
	}

	public static int getBarkPieces(EntityPlayer player) {
		return player.getCapability(BarkCapability.CAPABILITY, null).pieces;
	}

	public static void fixBark(EntityPlayer player, int totalArmorValue) {
		if (totalArmorValue == 0) {
			player.getCapability(BarkCapability.CAPABILITY, null).pieces = 0;
		} else {
			int value = player.getCapability(BarkCapability.CAPABILITY, null).pieces;
			int possible = Math.min(Math.max((10 - totalArmorValue), 0), 3);
			if (value > possible) {
				value = possible;
			}
			if (value < 0) {
				value = 0;
			}
			player.getCapability(BarkCapability.CAPABILITY, null).pieces = value;
		}
	}
	
	public static void fixBark(EntityPlayer player) {
		fixBark(player, (int) Math.ceil(ForgeHooks.getTotalArmorValue(player) / 2f));
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BAUBTYPE;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
		if (!entity.world.isRemote) {
			if (!(entity instanceof EntityPlayer)) {
				return;
			}
			EntityPlayer player = (EntityPlayer) entity;
			if (((entity.getRNG().nextDouble() < 0.0008d) && this.isValidSpot(player))) { // ~once every minute
				if (buildBark(player)) {
					player.addPotionEffect(new PotionEffect(ModPotions.rooting, 80, 0, false, false));
					player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_CHORUS_FLOWER_GROW, SoundCategory.PLAYERS, 1f, 1f);
				}
			}

		}
	}

	private boolean isValidSpot(EntityPlayer player) {
		return player.world.getBlockState(player.getPosition().down()).getBlock() == Blocks.GRASS;
	}

	private void destroyBark(EntityPlayer player) {
		player.getCapability(BarkCapability.CAPABILITY, null).pieces -= 1;
		fixBark(player);
		player.getCapability(BarkCapability.CAPABILITY, null).markDirty((byte) 1);
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
	public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		IBauble.super.onEquipped(itemstack, player);
		player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.15F, 1.9f);
		player.getCapability(BarkCapability.CAPABILITY, null).pieces = ((EntityPlayer) player).isCreative() ? 10 : 0;
		fixBark((EntityPlayer) player);
		player.getCapability(BarkCapability.CAPABILITY, null).markDirty((byte) 1);
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		player.getCapability(BarkCapability.CAPABILITY, null).pieces = 0;
		player.getCapability(BarkCapability.CAPABILITY, null).markDirty((byte) 1);
	}

	@SubscribeEvent
	public void onPlayerDamaged(LivingHurtEvent evt) {
		if (!evt.getEntityLiving().world.isRemote && (evt.getAmount() > 1.5f) && (evt.getSource().getTrueSource() != null) && (evt.getEntityLiving() instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
			fixBark(player);
			if (player.getCapability(BarkCapability.CAPABILITY, null).pieces > 0) {
				this.destroyBark(player);
				evt.setCanceled(true);
			}
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return (enchantment == Enchantments.BINDING_CURSE) || enchantment.type.canEnchantItem(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if (type == RenderType.BODY) {
			if (model == null) {
				model = new ModelGirdleOfTheWooded();
				model_with_armor = new ModelGirdleOfTheWoodedArmor();
			}
			GL11.glPushMatrix();
			IRenderBauble.Helper.rotateIfSneaking(player);
			GL11.glRotated(180, 1, 0, 0);
			GL11.glTranslated(0, 0, 0.02);
			GL11.glScaled(0.12, 0.12, 0.12);
			IRenderBauble.Helper.translateToChest();
			IRenderBauble.Helper.defaultTransforms();
			if (player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()) {
				model.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYaw, player.rotationPitch, 1);
			} else {
				model_with_armor.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYaw, player.rotationPitch, 1);
			}
			GL11.glPopMatrix();
		}
	}

	@SubscribeEvent
	public void onEquipmentChanged(LivingEquipmentChangeEvent evt) {
		if (!evt.getEntityLiving().world.isRemote && (evt.getEntityLiving() instanceof EntityPlayer)) {
			fixBark((EntityPlayer) evt.getEntityLiving(), getTotalArmorValue(evt));
			evt.getEntityLiving().getCapability(BarkCapability.CAPABILITY, null).markDirty((byte) 1);
		}
	}

	//This is forge being forge. The event is fired too soon, so the armor values are not updated yet at this point.
	//I need to update them manually, then revert them back to let forge do the actual change
	public static int getTotalArmorValue(LivingEquipmentChangeEvent evt) {
		EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
		
		if (!evt.getFrom().isEmpty()) {
            player.getAttributeMap().removeAttributeModifiers(evt.getFrom().getAttributeModifiers(evt.getSlot()));
        }

        if (!evt.getTo().isEmpty()) {
        	player.getAttributeMap().applyAttributeModifiers(evt.getTo().getAttributeModifiers(evt.getSlot()));
        }
		
		int ret = player.getTotalArmorValue();
		for (int x = 0; x < player.inventory.armorInventory.size(); x++) {
			ItemStack stack = player.inventory.armorInventory.get(x);
			if (stack.isItemEqual(evt.getFrom())) {
				stack = evt.getTo();
			}
			if (stack.getItem() instanceof ISpecialArmor) {
				ret += ((ISpecialArmor) stack.getItem()).getArmorDisplay(player, stack, x);
			}
		}
		
		if (!evt.getFrom().isEmpty()) {
            player.getAttributeMap().applyAttributeModifiers(evt.getFrom().getAttributeModifiers(evt.getSlot()));
        }

        if (!evt.getTo().isEmpty()) {
        	player.getAttributeMap().removeAttributeModifiers(evt.getTo().getAttributeModifiers(evt.getSlot()));
        }
		return ret;
	}

}
