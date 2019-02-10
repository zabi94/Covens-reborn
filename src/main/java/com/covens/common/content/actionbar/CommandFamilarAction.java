package com.covens.common.content.actionbar;

import com.covens.common.content.familiar.FamiliarController;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommandFamilarAction extends HotbarAction {

	public CommandFamilarAction(ResourceLocation name, int iconX, int iconY) {
		super(name, iconX, iconY);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconIndexX() {
		Entity e = Minecraft.getMinecraft().pointedEntity;
		return super.getIconIndexX() + FamiliarController.getExecutedCommand(Minecraft.getMinecraft().player, e).getFirst().ordinal();
	}

}
