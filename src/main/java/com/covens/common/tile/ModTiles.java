package com.covens.common.tile;

import com.covens.common.lib.LibMod;
import com.covens.common.tile.tiles.TileEntityApiary;
import com.covens.common.tile.tiles.TileEntityCauldron;
import com.covens.common.tile.tiles.TileEntityCrystalBall;
import com.covens.common.tile.tiles.TileEntityDistillery;
import com.covens.common.tile.tiles.TileEntityGemBowl;
import com.covens.common.tile.tiles.TileEntityGlyph;
import com.covens.common.tile.tiles.TileEntityMagicMirror;
import com.covens.common.tile.tiles.TileEntityOven;
import com.covens.common.tile.tiles.TileEntityPlacedItem;
import com.covens.common.tile.tiles.TileEntityTarotsTable;
import com.covens.common.tile.tiles.TileEntityThreadSpinner;
import com.covens.common.tile.tiles.TileEntityWitchAltar;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;


public final class ModTiles {

	private static final ResourceLocation CAULDRON = new ResourceLocation(LibMod.MOD_ID, "cauldron");
	private static final ResourceLocation APIARY = new ResourceLocation(LibMod.MOD_ID, "apiary");
	private static final ResourceLocation OVEN = new ResourceLocation(LibMod.MOD_ID, "oven");
	private static final ResourceLocation WITCH_ALTAR = new ResourceLocation(LibMod.MOD_ID, "witch_altar");
	private static final ResourceLocation THREAD_SPINNER = new ResourceLocation(LibMod.MOD_ID, "thread_spinner");
	private static final ResourceLocation GLYPH = new ResourceLocation(LibMod.MOD_ID, "glyph");
	private static final ResourceLocation CRYSTAL_BALL = new ResourceLocation(LibMod.MOD_ID, "crystal_ball");
	private static final ResourceLocation TAROTS_TABLE = new ResourceLocation(LibMod.MOD_ID, "tarots_table");
	private static final ResourceLocation GEM_BOWL = new ResourceLocation(LibMod.MOD_ID, "gem_bowl");
	private static final ResourceLocation MAGIC_MIRROR = new ResourceLocation(LibMod.MOD_ID, "magic_mirror");
	private static final ResourceLocation PLACED_ITEM = new ResourceLocation(LibMod.MOD_ID, "placed_item");
	private static final ResourceLocation DISTILLERY = new ResourceLocation(LibMod.MOD_ID, "distillery");

	private ModTiles() {
	}

	public static void registerAll() {
		GameRegistry.registerTileEntity(TileEntityCauldron.class, CAULDRON);
		GameRegistry.registerTileEntity(TileEntityApiary.class, APIARY);
		GameRegistry.registerTileEntity(TileEntityOven.class, OVEN);
		GameRegistry.registerTileEntity(TileEntityWitchAltar.class, WITCH_ALTAR);
		GameRegistry.registerTileEntity(TileEntityThreadSpinner.class, THREAD_SPINNER);
		GameRegistry.registerTileEntity(TileEntityGlyph.class, GLYPH);
		GameRegistry.registerTileEntity(TileEntityCrystalBall.class, CRYSTAL_BALL);
		GameRegistry.registerTileEntity(TileEntityTarotsTable.class, TAROTS_TABLE);
		GameRegistry.registerTileEntity(TileEntityGemBowl.class, GEM_BOWL);
		GameRegistry.registerTileEntity(TileEntityMagicMirror.class, MAGIC_MIRROR);
		GameRegistry.registerTileEntity(TileEntityPlacedItem.class, PLACED_ITEM);
		GameRegistry.registerTileEntity(TileEntityDistillery.class, DISTILLERY);
	}
}
