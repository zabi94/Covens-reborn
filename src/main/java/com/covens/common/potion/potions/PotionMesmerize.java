package com.covens.common.potion.potions;

import java.util.Set;

import com.covens.common.core.util.ReflectionBridge;
import com.covens.common.potion.PotionMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

public class PotionMesmerize extends PotionMod {

	public PotionMesmerize() {
		super("mesmerized", true, 0x7e03d4, false);
		this.setIconIndex(0, 2);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void performEffect(EntityLivingBase e, int amplifier) {
		if (e instanceof EntityLiving) {
			Entity target = e.getRevengeTarget();
			if (target instanceof EntityPlayer) {
				PotionEffect eff = ((EntityPlayer) target).getActivePotionEffect(this);
				if (eff != null) {
					e.setRevengeTarget(null);
					((EntityLiving) e).setAttackTarget(null);
					e.getCombatTracker().reset();
				}
			}
			EntityLiving el = (EntityLiving) e;
			EntityAITaskEntry taskFound = null;
			Set<EntityAITasks.EntityAITaskEntry> executingTaskEntries = ReflectionBridge.getExecutingTaskEntries(el.tasks);
			for (EntityAITaskEntry task:executingTaskEntries) {
				if (task.action instanceof EntityAIPanic) {
					el.getNavigator().clearPath();
					taskFound = task;
					break;
				}
			}
			if (taskFound != null) {
				taskFound.using = false;
				taskFound.action.resetTask();
				executingTaskEntries.remove(taskFound);
			}
		}
	}

}
