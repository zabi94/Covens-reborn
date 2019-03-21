package com.covens.common.crafting;

import com.covens.common.block.ModBlocks;
import com.covens.common.block.natural.Gem;
import com.covens.common.item.ModItems;
import com.covens.common.item.ModMaterials;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;


public final class VanillaCrafting {

	private VanillaCrafting() {
	}

	public static void blocks() {

		GameRegistry.addSmelting(ModBlocks.silver_ore, new ItemStack(ModItems.silver_ingot, 1), 0.35F);
		GameRegistry.addSmelting(Blocks.SAPLING, new ItemStack(ModItems.wood_ash, 4), 0.15F);
		GameRegistry.addSmelting(ModItems.silver_scales, new ItemStack(ModItems.silver_nugget, 1), 0.20F);
		GameRegistry.addSmelting((new ItemStack(ModItems.unfired_jar)), new ItemStack(ModItems.empty_jar), 0.45F);
		for (Gem g:Gem.values()) {
			GameRegistry.addSmelting(new ItemStack(g.getOreBlock()), new ItemStack(g.getGemItem(), 4), 0.35F);
		}
		GameRegistry.addSmelting(new ItemStack((ModItems.golden_thread), 1, 0), new ItemStack(Items.GOLD_NUGGET, 1, 0), 1.0F);
		ModMaterials.TOOL_ATHAME.setRepairItem(new ItemStack(ModItems.silver_ingot));
		ModMaterials.ARMOR_SILVER.setRepairItem(new ItemStack(ModItems.silver_ingot));
		ModMaterials.TOOL_SILVER.setRepairItem(new ItemStack(ModItems.silver_ingot));
		ModMaterials.TOOL_COLD_IRON.setRepairItem(new ItemStack(ModItems.cold_iron_ingot));
		ModMaterials.ARMOR_COLD_IRON.setRepairItem(new ItemStack(ModItems.cold_iron_ingot));
		ModMaterials.ARMOR_WITCH_LEATHER.setRepairItem(new ItemStack(ModItems.witches_stitching));
		ModMaterials.ARMOR_VAMPIRE.setRepairItem(new ItemStack(ModItems.sanguine_fabric));
	}
}
