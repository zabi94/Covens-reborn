package com.covens.common.item.natural.seed;

import java.util.List;

import javax.annotation.Nullable;

import com.covens.client.core.IModelRegister;
import com.covens.client.handler.ModelHandler;
import com.covens.common.core.statics.ModCreativeTabs;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class was created by Arekkuusu on 27/02/2017. It's distributed as part
 * of Covens under the MIT license.
 */
@SuppressWarnings("WeakerAccess")
public class ItemSeed extends ItemSeeds implements IModelRegister {

	protected final Block crop;
	protected final Block soil;

	public ItemSeed(String id, Block crop, Block soil) {
		super(crop, soil);
		this.setRegistryName(id);
		this.setTranslationKey(id);
		this.setCreativeTab(ModCreativeTabs.PLANTS_CREATIVE_TAB);
		this.crop = crop;
		this.soil = soil;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.ITALIC + I18n.format("witch.tooltip." + this.getNameInefficiently(stack) + "_description.name"));
	}

	public String getNameInefficiently(ItemStack stack) {
		return this.getTranslationKey().substring(5);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return this.soil == Blocks.FARMLAND ? EnumPlantType.Crop : EnumPlantType.Water;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
