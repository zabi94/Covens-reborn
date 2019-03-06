package com.covens.common.content.familiar;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.covens.api.CovensAPI;
import com.covens.common.content.familiar.ai.AIFamiliarSit;
import com.covens.common.content.familiar.ai.AIFollowTarget;
import com.covens.common.content.familiar.ai.AIGotoPlace;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.helper.MobHelper;
import com.covens.common.core.util.syncTasks.FamiliarFollowEntity;
import com.covens.common.core.util.syncTasks.FamiliarOrderGoto;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import zabi.minecraft.minerva.common.entity.RayTraceHelper;
import zabi.minecraft.minerva.common.entity.UUIDs;
import zabi.minecraft.minerva.common.entity.synchronization.SyncManager;

public class FamiliarController {

	public static final Set<String> bannedTasks = Sets.newHashSet();
	static {
		bannedTasks.add("");
	}

	public static void toggleFamiliarWait(EntityLiving familiar) {
		boolean isFollowing = !CapabilityFamiliarCreature.isSitting(familiar);
		CapabilityFamiliarCreature.setSitting(familiar, isFollowing);
		EntityPlayer p = familiar.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).getOwner();
		if (p != null) {
			p.sendStatusMessage(new TextComponentTranslation("familiar.command.sit."+(isFollowing?"enable":"disable"), familiar.getName()), true);
		}
	}
	
	public static void followOwner(EntityPlayer player) {
		UUID selectedFamiliar = player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar;
		if (UUIDs.isNull(selectedFamiliar)) {
			return;
		}
		SyncManager.executeOnEntityAvailable(selectedFamiliar, new FamiliarFollowEntity(selectedFamiliar, UUIDs.of(player)));
		player.sendStatusMessage(new TextComponentTranslation("familiar.command.followme", getSelectedFamiliarName(selectedFamiliar, player)), true);
	}

	public static void orderSelectedFamiliarFollow(EntityPlayer player, EntityLivingBase toFollow) {
		UUID selectedFamiliar = player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar;
		if (UUIDs.isNull(selectedFamiliar)) {
			return;
		}

		if (MobHelper.isSpirit(toFollow)) {
			player.sendStatusMessage(new TextComponentTranslation("familiar.command.follow.forbidden", toFollow.getName()), true);
			return;
		}

		SyncManager.executeOnEntityAvailable(selectedFamiliar, new FamiliarFollowEntity(selectedFamiliar, UUIDs.of(toFollow)));
		player.sendStatusMessage(new TextComponentTranslation("familiar.command.follow", getSelectedFamiliarName(selectedFamiliar, player), toFollow.getName()), true);
	}

	private static String getSelectedFamiliarName(UUID selectedFamiliar, EntityPlayer player) {
		List<EntityLiving> el = player.world.getEntities(EntityLiving.class, e -> e.getPersistentID().equals(selectedFamiliar));
		if (el.isEmpty()) {
			return player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliarName;
		}
		return el.get(0).getName();
	}

	public static void sendSelectedFamiliarHome(EntityPlayer e) {

	}
	
	public static boolean hasFamiliarsInRange(EntityPlayer p) {
		return !getFamiliarsInRange(p).isEmpty();
	}
	
	public static List<EntityLivingBase> getFamiliarsInRange(EntityPlayer p) {
		return p.world.getEntitiesWithinAABB(EntityLivingBase.class, p.getEntityBoundingBox().grow(5, 2, 5), e -> CovensAPI.getAPI().isValidFamiliar(e)).stream()
				.filter(e -> e.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).owner.equals(UUIDs.of(p)))
				.collect(Collectors.toList());
	}

	public static void sendSelectedFamiliarTo(EntityPlayer e, Vec3d hitVec) {
		UUID familiar = e.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar;
		if (!familiar.equals(UUIDs.NULL_UUID)) {
			SyncManager.executeOnEntityAvailable(familiar, new FamiliarOrderGoto(new BlockPos(hitVec)));
			e.sendStatusMessage(new TextComponentTranslation("familiar.command.goto", getSelectedFamiliarName(familiar, e)), true);
		}
	}

	public static void openFamiliarSelector(EntityPlayer player) {
		player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).deselectFamiliar();
	}

	public static void setupFamiliar(EntityLiving entity) {
		if (entity.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).aiSet) {
			return;
		}
		entity.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).aiSet = true;
		entity.getTags().add("familiar");
		removeTasks(entity);
		removeTargets(entity);
		entity.tasks.addTask(1, new AIFollowTarget(entity));
		entity.tasks.addTask(1, new AIGotoPlace(entity));
		if (!(entity instanceof EntityTameable)) {
			entity.tasks.addTask(2, new AIFamiliarSit(entity));
		}
		//		entity.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(entity));
		//		entity.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(entity));
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

	@Nonnull
	public static Tuple<FamiliarCommand, RayTraceResult> getExecutedCommand(EntityPlayer player, Entity pointedClose) {
		UUID selectedFamiliarUUID = player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar;
		if (UUIDs.isNull(selectedFamiliarUUID)) {
			if (pointedClose != null) {
				if (CovensAPI.getAPI().isValidFamiliar(pointedClose)) {
					if (pointedClose.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).owner.equals(UUIDs.of(player))) {
						return new Tuple<>(FamiliarCommand.SELECT, null);
					}
				}
				return new Tuple<>(FamiliarCommand.NO_COMMAND, null);
			} else {
				return new Tuple<>(FamiliarCommand.OPEN_GUI, null);
			}
		} else if (UUIDs.of(pointedClose).equals(selectedFamiliarUUID)) {
			if (player.isSneaking()) {
				return new Tuple<>(FamiliarCommand.GO_HOME, null);
			} else {
				return new Tuple<>(FamiliarCommand.FREE, null); //rotate: stay follow_me free
			}
		} else if (pointedClose != null) {
			return new Tuple<>(FamiliarCommand.FOLLOW_TARGET, null);
		}
		RayTraceResult rt = RayTraceHelper.rayTracePlayerSight(player, 32, true);
		if (rt != null) {
			switch (rt.typeOfHit) {
				case ENTITY: {
					if (player.isSneaking()) {
						return new Tuple<>(FamiliarCommand.FOLLOW_TARGET, rt);
					} else {
						return new Tuple<>(FamiliarCommand.ATTACK, rt);
					}
				}
				case BLOCK: {
					if (player.isSneaking()) {
						return new Tuple<>(FamiliarCommand.GUARD, rt);
					} else {
						return new Tuple<>(FamiliarCommand.GOTO, rt);
					}
				}
				case MISS: {
					
				}
			}
		}
		if (player.rotationPitch < -85) {
			return new Tuple<>(FamiliarCommand.FOLLOW_ME, null);
		}
		return new Tuple<>(FamiliarCommand.DESELECT, null);
	}

	public static enum FamiliarCommand {
		SELECT, DESELECT, FOLLOW_TARGET, FOLLOW_ME, STAY, GUARD, GOTO, ATTACK, OPEN_GUI, FREE, NO_COMMAND, GO_HOME
	}
}
