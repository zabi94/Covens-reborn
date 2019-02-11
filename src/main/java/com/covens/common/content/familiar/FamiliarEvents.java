package com.covens.common.content.familiar;

import com.covens.api.CovensAPI;
import com.covens.api.event.HotbarActionCollectionEvent;
import com.covens.api.event.HotbarActionTriggeredEvent;
import com.covens.common.content.actionbar.HotbarAction;
import com.covens.common.content.actionbar.ModAbilities;
import com.covens.common.content.familiar.FamiliarController.FamiliarCommand;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.util.syncTasks.FamiliarDeath;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import zabi.minecraft.minerva.common.utils.entity.EntitySyncHelper;

@Mod.EventBusSubscriber
public class FamiliarEvents {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void familiarDeath(LivingDeathEvent evt) {
		if (evt.getEntity().world.isRemote || !CovensAPI.getAPI().isValidFamiliar(evt.getEntity())) {
			return;
		}
		CapabilityFamiliarCreature cap = evt.getEntity().getCapability(CapabilityFamiliarCreature.CAPABILITY, null);
		if (cap.hasOwner()) {
			EntitySyncHelper.executeOnPlayerAvailable(cap.owner, new FamiliarDeath(evt.getEntity().getPersistentID(), evt.getEntity().getName()));
			CovensAPI.getAPI().unbindFamiliar((EntityLiving) evt.getEntity());
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
				FamiliarController.setupFamiliarAI(entity);
			}
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
			EntityPlayer p = evt.player;
			Tuple<FamiliarCommand, RayTraceResult> res = FamiliarController.getExecutedCommand(evt.player, e);
			switch (res.getFirst()) {
				case ATTACK:
					// TODO
					break;
				case DESELECT:
					p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectFamiliar(null);
					break;
				case FOLLOW_ME:
					// TODO FamiliarController.toggleFamiliarWait(e);
					break;
				case FOLLOW_TARGET:
					FamiliarController.orderSelectedFamiliarFollow(evt.player, (EntityLivingBase) (e==null?res.getSecond().entityHit:e));
					break;
				case FREE:
					// TODO FamiliarController.toggleFamiliarWait(e);
					break;
				case GOTO:
					FamiliarController.sendSelectedFamiliarTo(evt.player, res.getSecond().hitVec);
					break;
				case GUARD:
					//TODO
					break;
				case NO_COMMAND:
					// NO-OP
					break;
				case OPEN_GUI:
					//TODO
					break;
				case SELECT:
					p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectFamiliar(e);
					break;
				case STAY:
					// TODO FamiliarController.toggleFamiliarWait(e);
					break;
				default:
					//NO OP
					break;
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
}
