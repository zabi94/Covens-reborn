package com.covens.common.content.familiar;

import java.util.List;
import java.util.UUID;

import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.helper.MobHelper;
import com.covens.common.core.util.syncTasks.FamiliarFollowEntity;
import com.covens.common.core.util.syncTasks.FamiliarOrderGoto;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import zabi.minecraft.minerva.common.data.UUIDs;
import zabi.minecraft.minerva.common.utils.entity.EntitySyncHelper;

public class FamiliarController {
	
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
}
