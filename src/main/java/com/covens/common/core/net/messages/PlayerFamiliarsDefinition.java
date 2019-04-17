package com.covens.common.core.net.messages;

import java.util.List;

import com.covens.client.gui.GuiFamiliarSelector;
import com.covens.common.content.familiar.FamiliarDescriptor;
import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.common.network.SimpleMessage;
import zabi.minecraft.minerva.common.utils.DimensionalPosition;

public class PlayerFamiliarsDefinition implements IMessage {
	
	protected List<FamiliarDescriptor> familiars;

	public PlayerFamiliarsDefinition() {
		// Needed
	}
	
	public PlayerFamiliarsDefinition(List<FamiliarDescriptor> familiarList) {
		familiars = familiarList;
	}
	
	public static class Handler implements IMessageHandler<PlayerFamiliarsDefinition, IMessage> {
		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(PlayerFamiliarsDefinition message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiFamiliarSelector(message.familiars)));
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		familiars = Lists.newArrayList();
		int amount = buf.readInt();
		for (int i = 0; i < amount; i++) {
			FamiliarDescriptor fd = new FamiliarDescriptor(
					SimpleMessage.readString(buf), 
					SimpleMessage.readUUID(buf), 
					new DimensionalPosition(SimpleMessage.readBlockPos(buf), buf.readInt()), 
					buf.readBoolean());
			familiars.add(fd);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(familiars.size());
		for (FamiliarDescriptor fd: familiars) {
			SimpleMessage.writeString(fd.getName(), buf);
			SimpleMessage.writeUUID(fd.getUuid(), buf);
			SimpleMessage.writeBlockPos(fd.getLastKnownPos().getPosition(), buf);
			buf.writeInt(fd.getLastKnownPos().getDim());
			buf.writeBoolean(fd.isAvailable());
		}
	}
	
}
