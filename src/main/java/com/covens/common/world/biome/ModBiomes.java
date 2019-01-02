package com.covens.common.world.biome;

import com.covens.api.CovensAPI;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class ModBiomes {

	public static final Biome VAMPIRE_LAIR = new BiomeVampireLair();

	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> evt) {
		evt.getRegistry().registerAll(VAMPIRE_LAIR);

		BiomeDictionary.addTypes(VAMPIRE_LAIR, CovensAPI.getAPI().IMMUTABLE);
	}
}
