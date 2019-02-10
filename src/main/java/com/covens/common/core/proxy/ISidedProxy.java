package com.covens.common.core.proxy;

import java.util.ArrayList;

import com.covens.api.hotbar.IHotbarAction;
import com.covens.client.fx.ParticleF;
import com.covens.common.content.tarot.TarotHandler.TarotInfo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


public interface ISidedProxy {

	void preInit(FMLPreInitializationEvent event);

	void init(FMLInitializationEvent event);

	void spawnParticle(ParticleF particleF, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... args);

	boolean isFancyGraphicsEnabled();

	void handleTarot(ArrayList<TarotInfo> tarots);

	void loadActionsClient(ArrayList<IHotbarAction> actions, EntityPlayer player);

	boolean isPlayerInEndFire();

	void stopMimicking(EntityPlayer p);
}
