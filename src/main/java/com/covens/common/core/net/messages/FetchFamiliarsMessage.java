package com.covens.common.core.net.messages;

import com.covens.common.content.familiar.FamiliarEvents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.minerva.common.network.SimpleMessage;

public class FetchFamiliarsMessage extends SimpleMessage<FetchFamiliarsMessage> {
	
	public FetchFamiliarsMessage() {
		//NO-OP
	}
	
	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayer p = context.getServerHandler().player;
		return new PlayerFamiliarsDefinition(FamiliarEvents.getFamiliarDefinitions(p));
	}

}
