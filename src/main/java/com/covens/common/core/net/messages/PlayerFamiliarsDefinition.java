package com.covens.common.core.net.messages;

import java.util.List;

import com.covens.client.gui.GuiFamiliarSelector;
import com.covens.common.content.familiar.FamiliarDescriptor;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.common.network.SimpleMessage;

public class PlayerFamiliarsDefinition extends SimpleMessage<PlayerFamiliarsDefinition> {
	
	public List<FamiliarDescriptor> familiars;

	public PlayerFamiliarsDefinition() {
		// Needed
	}
	
	public PlayerFamiliarsDefinition(List<FamiliarDescriptor> familiarList) {
		familiars = familiarList;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IMessage handleMessage(MessageContext context) {
		Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiFamiliarSelector(familiars)));
		return null;
	}
	
}
