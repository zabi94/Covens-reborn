package com.covens.common.potion.potions;

import com.covens.api.CovensAPI;
import com.covens.api.transformation.DefaultTransformations;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.core.helper.MobHelper;
import com.covens.common.potion.PotionMod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionVampireLeech extends PotionMod {
	
	public PotionVampireLeech() {
		super("vampire_leech", false, 0x830f0f, false);
		this.setIconIndex(6, 2);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return false;
	}
	
	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase e, AbstractAttributeMap attributeMapIn, int amplifier) {
		if (!(e instanceof EntityPlayer) || !e.getCapability(CapabilityTransformation.CAPABILITY, null).getType().equals(DefaultTransformations.VAMPIRE)) {
			e.removeActivePotionEffect(this);
		}
	}
	
	@Override
	public void performEffect(EntityLivingBase e, int amplifier) {
		//NO-OP
	}
	
	@SubscribeEvent
	public void onPlayerHitEntity(LivingHurtEvent evt) {
		if (MobHelper.isLivingCorporeal(evt.getEntityLiving()) && isValidDamage(evt.getSource())) {
			CovensAPI.getAPI().drainBloodFromEntity((EntityPlayer) evt.getSource().getTrueSource(), evt.getEntityLiving());
		}
	}
	
	private static boolean isValidDamage(DamageSource src) {
		return !src.isProjectile() && src.getTrueSource() instanceof EntityLivingBase;
	}

}
