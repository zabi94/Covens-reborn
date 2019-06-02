package com.covens.common.content.actionbar;

import com.covens.common.content.familiar.FamiliarController;
import com.covens.common.content.familiar.FamiliarController.FamiliarCommand;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommandFamilarAction extends HotbarAction {
	
	public FamiliarCommand lastCommand = null; 

	public CommandFamilarAction(ResourceLocation name, int iconX, int iconY) {
		super(name, iconX, iconY);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconIndexX() {
		Entity e = Minecraft.getMinecraft().pointedEntity;
		FamiliarCommand fc = FamiliarController.getExecutedCommand(Minecraft.getMinecraft().player, e).getFirst();
		lastCommand = fc;
		return super.getIconIndexX() + fc.ordinal();
	}

}
