package com.covens.common.content.familiar;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.covens.common.content.familiar.ai.AIFamiliarSit;
import com.covens.common.content.familiar.ai.AIFollowTarget;
import com.covens.common.content.familiar.ai.AIGotoPlace;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.helper.MobHelper;
import com.covens.common.core.util.syncTasks.FamiliarFollowEntity;
import com.covens.common.core.util.syncTasks.FamiliarOrderGoto;
import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import zabi.minecraft.minerva.common.data.UUIDs;
import zabi.minecraft.minerva.common.utils.entity.EntitySyncHelper;

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
			p.sendMessage(new TextComponentTranslation("familiar.command.sit."+(isFollowing?"disable":"enable"), familiar.getName()));
		}
	}
	
	public static void orderSelectedFamiliarFollow(EntityPlayer player, EntityLivingBase toFollow) {
		UUID selectedFamiliar = player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar;
		if (UUIDs.isNull(selectedFamiliar)) {
			throw new IllegalArgumentException("Cannot order to unselected familiar");
		}
		
		if (MobHelper.isSpirit(toFollow)) {
			player.sendStatusMessage(new TextComponentTranslation("familiar.command.follow.forbidden", toFollow.getName()), true);
			return;
		}
		
		EntitySyncHelper.executeOnEntityAvailable(selectedFamiliar, new FamiliarFollowEntity(selectedFamiliar, UUIDs.of(toFollow)));
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

	public static void sendSelectedFamiliarTo(EntityPlayer e, Vec3d hitVec) {
		UUID familiar = e.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar;
		if (!familiar.equals(UUIDs.NULL_UUID)) {
			EntitySyncHelper.executeOnEntityAvailable(familiar, new FamiliarOrderGoto(new BlockPos(hitVec)));
			e.sendStatusMessage(new TextComponentTranslation("familiar.command.goto", getSelectedFamiliarName(familiar, e)), true);
		}
	}

	public static void openFamiliarSelector(EntityPlayer player) {
		player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectFamiliar(null);
	}
	
	public static void setupFamiliarAI(EntityLiving entity) {
		removeTasks(entity);
		removeTargets(entity);
		entity.tasks.addTask(2, new AIFollowTarget(entity));
		entity.tasks.addTask(3, new AIGotoPlace(entity));
		entity.tasks.addTask(4, new AIFamiliarSit(entity));
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
}
