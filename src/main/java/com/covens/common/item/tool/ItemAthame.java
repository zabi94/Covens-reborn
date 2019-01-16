package com.covens.common.item.tool;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.covens.common.core.helper.MobHelper;
import com.covens.common.core.statics.ModCreativeTabs;
import com.covens.common.item.ModItems;
import com.covens.common.item.ModMaterials;
import com.covens.common.lib.LibItemName;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityWolf;
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
import zabi.minecraft.minerva.client.blockmodels.IModelRegister;
import zabi.minecraft.minerva.client.blockmodels.ModelHandler;

/**
 * This class was created by BerciTheBeast on 27.3.2017. It's distributed as
 * part of Covens under the MIT license.
 */

public class ItemAthame extends ItemSword implements IModelRegister {

	public ItemAthame() {
		super(ModMaterials.TOOL_ATHAME);
		this.setMaxDamage(600);
		this.setMaxStackSize(1);
		this.setRegistryName(LibItemName.ATHAME);
		this.setTranslationKey(LibItemName.ATHAME);
		this.setCreativeTab(ModCreativeTabs.ITEMS_CREATIVE_TAB);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (!target.world.isRemote) {
			if ((target instanceof EntityEnderman) && (attacker instanceof EntityPlayer)) {
				target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 20);
				stack.damageItem(5, attacker);
			} else {
				stack.damageItem(1, attacker);
			}
		}
		return true;
	}

	public String getNameInefficiently(ItemStack stack) {
		return this.getTranslationKey().substring(5);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.YELLOW + I18n.format("witch.tooltip." + this.getNameInefficiently(stack) + "_description.name"));
	}

	@SubscribeEvent
	public void onEntityDrops(LivingDropsEvent event) {
		if (event.isRecentlyHit() && (event.getSource().getTrueSource() != null) && (event.getSource().getTrueSource() instanceof EntityPlayer)) {
			ItemStack weapon = ((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (!weapon.isEmpty() && (weapon.getItem() == this)) {
				Random rand = event.getEntityLiving().world.rand;
				int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, weapon);

				if ((event.getEntityLiving() instanceof AbstractSkeleton) && (rand.nextInt(26) <= (3 + looting))) {
					this.addDrop(event, new ItemStack(Items.SKULL, 1, event.getEntityLiving() instanceof EntityWitherSkeleton ? 1 : 0));
				} else if ((event.getEntityLiving() instanceof EntityZombie) && !(event.getEntityLiving() instanceof EntityPigZombie) && (rand.nextInt(26) <= (2 + (2 * looting)))) {
					this.addDrop(event, new ItemStack(Items.SKULL, 1, 2));
				} else if ((event.getEntityLiving() instanceof EntityCreeper) && (rand.nextInt(26) <= (2 + (2 * looting)))) {
					this.addDrop(event, new ItemStack(Items.SKULL, 1, 4));
				} else if ((event.getEntityLiving() instanceof EntityBat) && (rand.nextInt(5) <= (2 + (2 * looting)))) {
					this.addDrop(event, new ItemStack(ModItems.wool_of_bat, 3));
				} else if ((event.getEntityLiving() instanceof EntityWolf) && (rand.nextInt(5) <= (2 + (2 * looting)))) {
					this.addDrop(event, new ItemStack(ModItems.tongue_of_dog, 1));
				} else if ((event.getEntityLiving().getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) && (rand.nextInt(16) <= (2 + (2 * looting)))) {
					this.addDrop(event, new ItemStack(ModItems.spectral_dust, 1));
				} else if ((event.getEntityLiving() instanceof EntitySilverfish) && (rand.nextInt(16) <= (2 + (2 * looting)))) {
					this.addDrop(event, new ItemStack(ModItems.silver_scales, 2));
				} else if (MobHelper.isHumanoid(event.getEntityLiving()) && (rand.nextInt(2) <= (2 + (2 * looting)))) {
					this.addDrop(event, new ItemStack(ModItems.heart, 1));
				} else if ((event.getEntityLiving() instanceof EntitySpider) && (rand.nextInt(6) <= (2 + (2 * looting)))) {
					this.addDrop(event, new ItemStack(ModItems.envenomed_fang, 2));
				} else if ((event.getEntityLiving() instanceof EntityCaveSpider) && (rand.nextInt(6) <= (2 + (2 * looting)))) {
					this.addDrop(event, new ItemStack(ModItems.envenomed_fang, 2));
				} else if ((event.getEntityLiving() instanceof EntityEndermite) && (rand.nextInt(6) <= (2 + (2 * looting)))) {
					this.addDrop(event, new ItemStack(ModItems.dimensional_sand, 2));
				} else if ((event.getEntityLiving() instanceof AbstractHorse) && (rand.nextInt(4) <= (2 + (2 * looting)))) {
					this.addDrop(event, new ItemStack(ModItems.equine_tail, 1));
				} else if ((event.getEntityLiving() instanceof EntityPlayer) && (rand.nextInt(11) <= (1 + looting))) {
					ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString("SkullOwner", event.getEntityLiving().getName());
					stack.setTagCompound(tag);
					this.addDrop(event, stack);
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
