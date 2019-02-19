package com.covens.common.content.cauldron.brews;

import java.util.UUID;

import com.covens.common.content.cauldron.BrewMod;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.minerva.common.utils.AttributeModifierModeHelper;

public class PotionRooting extends BrewMod {
	
	private static final UUID SPEED_ID = UUID.fromString("0d9985ba-0297-4159-83d7-7a5fd7200489");
	
	public PotionRooting() {
		super("rooting", true, 0x6f4a02, false, 200, 90);
		this.setIconIndex(5, 2);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void performEffect(EntityLivingBase elb, int amplifier) {
		if (!elb.onGround || !entityOnGrass(elb)) {
			elb.removePotionEffect(this);
		} else if (!elb.world.isRemote && elb instanceof EntityPlayer && elb.getRNG().nextInt(200) == 0 && elb.world.isRainingAt(elb.getPosition())) {
			((EntityPlayer) elb).getFoodStats().addStats(1, 0);
		}
	}
	
	@SubscribeEvent
	public void onJump(LivingJumpEvent evt) {
		if (evt.getEntityLiving().getActivePotionEffect(this) != null) {
			evt.getEntityLiving().motionY = -10;
		}
	}
	
	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase elb, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.applyAttributesModifiersToEntity(elb, attributeMapIn, amplifier);
		if (elb.onGround && entityOnGrass(elb)) {
			IAttributeInstance iai = elb.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED);
			iai.removeModifier(SPEED_ID);
			iai.applyModifier(new AttributeModifier(SPEED_ID, "rooting_speed_mod", -1, AttributeModifierModeHelper.PERCENT));
		}
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase elb, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(elb, attributeMapIn, amplifier);
		IAttributeInstance iai = elb.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED);
		iai.removeModifier(SPEED_ID);
	}

	private boolean entityOnGrass(EntityLivingBase elb) {
		Material m = elb.world.getBlockState(elb.getPosition().down()).getMaterial();
		return m.equals(Material.GROUND) || m.equals(Material.GRASS);
	}
	
	@Override
	public int getArrowDuration() {
		return 40;
	}
}
