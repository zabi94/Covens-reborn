package com.covens.common.core.net.messages;

import com.covens.client.fx.ParticleF;
import com.covens.common.Covens;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class ParticleMessage implements IMessage {

	private ParticleF particleF;
	private double x;
	private double y;
	private double z;
	private int amount;
	private double xSpeed;
	private double ySpeed;
	private double zSpeed;
	private int[] args;

	public ParticleMessage() {
	}

	public ParticleMessage(ParticleF particleF, double x, double y, double z, int amount, double xSpeed, double ySpeed, double zSpeed, int... args) {
		this.particleF = particleF;
		this.x = x;
		this.y = y;
		this.z = z;
		this.amount = amount;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.zSpeed = zSpeed;
		this.args = args;
	}

	@Override
	public void fromBytes(ByteBuf byteBuf) {
		PacketBuffer buf = new PacketBuffer(byteBuf);
		this.particleF = buf.readEnumValue(ParticleF.class);
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.amount = buf.readInt();
		this.xSpeed = buf.readDouble();
		this.ySpeed = buf.readDouble();
		this.zSpeed = buf.readDouble();
		int argCount = buf.readInt();
		this.args = new int[argCount];
		for (int i = 0; i < argCount; i++) {
			this.args[i] = buf.readInt();
		}
	}

	@Override
	public void toBytes(ByteBuf byteBuf) {
		PacketBuffer buf = new PacketBuffer(byteBuf);
		buf.writeEnumValue(this.particleF);
		buf.writeDouble(this.x);
		buf.writeDouble(this.y);
		buf.writeDouble(this.z);
		buf.writeInt(this.amount);
		buf.writeDouble(this.xSpeed);
		buf.writeDouble(this.ySpeed);
		buf.writeDouble(this.zSpeed);
		buf.writeInt(this.args.length);
		for (int arg : this.args) {
			buf.writeInt(arg);
		}
	}

	public static class ParticleMessageHandler implements IMessageHandler<ParticleMessage, IMessage> {

		@Override
		public IMessage onMessage(ParticleMessage message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				for (int i = 0; i < message.amount; i++) {
					Covens.proxy.spawnParticle(message.particleF, message.x, message.y, message.z, message.xSpeed, message.ySpeed, message.zSpeed, message.args);
				}
			});
			return null;
		}
	}
}
