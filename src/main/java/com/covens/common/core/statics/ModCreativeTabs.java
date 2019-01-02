package com.covens.common.core.statics;

import com.covens.common.block.ModBlocks;
import com.covens.common.content.cauldron.BrewData;
import com.covens.common.content.cauldron.BrewData.BrewEntry;
import com.covens.common.content.cauldron.BrewModifierListImpl;
import com.covens.common.item.ModItems;
import com.covens.common.lib.LibMod;
import com.covens.common.potion.ModPotions;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class was created by <Arekkuusu> on 26/02/2017. It's distributed as part
 * of Covens under the MIT license.
 */
public final class ModCreativeTabs {

	// Todo: Organize the tabs. They are very messy.

	public static final PlantsCreativeTab PLANTS_CREATIVE_TAB = new PlantsCreativeTab();
	public static final ItemsCreativeTab ITEMS_CREATIVE_TAB = new ItemsCreativeTab();
	public static final BrewsCreativeTab BREW_CREATIVE_TAB = new BrewsCreativeTab();
	public static final BlocksCreativeTab BLOCKS_CREATIVE_TAB = new BlocksCreativeTab();

	private ModCreativeTabs() {
	}

	private static class CreativeTab extends CreativeTabs {

		CreativeTab(String name) {
			super(LibMod.MOD_ID + name);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack createIcon() {
			return this.getIcon();
		}
	}

	private static class PlantsCreativeTab extends CreativeTab {

		PlantsCreativeTab() {
			super("_plants");
		}

		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getIcon() {
			return new ItemStack(ModItems.aconitum);
		}
	}

	private static class ItemsCreativeTab extends CreativeTab {

		ItemsCreativeTab() {
			super("_items");
		}

		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getIcon() {
			return new ItemStack(ModItems.mortar_and_pestle);
		}
	}

	private static class BrewsCreativeTab extends CreativeTab {

		BrewsCreativeTab() {
			super("_brews");
		}

		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getIcon() {
			ItemStack stack = new ItemStack(ModItems.brew_phial_drink);
			BrewData data = new BrewData();
			data.addEntry(new BrewEntry(ModPotions.cursed_leaping, new BrewModifierListImpl()));
			data.saveToStack(stack);
			return stack;
		}
	}

	private static class BlocksCreativeTab extends CreativeTab {

		BlocksCreativeTab() {
			super("_blocks");
		}

		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getIcon() {
			return new ItemStack(ModBlocks.cauldron);
		}
	}
}
