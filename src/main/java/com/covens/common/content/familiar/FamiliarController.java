package com.covens.common.content.familiar;

import java.util.UUID;

import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.covens.common.core.helper.Log;
import com.covens.common.core.util.EntitySyncHelper;
import com.covens.common.core.util.UUIDs;
import com.covens.common.core.util.syncTasks.FamiliarFollowEntity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class FamiliarController {
	
	public static void toggleFamiliarWait(EntityLiving familiar) {
		
	}
	
	public static void orderSelectedFamiliarFollow(EntityPlayer player, EntityLivingBase toFollow) {
		UUID selectedFamiliar = player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar;
		if (UUIDs.isNull(selectedFamiliar)) {
			throw new IllegalArgumentException("Cannot order to unselected familiar");
		}
		Log.i("Follow order issued");
		EntitySyncHelper.executeOnEntityAvailable(selectedFamiliar, new FamiliarFollowEntity(selectedFamiliar, UUIDs.of(toFollow)));
	}

	public static void sendSelectedFamiliarHome(EntityPlayer e) {
		
	}

	public static void sendSelectedFamiliarTo(EntityPlayer e, Vec3d hitVec) {
		
	}

	public static void openFamiliarSelector(EntityPlayer player) {
		
	}
}
