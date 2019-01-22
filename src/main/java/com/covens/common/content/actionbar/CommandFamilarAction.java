package com.covens.common.content.actionbar;

import java.util.UUID;

import com.covens.api.CovensAPI;
import com.covens.common.core.capability.familiar.CapabilityFamiliarCreature;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.common.data.UUIDs;
import zabi.minecraft.minerva.common.utils.entity.RayTraceHelper;

public class CommandFamilarAction extends HotbarAction {

	public CommandFamilarAction(ResourceLocation name, int iconX, int iconY) {
		super(name, iconX, iconY);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconIndexY() {
		Entity e = Minecraft.getMinecraft().pointedEntity;
		UUID selected = Minecraft.getMinecraft().player.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectedFamiliar;
		if (e != null && CovensAPI.getAPI().isValidFamiliar(e) && e.getCapability(CapabilityFamiliarCreature.CAPABILITY, null).owner.equals(Minecraft.getMinecraft().player.getUniqueID())) {
			if (Minecraft.getMinecraft().player.isSneaking()) {
				return super.getIconIndexX() + 6; //Wait
			}
			if (selected.equals(e.getPersistentID())) {
				return super.getIconIndexY() + 2; //Send home
			} else {
				return super.getIconIndexY(); //Select familiar
			}
		}
		if (selected.equals(UUIDs.NULL_UUID)) {
			return super.getIconIndexY() + 1; //Nothing
		}
		RayTraceResult rt = RayTraceHelper.rayTracePlayerSight(Minecraft.getMinecraft().player, 32, true);
		if (rt != null) {
			switch (rt.typeOfHit) {
				case MISS: return super.getIconIndexY() + 3; //Deselect
				case BLOCK: return super.getIconIndexY() + 4; //Go to
				case ENTITY: return super.getIconIndexY() + 5; //Follow
			}
		}
		return super.getIconIndexY() + 1; // Nothing
	}
	
}
