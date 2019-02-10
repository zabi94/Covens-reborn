package com.covens.common.core.net.messages;

import java.util.ArrayList;

import com.covens.common.Covens;
import com.covens.common.content.tarot.TarotHandler;
import com.covens.common.content.tarot.TarotHandler.TarotInfo;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class TarotMessage implements IMessage {

	public ArrayList<TarotInfo> info;

	public TarotMessage() {
	}

	public TarotMessage(EntityPlayer p) {
		this.info = TarotHandler.getTarotsForPlayer(p);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.info = TarotInfo.fromBuffer(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.info.size());
		for (TarotInfo ti : this.info) {
			buf.writeBoolean(ti.isReversed());
			buf.writeInt(ti.getNumber());
			ByteBufUtils.writeUTF8String(buf, ti.getRegistryName());
		}
	}

	public static class TarotMessageHandler implements IMessageHandler<TarotMessage, IMessage> {

		@Override
		public IMessage onMessage(TarotMessage message, MessageContext ctx) {
			Covens.proxy.handleTarot(message.info);
			return null;
		}
	}
}
