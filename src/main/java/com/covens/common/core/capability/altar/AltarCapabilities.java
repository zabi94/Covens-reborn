package com.covens.common.core.capability.altar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import com.covens.api.altar.IAltarPowerUpgrade;
import com.covens.api.altar.IAltarSpecialEffect;
import com.covens.api.altar.IAltarSpeedUpgrade;
import com.covens.common.lib.LibMod;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AltarCapabilities {
	
	private static final HashMap<Item, List<ICapabilityProvider>> modifiers = new HashMap<>();
	
	
	public static void init() {
		CapabilityManager.INSTANCE.register(IAltarSpecialEffect.class, new FakeStorage<>(), throwError());
		CapabilityManager.INSTANCE.register(IAltarPowerUpgrade.class, new FakeStorage<>(), throwError());
		CapabilityManager.INSTANCE.register(IAltarSpeedUpgrade.class, new FakeStorage<>(), throwError());
		MinecraftForge.EVENT_BUS.register(new AltarCapabilities());
	}
	
	public static <T> Callable<T> throwError() {
		return () -> {
			throw new UnsupportedOperationException("Altar power can't use the default capability constructor");
		};
	}
	
	@SubscribeEvent	
	public void registerItems(AttachCapabilitiesEvent<ItemStack> evt) {
		if (modifiers.containsKey(evt.getObject().getItem())) {
			modifiers.get(evt.getObject().getItem()).forEach(icp -> {
				evt.addCapability(new ResourceLocation(LibMod.MOD_ID, "cap"+evt.getObject().getItem().getRegistryName()), icp);
			});
		}
	}
	
	private static void add(Item i, ICapabilityProvider mod) {
		getList(i).add(mod);
	}
	
	private static void add(Block i, ICapabilityProvider mod) {
		add(Item.getItemFromBlock(i), mod);
	}
	
	private static List<ICapabilityProvider> getList(Item i) {
		if (modifiers.containsKey(i)) {
			return modifiers.get(i);
		}
		List<ICapabilityProvider> list = new ArrayList<>();
		modifiers.put(i, list);
		return list;
	}
	
	public static void loadItems() {
		add(Blocks.TORCH, new MixedProvider(1, 0));
		add(Blocks.DIAMOND_BLOCK, new MixedProvider(999, 200));
	}
}
