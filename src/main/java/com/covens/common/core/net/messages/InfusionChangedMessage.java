package com.covens.common.core.net.messages;

import com.covens.api.CovensAPI;
import com.covens.common.content.infusion.ModInfusions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.minerva.common.network.SimpleMessage;

public class InfusionChangedMessage extends SimpleMessage<InfusionChangedMessage> {

	public ResourceLocation infusion;

	public InfusionChangedMessage(EntityPlayer p) {
		this.infusion = CovensAPI.getAPI().getPlayerInfusion(p).getRegistryName();
	}

	public InfusionChangedMessage() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage handleMessage(MessageContext context) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			CovensAPI.getAPI().setPlayerInfusion(Minecraft.getMinecraft().player, ModInfusions.REGISTRY.getValue(this.infusion));
		});
		return null;
	}

}
