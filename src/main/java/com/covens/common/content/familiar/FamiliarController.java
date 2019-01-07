package com.covens.common.content.familiar;

import java.util.UUID;

import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.util.EntitySyncHelper;
import com.covens.common.core.util.UUIDs;
import com.covens.common.core.util.syncTasks.FamiliarFollowEntity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;

public class FamiliarController {
	
	public static void toggleFamiliarWait(EntityLiving familiar) {
		boolean isFollowing = false;
		if (familiar instanceof EntityTameable) {
			isFollowing = ((EntityTameable) familiar).isSitting();
			((EntityTameable) familiar).setSitting(!isFollowing);
		} else {
			
		}
		EntityPlayer p = familiar.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).getOwner();
		if (p != null) {
			p.sendMessage(new TextComponentTranslation("familiar.command.sit."+(isFollowing?"enable":"disable"), familiar.getName()));
		}
	}
	
	public static void orderSelectedFamiliarFollow(EntityPlayer player, EntityLivingBase toFollow) {
		UUID selectedFamiliar = player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar;
		if (UUIDs.isNull(selectedFamiliar)) {
			throw new IllegalArgumentException("Cannot order to unselected familiar");
		}
		EntitySyncHelper.executeOnEntityAvailable(selectedFamiliar, new FamiliarFollowEntity(selectedFamiliar, UUIDs.of(toFollow)));
		player.sendStatusMessage(new TextComponentTranslation("familiar.command.follow", getSelectedFamiliarName(selectedFamiliar, player), toFollow.getName()), true);
	}

	private static String getSelectedFamiliarName(UUID selectedFamiliar, EntityPlayer player) {
		return "familiar.name";
	}

	public static void sendSelectedFamiliarHome(EntityPlayer e) {
		
	}

	public static void sendSelectedFamiliarTo(EntityPlayer e, Vec3d hitVec) {
		
	}

	public static void openFamiliarSelector(EntityPlayer player) {
		
	}
}
