package com.covens.common.content.familiar;

import java.util.List;

import com.covens.api.CovensAPI;
import com.covens.api.event.HotbarActionCollectionEvent;
import com.covens.api.event.HotbarActionTriggeredEvent;
import com.covens.common.content.actionbar.HotbarAction;
import com.covens.common.content.actionbar.ModAbilities;
import com.covens.common.content.familiar.FamiliarController.FamiliarCommand;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.net.NetworkHandler;
import com.covens.common.core.net.messages.PlayerFamiliarsDefinition;
import com.covens.common.core.util.syncTasks.FamiliarDeath;
import com.covens.common.core.util.syncTasks.FamiliarPingPosition;
import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import zabi.minecraft.minerva.common.entity.EntityHelper;
import zabi.minecraft.minerva.common.entity.UUIDs;
import zabi.minecraft.minerva.common.entity.synchronization.SyncManager;
import zabi.minecraft.minerva.common.utils.DimensionalPosition;

@Mod.EventBusSubscriber
public class FamiliarEvents {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void familiarDeath(LivingDeathEvent evt) {
		if (evt.getEntity().world.isRemote || !CovensAPI.getAPI().isValidFamiliar(evt.getEntity())) {
			return;
		}
		CapabilityFamiliarCreature cap = evt.getEntity().getCapability(CapabilityFamiliarCreature.CAPABILITY, null);
		if (cap.hasOwner()) {
			SyncManager.executeOnPlayerAvailable(cap.owner, new FamiliarDeath(evt.getEntity().getPersistentID(), evt.getEntity().getName()));
			CovensAPI.getAPI().unbindFamiliar((EntityLiving) evt.getEntity());
		}
		SyncManager.cleanMessagesForEntity(evt.getEntity().getPersistentID()); //TODO move to minerva!
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
			CapabilityFamiliarCreature fam = entity.getCapability(CapabilityFamiliarCreature.CAPABILITY, null); 
			if (fam.hasOwner()) {
				FamiliarController.setupFamiliar(entity);
				SyncManager.executeOnPlayerAvailable(fam.owner, new FamiliarPingPosition(UUIDs.of(entity), new DimensionalPosition(entity)), UUIDs.of(entity));
			}
		}
	}

	@SubscribeEvent
	public static void onFamiliarUnload(ChunkEvent.Unload evt) {
		CovensAPI api = CovensAPI.getAPI();
		for (ClassInheritanceMultiMap<Entity> cimm : evt.getChunk().getEntityLists()) {
			cimm.forEach(e -> {
				if (api.isValidFamiliar(e)) {
					CapabilityFamiliarCreature fam = e.getCapability(CapabilityFamiliarCreature.CAPABILITY, null);
					if (fam.hasOwner()) {
						SyncManager.executeOnPlayerAvailable(fam.owner, new FamiliarPingPosition(UUIDs.of(e), new DimensionalPosition(e)), UUIDs.of(e));
					}
				}
			});
		}
	}
	
	@SubscribeEvent
	public static void onFamiliarChangeDimension(EntityTravelToDimensionEvent evt) {
		CovensAPI api = CovensAPI.getAPI();
		if (api.isValidFamiliar(evt.getEntity())) {
			CapabilityFamiliarCreature fam = evt.getEntity().getCapability(CapabilityFamiliarCreature.CAPABILITY, null);
			if (fam.hasOwner()) {
				SyncManager.executeOnPlayerAvailable(fam.owner, new FamiliarPingPosition(UUIDs.of(evt.getEntity()), new DimensionalPosition(evt.getEntity())), UUIDs.of(evt.getEntity()));
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
	public static void protectFamiliars(LivingHurtEvent evt) {
		if (CovensAPI.getAPI().isValidFamiliar(evt.getEntityLiving()) && evt.getEntityLiving().getCapability(CapabilityFamiliarCreature.CAPABILITY, null).hasOwner()) {
			DamageSource src = evt.getSource();
			if (!src.canHarmInCreative() && !src.isMagicDamage() && !canHurtSpirits(src)) {
				evt.setAmount(0);
				evt.setCanceled(true);
				return;
			}
			if (src.getTrueSource() != null && UUIDs.of(src.getTrueSource()).equals(evt.getEntityLiving().getCapability(CapabilityFamiliarCreature.CAPABILITY, null).owner)) {
				evt.setAmount(0);
				evt.setCanceled(true);
				return;
			}
		}
	}
	
	private static boolean canHurtSpirits(DamageSource src) {
		if (src.getTrueSource() instanceof EntityPlayer && !src.getTrueSource().getCapability(CapabilityTransformation.CAPABILITY, null).getType().canCrossSalt()) {
			return true;
		}
		return false; //TODO
	}
	
	@SubscribeEvent
	public static void stopDrops(LivingDropsEvent evt) {
		if (evt.getEntityLiving().getTags().contains("familiar")) {
			evt.setCanceled(true);
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
					p.sendStatusMessage(new TextComponentTranslation("This is not implemented yet, sorry!"), true);
					break;
				case DESELECT:
					p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).deselectFamiliar();
					break;
				case FOLLOW_ME:
					FamiliarController.followOwner(p);
					break;
				case FOLLOW_TARGET:
					FamiliarController.orderSelectedFamiliarFollow(evt.player, (EntityLivingBase) (e==null?res.getSecond().entityHit:e));
					break;
				case FREE:
					// TODO FamiliarController.toggleFamiliarWait(e);
					p.sendStatusMessage(new TextComponentTranslation("This is not implemented yet, sorry!"), true);
					break;
				case GOTO:
					FamiliarController.sendSelectedFamiliarTo(evt.player, res.getSecond().hitVec);
					break;
				case GUARD:
					p.sendStatusMessage(new TextComponentTranslation("This is not implemented yet, sorry!"), true);
					break;
				case NO_COMMAND:
					p.sendStatusMessage(new TextComponentTranslation("This is not implemented yet, sorry!"), true);
					break;
				case OPEN_GUI:
					NetworkHandler.HANDLER.sendTo(new PlayerFamiliarsDefinition(getFamiliarDefinitions(p)), (EntityPlayerMP) p);
					break;
				case SELECT:
					p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectFamiliar(UUIDs.of(e), e.getName());
					break;
				case STAY:
					p.sendStatusMessage(new TextComponentTranslation("This is not implemented yet, sorry!"), true);
					break;
				default:
					p.sendStatusMessage(new TextComponentTranslation("This is a bug, report to Covens!"), true);
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
	
	public static List<FamiliarDescriptor> getFamiliarDefinitions(EntityPlayer p) {
		CapabilityFamiliarOwner owner = p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null);
		List<FamiliarDescriptor> list = Lists.newArrayList();
		owner.familiars.forEach((uuid, desc) -> retrieveAndAdd(p, list, desc));
		return list;
	}
	
	private static void retrieveAndAdd(EntityPlayer p, List<FamiliarDescriptor> list, FamiliarDescriptor desc) {
		EntityLivingBase e = EntityHelper.getEntityAcrossDimensions(desc.getUuid());
		boolean available = e != null;
		if (!available) {
			FamiliarDescriptor lastDesc = p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).familiars.get(desc.getUuid());
			list.add(new FamiliarDescriptor(lastDesc.getName(), lastDesc.getUuid(), lastDesc.getLastKnownPos(), false));
		} else {
			list.add(new FamiliarDescriptor(e.getName(), UUIDs.of(e), new DimensionalPosition(e), true));
		}
	}
}
