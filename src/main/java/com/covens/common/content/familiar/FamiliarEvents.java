package com.covens.common.content.familiar;

import java.util.UUID;

import com.covens.api.CovensAPI;
import com.covens.api.event.HotbarActionCollectionEvent;
import com.covens.api.event.HotbarActionTriggeredEvent;
import com.covens.common.content.actionbar.ModAbilities;
import com.covens.common.content.familiar.ai.AIFollowTarget;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.helper.RayTraceHelper;
import com.covens.common.core.util.EntitySyncHelper;
import com.covens.common.core.util.UUIDs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
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
	
	@SubscribeEvent
	public static void attachFamiliarAI(EntityConstructing evt) {
		//Don't use the capabilities at this point, they are still not attached
		if (evt.getEntity() instanceof EntityLiving && CovensAPI.getAPI().isValidFamiliar(evt.getEntity())) {
			EntityLiving entity = (EntityLiving) evt.getEntity();
			entity.tasks.addTask(2, new AIFollowTarget(entity));
		}
	}
	
	@SubscribeEvent
	public static void addActions(HotbarActionCollectionEvent evt) {
		if (evt.player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).familiarCount > 0) {
			evt.getList().add(ModAbilities.COMMAND_FAMILIAR);
		}
	}
	
	@SubscribeEvent
	public static void handleCommandAbility(HotbarActionTriggeredEvent evt) {
		if (evt.action.equals(ModAbilities.COMMAND_FAMILIAR)) {
			Entity e = evt.focusedEntity;
			if (e != null && CovensAPI.getAPI().isValidFamiliar(e) && e.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).owner.equals(evt.player.getUniqueID())) {
				handleClickOnFamiliar(evt.player, (EntityLiving) e);
			} else {
				RayTraceResult result = RayTraceHelper.rayTraceResult(evt.player, RayTraceHelper.fromLookVec(evt.player, 32), true, true);
				switch (result.typeOfHit) {
					case BLOCK:
						FamiliarController.sendSelectedFamiliarTo(evt.player, result.hitVec);
						break;
					case ENTITY:
						if (result.entityHit instanceof EntityLivingBase) {
							FamiliarController.orderSelectedFamiliarFollow(evt.player, (EntityLivingBase) result.entityHit);
						}
						break;
					case MISS:
						FamiliarController.openFamiliarSelector(evt.player);
						break;
				}
			}
		}
	}

	private static void handleClickOnFamiliar(EntityPlayer player, EntityLiving e) {
		if (player.isSneaking()) {
			FamiliarController.toggleFamiliarWait(e);
		} else {
			UUID sel = player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar;
			if (sel.equals(UUIDs.of(e))) {
				FamiliarController.sendSelectedFamiliarHome(player);
				player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar = UUIDs.NULL_UUID;
			} else {
				player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar = UUIDs.of(e);
			}
		}
	}
}
