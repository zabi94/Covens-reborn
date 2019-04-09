package com.covens.common.content.enchantments;

import com.covens.api.mp.MPContainer;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentPotentWard extends BaublesEnchantment {

	protected EnchantmentPotentWard() {
		super("potent_ward", Rarity.VERY_RARE, 3);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onDamage(LivingHurtEvent evt) {
		if (evt.getAmount() >= evt.getEntityLiving().getHealth()*0.9f && evt.getSource().isMagicDamage() && (evt.getEntityLiving() instanceof EntityPlayer)) {
			EntityPlayer p = (EntityPlayer) evt.getEntityLiving();
			MPContainer mpc = p.getCapability(MPContainer.CAPABILITY, null);
			int maxLevel = this.getMaxLevelOnPlayer(p);
			if (maxLevel > 0 && mpc.drain(600/maxLevel)) {
				evt.setCanceled(true);
			}
		}
	}

	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof EnchantmentDesperateWard) && super.canApplyTogether(ench);
	}
}
