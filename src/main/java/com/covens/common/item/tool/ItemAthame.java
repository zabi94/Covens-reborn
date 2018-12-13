package com.covens.common.item.tool;

import com.covens.client.core.IModelRegister;
import com.covens.client.handler.ModelHandler;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.item.ModItems;
import com.covens.common.item.ModMaterials;
import com.covens.common.lib.LibItemName;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * This class was created by BerciTheBeast on 27.3.2017.
 * It's distributed as part of Covens under
 * the MIT license.
 */
// Uses code from Botania

public class ItemAthame extends ItemSword implements IModelRegister {

	public ItemAthame() {
		super(ModMaterials.TOOL_RITUAL);
		setMaxDamage(600);
		this.setMaxStackSize(1);
		setRegistryName(LibItemName.ATHAME);
		setTranslationKey(LibItemName.ATHAME);
		setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
		MinecraftForge.EVENT_BUS.register(this);
	}


	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (!target.world.isRemote)
			if (target instanceof EntityEnderman && attacker instanceof EntityPlayer) {
				target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 20);
				stack.damageItem(50, attacker);
			} else {
				stack.damageItem(1, attacker);
			}
		return true;
	}

	public String getNameInefficiently(ItemStack stack) {
		return getTranslationKey().substring(5);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.YELLOW + I18n.format("witch.tooltip." + getNameInefficiently(stack) + "_description.name"));
	}

	//Todo: Rewrite in it's entirety. Use loot tables.
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void onEntityDrops(LivingDropsEvent event) {
		if (event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
			ItemStack weapon = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (!weapon.isEmpty() && weapon.getItem() == this) {
				Random rand = event.getEntityLiving().world.rand;
				int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, weapon);

				if (event.getEntityLiving() instanceof AbstractSkeleton && rand.nextInt(26) <= 3 + looting)
					addDrop(event, new ItemStack(Items.SKULL, 1, event.getEntityLiving() instanceof EntityWitherSkeleton ? 1 : 0));

				else if (event.getEntityLiving() instanceof EntityZombie && !(event.getEntityLiving() instanceof EntityPigZombie) && rand.nextInt(26) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(Items.SKULL, 1, 2));

				else if (event.getEntityLiving() instanceof EntityCreeper && rand.nextInt(26) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(Items.SKULL, 1, 4));

				else if (event.getEntityLiving() instanceof EntityBat && rand.nextInt(5) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.wool_of_bat, 3));

				else if (event.getEntityLiving() instanceof EntityWolf && rand.nextInt(5) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.tongue_of_dog, 1));

				else if (event.getEntityLiving() instanceof EntityZombie && rand.nextInt(16) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.spectral_dust, 1));

				else if (event.getEntityLiving() instanceof EntitySkeleton && rand.nextInt(16) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.spectral_dust, 1));

				else if (event.getEntityLiving() instanceof EntitySkeletonHorse && rand.nextInt(16) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.spectral_dust, 2));

				else if (event.getEntityLiving() instanceof EntityStray && rand.nextInt(16) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.spectral_dust, 1));

				else if (event.getEntityLiving() instanceof EntityPigZombie && rand.nextInt(16) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.spectral_dust, 3));

				else if (event.getEntityLiving() instanceof EntityHusk && rand.nextInt(16) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.spectral_dust, 1));

				else if (event.getEntityLiving() instanceof EntityWitherSkeleton && rand.nextInt(16) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.spectral_dust, 2));

				else if (event.getEntityLiving() instanceof EntityZombieHorse && rand.nextInt(16) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.spectral_dust, 1));

				else if (event.getEntityLiving() instanceof EntityZombieVillager && rand.nextInt(16) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.spectral_dust, 1));

				else if (event.getEntityLiving() instanceof EntityWither && rand.nextInt(16) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.spectral_dust, 6));

				else if (event.getEntityLiving() instanceof EntitySilverfish && rand.nextInt(16) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.silver_scales, 2));

				else if (event.getEntityLiving() instanceof EntityVillager && rand.nextInt(2) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.heart, 1));

				else if (event.getEntityLiving() instanceof EntityPlayer && rand.nextInt(4) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.heart, 1));

				else if (event.getEntityLiving() instanceof EntitySpider && rand.nextInt(6) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.envenomed_fang, 2));

				else if (event.getEntityLiving() instanceof EntityCaveSpider && rand.nextInt(6) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.envenomed_fang, 2));

				else if (event.getEntityLiving() instanceof EntityEndermite && rand.nextInt(6) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.dimensional_sand, 2));

				else if (event.getEntityLiving() instanceof AbstractHorse && rand.nextInt(4) <= 2 + 2 * looting)
					addDrop(event, new ItemStack(ModItems.equine_tail, 1));

				else if (event.getEntityLiving() instanceof EntityPlayer && rand.nextInt(11) <= 1 + looting) {
					ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString("SkullOwner", event.getEntityLiving().getName());
					stack.setTagCompound(tag);
					addDrop(event, stack);
				}
			}
		}
	}

	private void addDrop(LivingDropsEvent event, ItemStack drop) {
		EntityItem entityitem = new EntityItem(event.getEntityLiving().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, drop);
		entityitem.setPickupDelay(10);
		event.getDrops().add(entityitem);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
