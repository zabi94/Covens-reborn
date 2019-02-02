package com.covens.common.content.familiar;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.covens.api.CovensAPI;
import com.covens.api.event.HotbarActionCollectionEvent;
import com.covens.api.event.HotbarActionTriggeredEvent;
import com.covens.common.content.actionbar.HotbarAction;
import com.covens.common.content.actionbar.ModAbilities;
import com.covens.common.content.familiar.ai.AIFamiliarSit;
import com.covens.common.content.familiar.ai.AIFollowTarget;
import com.covens.common.content.familiar.ai.AIGotoPlace;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.util.syncTasks.FamiliarDeath;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import zabi.minecraft.minerva.common.data.UUIDs;
import zabi.minecraft.minerva.common.utils.entity.EntitySyncHelper;
import zabi.minecraft.minerva.common.utils.entity.RayTraceHelper;

@Mod.EventBusSubscriber
public class FamiliarEvents {
	
	public static final Set<String> bannedTasks = Sets.newHashSet();
	static {
		bannedTasks.add("");
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void familiarDeath(LivingDeathEvent evt) {
		if (evt.getEntity().world.isRemote || !CovensAPI.getAPI().isValidFamiliar(evt.getEntity())) {
			return;
		}
		CapabilityFamiliarCreature cap = evt.getEntity().getCapability(CapabilityFamiliarCreature.CAPABILITY, null);
		if (cap.hasOwner()) {
			EntitySyncHelper.executeOnPlayerAvailable(cap.owner, new FamiliarDeath(evt.getEntity().getPersistentID(), evt.getEntity().getName()));
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
	public static void attachFamiliarAI(EntityJoinWorldEvent evt) {
		if (!evt.getWorld().isRemote && (evt.getEntity() instanceof EntityLiving) && CovensAPI.getAPI().isValidFamiliar(evt.getEntity())) {
			EntityLiving entity = (EntityLiving) evt.getEntity();
			if (entity.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).hasOwner()) {
				removeTasks(entity);
				removeTargets(entity);
				entity.tasks.addTask(2, new AIFollowTarget(entity));
				entity.tasks.addTask(3, new AIGotoPlace(entity));
				entity.tasks.addTask(4, new AIFamiliarSit(entity));
//				entity.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(entity));
//				entity.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(entity));
			}
		}
	}

	private static void removeTasks(EntityLiving elb) {
		Iterator<EntityAITasks.EntityAITaskEntry> iterator = elb.tasks.taskEntries.iterator();
		while (iterator.hasNext()) {
			EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry = iterator.next();
			EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;
			if (bannedTasks.contains(entityaibase.getClass().getName())) {
				if (entityaitasks$entityaitaskentry.using) {
					entityaitasks$entityaitaskentry.using = false;
					entityaitasks$entityaitaskentry.action.resetTask();
					elb.tasks.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
				}
				iterator.remove();
				return;
			}
		}
	}

	private static void removeTargets(EntityLiving elb) {
		Iterator<EntityAITasks.EntityAITaskEntry> iterator = elb.targetTasks.taskEntries.iterator();
		while (iterator.hasNext()) {
			EntityAITasks.EntityAITaskEntry entry = iterator.next();
			if (entry.using) {
				entry.using = false;
				entry.action.resetTask();
				elb.targetTasks.executingTaskEntries.remove(entry);
			}
			iterator.remove();
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
			if ((e != null) && CovensAPI.getAPI().isValidFamiliar(e) && e.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).owner.equals(evt.player.getUniqueID())) {
				handleClickOnFamiliar(evt.player, (EntityLiving) e);
			} else {
				RayTraceResult result = RayTraceHelper.rayTracePlayerSight(evt.player, 32, true);
				if (result == null) {
					result = new RayTraceResult(Type.MISS, new Vec3d(0, 0, 0), null, null);
				}
				switch (result.typeOfHit) {
					case BLOCK:
						FamiliarController.sendSelectedFamiliarTo(evt.player, result.hitVec);
						break;
					case ENTITY:
						if (result.entityHit instanceof EntityLivingBase) {
							FamiliarController.orderSelectedFamiliarFollow(evt.player, (EntityLivingBase) result.entityHit);
						} else {
						}
						break;
					case MISS:
						FamiliarController.openFamiliarSelector(evt.player);
						break;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent evt) {
		if (evt.player instanceof EntityPlayerMP) {
			evt.player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).markDirty((byte) 2);
			HotbarAction.refreshActions(evt.player, evt.player.world);
		}
	}

	private static void handleClickOnFamiliar(EntityPlayer player, EntityLiving e) {
		if (player.isSneaking()) {
			FamiliarController.toggleFamiliarWait(e);
		} else {
			UUID sel = player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar;
			if (UUIDs.of(e).equals(sel)) {
				FamiliarController.sendSelectedFamiliarHome(player);
				player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectFamiliar(null);
			} else {
				player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectFamiliar(e);
			}
		}
	}
}
