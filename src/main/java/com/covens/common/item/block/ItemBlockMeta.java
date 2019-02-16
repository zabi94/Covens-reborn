package com.covens.common.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMeta<T extends Item> extends ItemBlock {

	private Enum<?>[] itemVariants;
	private EnumNameMode nMode;

	public ItemBlockMeta(Block block, Enum<?>[] variants, EnumNameMode nameMode) {
		super(block);
		if (variants.length == 0) {
			throw new IllegalArgumentException("At least 1 variant is required");
		}
		this.setRegistryName(block.getRegistryName());
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.itemVariants = variants;
		this.nMode = nameMode;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if ((this.nMode == EnumNameMode.TOOLTIP) && (stack.getMetadata() >= 0) && (stack.getMetadata() < this.itemVariants.length)) {
			tooltip.add(I18n.format(this.getRegistryName().toString().replace(':', '.') + ".tooltip.variety." + this.itemVariants[stack.getMetadata()].name().toLowerCase()));
		}
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		if (this.nMode == EnumNameMode.NAME) {
			int meta = stack.getMetadata();
			if ((meta < 0) || (meta >= this.itemVariants.length)) {
				meta = 0;
			}
			return super.getTranslationKey() + "_" + this.itemVariants[stack.getMetadata()].name().toLowerCase();
		}
		return super.getTranslationKey();
	}

	public static enum EnumNameMode {
		NONE, TOOLTIP, NAME
	}

}
