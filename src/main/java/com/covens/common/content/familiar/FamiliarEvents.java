package com.covens.common.content.familiar;

import com.covens.api.CovensAPI;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.util.EntitySyncHelper;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class FamiliarEvents {
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void familiarDeath(LivingDeathEvent evt) {
		if (!CovensAPI.getAPI().isValidFamiliar(evt.getEntity())) {
			return;
		}
		if (evt.getEntity().getCapability(CapabilityFamiliarCreature.CAPABILITY, null).hasOwner()) {
			CovensAPI.getAPI().unbindFamiliar(evt.getEntity());
		}
		EntitySyncHelper.cleanMessagesForEntity(evt.getEntity().getPersistentID());
	}
	
	@SubscribeEvent
	public static void stopDespawn(LivingSpawnEvent.AllowDespawn evt) {
		if (!CovensAPI.getAPI().isValidFamiliar(evt.getEntity())) {
			return;
		}
		if (evt.getEntity().getCapability(CapabilityFamiliarCreature.CAPABILITY, null).hasOwner()) {
			evt.setResult(Result.DENY);
		}
	}
	
}
