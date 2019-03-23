package com.covens.common.core.capability.altar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

import com.covens.api.altar.IAltarPowerUpgrade;
import com.covens.api.altar.IAltarSpecialEffect;
import com.covens.api.altar.IAltarSpeedUpgrade;
import com.covens.common.block.ModBlocks;
import com.covens.common.core.capability.altar.tile_providers.FlowerPotProvider;
import com.covens.common.core.capability.altar.tile_providers.SkullProvider;
import com.covens.common.core.statics.ModConfig;
import com.covens.common.lib.LibMod;
import com.covens.common.tile.tiles.TileEntityGemBowl;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AltarCapabilities {

	@CapabilityInject(IAltarSpecialEffect.class)
	public static Capability<IAltarSpecialEffect> ALTAR_EFFECT_CAPABILITY;

	@CapabilityInject(IAltarSpeedUpgrade.class)
	public static Capability<IAltarSpeedUpgrade> ALTAR_GAIN_CAPABILITY;

	@CapabilityInject(IAltarPowerUpgrade.class)
	public static Capability<IAltarPowerUpgrade> ALTAR_MULTIPLIER_CAPABILITY;

	private static final HashMap<Item, List<ICapabilityProvider>> items = new HashMap<>();
	private static final HashMap<Item, Function<ItemStack, List<ICapabilityProvider>>> tile_items = new HashMap<>();
	private static final HashMap<Class<? extends TileEntity>, List<Function<TileEntity, ICapabilityProvider>>> tiles = new HashMap<>();

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
		if (items.containsKey(evt.getObject().getItem())) {
			items.get(evt.getObject().getItem()).forEach(icp -> {
				evt.addCapability(new ResourceLocation(LibMod.MOD_ID, "cap"+evt.getObject().getItem().getRegistryName()), icp);
			});
		}
		
		if (tile_items.containsKey(evt.getObject().getItem())) {
			tile_items.get(evt.getObject().getItem()).apply(evt.getObject()).forEach(icp -> {
				evt.addCapability(new ResourceLocation(LibMod.MOD_ID, "cap"+evt.getObject().getItem().getRegistryName()), icp);
			});
			
		}
	}

	@SubscribeEvent	
	public void registerTiles(AttachCapabilitiesEvent<TileEntity> evt) {
		if (tiles.containsKey(evt.getObject().getClass())) {
			tiles.get(evt.getObject().getClass()).forEach(icp -> {
				evt.addCapability(new ResourceLocation(LibMod.MOD_ID, "cap"+evt.getObject().getClass().getName()), icp.apply(evt.getObject()));
			});
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	@SideOnly(Side.CLIENT)
	public void addTooltip(ItemTooltipEvent evt) {
		if (ModConfig.CLIENT.showAltarModifiersTooltips) {
			ItemStack stack = evt.getItemStack();
			if (stack.hasCapability(ALTAR_EFFECT_CAPABILITY, null) || stack.hasCapability(ALTAR_GAIN_CAPABILITY, null) || stack.hasCapability(ALTAR_MULTIPLIER_CAPABILITY, null)) {
				evt.getToolTip().add("");
				evt.getToolTip().add(TextFormatting.DARK_PURPLE+I18n.format("altar.effect.tooltip.when"));
				if (stack.hasCapability(ALTAR_EFFECT_CAPABILITY, null)) {
					evt.getToolTip().add(TextFormatting.GRAY+TextFormatting.ITALIC.toString()+I18n.format(stack.getCapability(ALTAR_EFFECT_CAPABILITY, null).getDescriptionTranslationKey()));
				}
				if (stack.hasCapability(ALTAR_GAIN_CAPABILITY, null)) {
					evt.getToolTip().add(TextFormatting.GRAY+TextFormatting.ITALIC.toString()+I18n.format("altar.effect.tooltip.gain"));
				}
				if (stack.hasCapability(ALTAR_MULTIPLIER_CAPABILITY, null)) {
					evt.getToolTip().add(TextFormatting.GRAY+TextFormatting.ITALIC.toString()+I18n.format("altar.effect.tooltip.mult"));
				}
			}
			if (Minecraft.getMinecraft().player!= null && Minecraft.getMinecraft().player.getHeldItemMainhand().getItem()==Item.getItemFromBlock(ModBlocks.gem_bowl)) {
				double value = TileEntityGemBowl.getGemValue(stack);
				if (value != 0) {
					evt.getToolTip().add("");
					evt.getToolTip().add(TextFormatting.DARK_PURPLE+I18n.format("altar.effect.tooltip.gem", value));
				}
			}
		}
	}

	private static void add(Item i, ICapabilityProvider mod) {
		getList(i).add(mod);
	}

	private static void add(Block i, ICapabilityProvider mod) {
		add(Item.getItemFromBlock(i), mod);
	}

	private static void add(Class<? extends TileEntity> i, Function<TileEntity, ICapabilityProvider> mod, Function<ItemStack, List<ICapabilityProvider>> stack_provider, Item... items) {
		getList(i).add(mod);
		for (Item item:items) {
			if (item != Items.AIR) {
				tile_items.put(item, stack_provider);
			}
		}
	}

	private static List<ICapabilityProvider> getList(Item i) {
		if (items.containsKey(i)) {
			return items.get(i);
		}
		List<ICapabilityProvider> list = new ArrayList<>();
		items.put(i, list);
		return list;
	}

	private static List<Function<TileEntity, ICapabilityProvider>> getList(Class<? extends TileEntity> i) {
		if (tiles.containsKey(i)) {
			return tiles.get(i);
		}
		List<Function<TileEntity, ICapabilityProvider>> list = new ArrayList<>();
		tiles.put(i, list);
		return list;
	}

	public static void loadObjects() {
		add(Blocks.TORCH, new GainProvider(1));
		add(Blocks.DIAMOND_BLOCK, new MixedProvider(999, 200));

		add(TileEntityFlowerPot.class, t -> new FlowerPotProvider((TileEntityFlowerPot) t), s -> ImmutableList.of(new FakeProvider(false, true, "altar.effect.tooltip.needs_plant")), Items.FLOWER_POT);
		add(TileEntitySkull.class, t -> new SkullProvider((TileEntitySkull) t), s -> ImmutableList.of(new FakeProvider(true, true)), Items.SKULL);
	}
}
