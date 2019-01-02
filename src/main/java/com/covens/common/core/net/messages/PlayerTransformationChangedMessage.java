package com.covens.common.core.net.messages;

import com.covens.api.CovensAPI;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.content.transformation.ModTransformations;
import com.covens.common.core.net.SimpleMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerTransformationChangedMessage extends SimpleMessage<PlayerTransformationChangedMessage> {

	public int level;
	public String type;

	public PlayerTransformationChangedMessage() {
	}

	public PlayerTransformationChangedMessage(EntityPlayer player) {
		CapabilityTransformation data = player.getCapability(CapabilityTransformation.CAPABILITY, null);
		this.type = data.getType().getRegistryName().toString();
		this.level = data.getLevel();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IMessage handleMessage(MessageContext context) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			CovensAPI.getAPI().setTypeAndLevel(Minecraft.getMinecraft().player, ModTransformations.REGISTRY.getValue(new ResourceLocation(this.type)), this.level, true);
		});
		return null;
	}

}
