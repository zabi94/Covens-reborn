package com.covens.common.core.net.messages;

import com.covens.api.mp.MPContainer;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.minerva.common.network.SimpleMessage;

public class EnergySync extends SimpleMessage<EnergySync> {

	public int amount, max;

	public EnergySync() {
		this.amount = 0;
		this.max = 0;
	}

	public EnergySync(int energy_amount, int energy_max) {
		this.amount = energy_amount;
		this.max = energy_max;
	}

	@Override
	public IMessage handleMessage(MessageContext context) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			MPContainer c = Minecraft.getMinecraft().player.getCapability(MPContainer.CAPABILITY, null);
			c.setMaxAmount(this.max);
			c.setAmount(this.amount);
		});
		return null;
	}
}
