package com.covens.common.core.event;

import static com.covens.common.core.statics.ModCrops.ACONITUM;
import static com.covens.common.core.statics.ModCrops.BELLADONNA;
import static com.covens.common.core.statics.ModCrops.CHRYSANTHEMUM;
import static com.covens.common.core.statics.ModCrops.GARLIC;
import static com.covens.common.core.statics.ModCrops.GINGER;
import static com.covens.common.core.statics.ModCrops.HELLEBORE;
import static com.covens.common.core.statics.ModCrops.KELP;
import static com.covens.common.core.statics.ModCrops.LAVENDER;
import static com.covens.common.core.statics.ModCrops.MANDRAKE;
import static com.covens.common.core.statics.ModCrops.MINT;
import static com.covens.common.core.statics.ModCrops.SAGEBRUSH;
import static com.covens.common.core.statics.ModCrops.THISTLE;
import static com.covens.common.core.statics.ModCrops.TULSI;
import static com.covens.common.core.statics.ModCrops.WHITE_SAGE;
import static com.covens.common.core.statics.ModCrops.WORMWOOD;

import com.covens.api.divination.ITarot;
import com.covens.common.block.ModBlocks;
import com.covens.common.block.natural.crop.BlockCrop;
import com.covens.common.content.tarot.TarotHandler;
import com.covens.common.core.helper.CropHelper;
import com.covens.common.core.statics.ModCrops;
import com.covens.common.item.ModItems;
import com.covens.common.item.natural.crop.ItemAconitum;
import com.covens.common.item.natural.crop.ItemBelladonna;
import com.covens.common.item.natural.crop.ItemCropFood;
import com.covens.common.item.natural.crop.ItemGinger;
import com.covens.common.item.natural.crop.ItemKelp;
import com.covens.common.item.natural.crop.ItemLavender;
import com.covens.common.item.natural.crop.ItemMandrake;
import com.covens.common.item.natural.crop.ItemThistle;
import com.covens.common.item.natural.seed.ItemKelpSeed;
import com.covens.common.item.natural.seed.ItemSeed;
import com.covens.common.lib.LibItemName;
import com.covens.common.lib.LibMod;
import com.covens.common.tile.ModTiles;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.RegistryBuilder;


@Mod.EventBusSubscriber(modid = LibMod.MOD_ID)
public final class RegistryEvents {

	private RegistryEvents() {
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		registerCrop(WHITE_SAGE, ModBlocks.crop_white_sage, new ItemCropFood(LibItemName.WHITE_SAGE, 1, 0.4F, false), LibItemName.SEED_WHITE_SAGE);
		registerCrop(WORMWOOD, ModBlocks.crop_wormwood, new ItemCropFood(LibItemName.WORMWOOD, 4, 0.8F, false), LibItemName.SEED_WORMWOOD);
		registerCrop(MANDRAKE, ModBlocks.crop_mandrake_root, new ItemMandrake(), LibItemName.SEED_MANDRAKE);
		registerCrop(GARLIC, ModBlocks.crop_garlic, new ItemCropFood(LibItemName.GARLIC, 4, 6F, false), LibItemName.SEED_GARLIC);
		registerCrop(TULSI, ModBlocks.crop_tulsi, new ItemCropFood(LibItemName.TULSI, 1, 0.4F, false), LibItemName.SEED_TULSI);
		registerCrop(MINT, ModBlocks.crop_mint, new ItemCropFood(LibItemName.MINT, 1, 2F, false), LibItemName.SEED_MINT);
		registerCrop(HELLEBORE, ModBlocks.crop_hellebore, new ItemCropFood(LibItemName.HELLEBORE, 2, 0.1F, false), LibItemName.SEED_HELLEBORE);
		registerCrop(CHRYSANTHEMUM, ModBlocks.crop_chrysanthemum, new ItemCropFood(LibItemName.CHRYSANTHEMUM, 2, 0.1F, false), LibItemName.SEED_CHRYSANTHEMUM);
		registerCrop(SAGEBRUSH, ModBlocks.crop_sagebrush, new ItemCropFood(LibItemName.SAGEBRUSH, 2, 0.1F, false), LibItemName.SEED_SAGEBRUSH);
		registerCrop(BELLADONNA, ModBlocks.crop_belladonna, new ItemBelladonna(), LibItemName.SEED_BELLADONNA);
		registerCrop(ACONITUM, ModBlocks.crop_aconitum, new ItemAconitum(), LibItemName.SEED_ACONITUM);
		registerCrop(LAVENDER, ModBlocks.crop_lavender, new ItemLavender(), LibItemName.SEED_LAVENDER);
		registerCrop(THISTLE, ModBlocks.crop_thistle, new ItemThistle(), LibItemName.SEED_THISTLE);
		registerCrop(GINGER, ModBlocks.crop_ginger, new ItemGinger(), LibItemName.SEED_GINGER);
		CropHelper.registerCrop(KELP, ModBlocks.crop_kelp, new ItemKelp(), new ItemKelpSeed());

		ModItems.register(event.getRegistry());
	}

	private static void registerCrop(ModCrops crop, BlockCrop placed, Item cropItem, String seedName) {
		CropHelper.registerCrop(crop, placed, cropItem, new ItemSeed(seedName, placed, crop.getSoil()));
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ModBlocks.register(event.getRegistry());
		ModTiles.registerAll();
	}

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry().registerAll();
	}
	
	@SubscribeEvent
	public static void createRegistries(RegistryEvent.NewRegistry evt) {
		TarotHandler.REGISTRY = new RegistryBuilder<ITarot>().setName(new ResourceLocation(LibMod.MOD_ID, "tarots")).setType(ITarot.class).setIDRange(0, 200).create();
	}
}
