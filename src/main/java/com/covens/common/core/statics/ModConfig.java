/**
 * This class was created by <ArekkuusuJerii>. It's distributed as
 * part of the Grimoire Of Alice Mod. Get the Source Code in github:
 * https://github.com/ArekkuusuJerii/Grimore-Of-Alice
 * <p>
 * Grimoire Of Alice is Open Source and distributed under the
 * Grimoire Of Alice license: https://github.com/ArekkuusuJerii/Grimoire-Of-Alice/blob/master/LICENSE.md
 */
package com.covens.common.core.statics;

import com.covens.common.lib.LibMod;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Config(modid = LibMod.MOD_ID)
@Mod.EventBusSubscriber
public final class ModConfig {

	@Comment("Change vein sizes, generation height and generation chance")
	@Config.RequiresMcRestart
	@Config.LangKey("covens.config.world_gen")
	public static WorldGen WORLD_GEN = new WorldGen();
	@Comment("Customize the client-side only settings")
	@Config.LangKey("covens.config.client")
	public static ClientConfig CLIENT = new ClientConfig();
	@Comment("The amount of ticks between two altar scans. Lower values cause lag!")
	public static int altar_scan_delay = 1200;

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent evt) {
		if (evt.getModID().equals(LibMod.MOD_ID)) {
			ConfigManager.sync(LibMod.MOD_ID, Type.INSTANCE);
		}
	}

	public static class WorldGen {

		@Comment("Silver Ore gen, this determines how much can spawn in a chunk, and how far up")
		public SilverOre silver = new SilverOre();
		@Comment("Tourmaline Ore gen, this determines how much can spawn in a chunk, and how far up")
		public Tourmaline tourmaline = new Tourmaline();
		@Comment("Malachite Ore gen, this determines how much can spawn in a chunk, and how far up")
		public Malachite malachite = new Malachite();
		@Comment("Tigers Eye Ore gen, this determines how much can spawn in a chunk, and how far up")
		public TigersEye tigers_eye = new TigersEye();
		@Comment("Garnet Ore gen, this determines how much can spawn in a chunk, and how far up")
		public Garnet garnet = new Garnet();
		@Comment("Salt Ore gen, this determines how much can spawn in a chunk, and how far up")
		public Salt salt = new Salt();
		@Comment("Beehive gen, this determines how many can spawn in a chunk, and how far up")
		public Beehive beehive = new Beehive();

		public static class SilverOre {
			public int silver_min_vein = 1;
			public int silver_max_vein = 8;
			public int silver_min_height = 10;
			public int silver_max_height = 128;
			public int silver_gen_chance = 8;
		}

		public static class Tourmaline {
			public int tourmaline_min_vein = 1;
			public int tourmaline_max_vein = 2;
			public int tourmaline_min_height = 10;
			public int tourmaline_max_height = 80;
			public int tourmaline_gen_chance = 6;
		}

		public static class Malachite {
			public int malachite_min_vein = 1;
			public int malachite_max_vein = 2;
			public int malachite_min_height = 10;
			public int malachite_max_height = 80;
			public int malachite_gen_chance = 6;
		}

		public static class TigersEye {
			public int tigersEye_min_vein = 1;
			public int tigersEye_max_vein = 2;
			public int tigersEye_min_height = 10;
			public int tigersEye_max_height = 60;
			public int tigersEye_gen_chance = 6;
		}

		public static class Garnet {
			public int garnet_min_vein = 1;
			public int garnet_max_vein = 2;
			public int garnet_min_height = 10;
			public int garnet_max_height = 65;
			public int garnet_gen_chance = 6;
		}

		public static class Salt {
			public int salt_min_vein = 1;
			public int salt_max_vein = 4;
			public int salt_min_height = 10;
			public int salt_max_height = 128;
			public int salt_gen_chance = 6;
		}

		public static class Beehive {
			public int beehive_gen_chance = 12;
		}
	}

	public static class ClientConfig {
		
		@Comment("The amount of visual imprecision to give to chalk runes. Use 0 to have them perfectly aligned to the block")
		@Config.RangeDouble(min = 0d, max = 1d)
		public double glyphImprecision = 0.3d;

		@Comment("Set this to false to disable ritual circles from emitting particles on non-golden circles. Might increase client performance")
		public boolean allGlyphParticles = true;
		
		@Comment("Set this to false to disable the \"When on altar:\" tooltips")
		public boolean showAltarModifiersTooltips = true;
		
		@Comment("Set this to false to keep the MP HUD on screen instead of fading out when not used")
		public boolean autoHideMPHud = true;

		@Comment("When this is true, the player hands in first person will be hidden when an ability is selected")
		public boolean hideHandWithAbility = true;

		@Comment("Set this to true to extend the item hotbar and scroll automatically to the actions instead of looping back to the first item")
		public boolean autoJumpToBar = false;

		@Comment("When this is true, an arrow symbol will appear, and it will highlight when scrolling will automatically go to the action menu")
		public boolean showArrowsInBar = true;

		
	}
}
