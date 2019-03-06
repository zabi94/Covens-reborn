package com.covens.common.core.net.messages;

import java.util.UUID;

import com.covens.common.content.familiar.FamiliarDescriptor;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.minerva.common.network.SimpleMessage;

public class SelectFamiliar extends SimpleMessage<SelectFamiliar> {
	
	public UUID id;
	public String name;

	public SelectFamiliar() {
		// Required
	}
	
	public SelectFamiliar(FamiliarDescriptor d) {
		id = d.getUuid();
		name = d.getName();
	}
	
	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayer p = context.getServerHandler().player;
		p.world.getMinecraftServer().addScheduledTask(() -> {
			p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).selectFamiliar(id, name);
		});
		return null;
	}
	
}
