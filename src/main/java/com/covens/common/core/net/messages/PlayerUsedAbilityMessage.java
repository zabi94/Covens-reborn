package com.covens.common.core.net.messages;

import com.covens.api.event.HotbarActionCollectionEvent;
import com.covens.api.event.HotbarActionTriggeredEvent;
import com.covens.api.hotbar.IHotbarAction;
import com.covens.common.content.actionbar.HotbarAction;
import com.covens.common.core.net.SimpleMessage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PlayerUsedAbilityMessage extends SimpleMessage<PlayerUsedAbilityMessage> {

	public String ability;
	public int entity;

	public PlayerUsedAbilityMessage() {
	}

	public PlayerUsedAbilityMessage(String ability, int focused_entity) {
		this.ability = ability;
		this.entity = focused_entity;
	}

	@Override
	public IMessage handleMessage(MessageContext context) {
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
			IHotbarAction action = HotbarAction.getFromRegistryName(this.ability);
			HotbarActionCollectionEvent evt = new HotbarActionCollectionEvent(context.getServerHandler().player, context.getServerHandler().player.world);
			MinecraftForge.EVENT_BUS.post(evt);
			if (evt.getList().contains(action)) {
				MinecraftForge.EVENT_BUS.post(new HotbarActionTriggeredEvent(action, context.getServerHandler().player, context.getServerHandler().player.world, context.getServerHandler().player.world.getEntityByID(this.entity)));
			}
		});
		return null;
	}

}
