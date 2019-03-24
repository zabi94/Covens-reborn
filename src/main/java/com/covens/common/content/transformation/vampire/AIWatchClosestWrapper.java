package com.covens.common.content.transformation.vampire;

import java.lang.reflect.Field;

import com.covens.api.transformation.DefaultTransformations;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.item.ModItems;
import com.covens.common.lib.LibReflection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumSkyBlock;

public class AIWatchClosestWrapper extends EntityAIBase {

	private static final Field entityClosest = LibReflection.field("entityClosest", "field_75334_a", EntityAIWatchClosest.class);
	private static final Field entitySubject = LibReflection.field("entity", "field_75332_b", EntityAIWatchClosest.class);
	private EntityAIWatchClosest wrapped;

	public AIWatchClosestWrapper(EntityAIWatchClosest action) {
		this.wrapped = action;
		this.setMutexBits(action.getMutexBits());
	}

	@Override
	public boolean shouldExecute() {
		try {
			return this.wrapped.shouldExecute() && this.canLookAt((EntityLiving) entitySubject.get(this.wrapped), (Entity) entityClosest.get(this.wrapped));
		} catch (Exception e) {
			throw new IllegalStateException("Reflection failed", e);
		}
	}

	private boolean canLookAt(EntityLiving subject, Entity target) {
		return !isVampireWithPants(target) && !(target.isSneaking() && ((Math.abs(target.getRotationYawHead() - subject.rotationYawHead) < 70) || (target.world.getLightFor(EnumSkyBlock.BLOCK, target.getPosition()) < 3)));
	}

	private boolean isVampireWithPants(Entity target) {
		if (target instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) target;
			if (p.getCapability(CapabilityTransformation.CAPABILITY, null).getType() == DefaultTransformations.VAMPIRE) {
				return p.inventory.armorItemInSlot(1).getItem() == ModItems.vampire_pants;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.wrapped.shouldContinueExecuting();
	}

	@Override
	public boolean isInterruptible() {
		return this.wrapped.isInterruptible();
	}

	@Override
	public void resetTask() {
		this.wrapped.resetTask();
	}

	@Override
	public void updateTask() {
		this.wrapped.updateTask();
	}

	@Override
	public void startExecuting() {
		this.wrapped.startExecuting();
	}
}
