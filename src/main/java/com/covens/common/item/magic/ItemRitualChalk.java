package com.covens.common.item.magic;

import com.covens.api.ritual.EnumGlyphType;
import com.covens.api.state.StateProperties;
import com.covens.common.block.ModBlocks;
import com.covens.common.core.statics.ModSounds;
import com.covens.common.item.ItemMod;
import com.covens.common.lib.LibItemName;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRitualChalk extends ItemMod {

	public static final int MAX_USES = 40;
	private final EnumGlyphType type;

	public ItemRitualChalk(EnumGlyphType id) {
		super(LibItemName.RITUAL_CHALK+"_"+id.name().toLowerCase());
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setMaxDamage(MAX_USES);
		this.type = id;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Override
	public boolean canItemEditBlocks() {
		return true;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		boolean isReplacing = worldIn.getBlockState(pos).getBlock().equals(ModBlocks.ritual_glyphs) && (worldIn.getBlockState(pos).getValue(StateProperties.GLYPH_TYPE) != EnumGlyphType.GOLDEN);
		if (!worldIn.isRemote && (((facing == EnumFacing.UP) && ModBlocks.ritual_glyphs.canPlaceBlockAt(worldIn, pos.up())) || isReplacing)) {
			ItemStack chalk = player.getHeldItem(hand);
			if (!player.isCreative()) {
				chalk.damageItem(1, player);
			}
			IBlockState state = ModBlocks.ritual_glyphs.getExtendedState(ModBlocks.ritual_glyphs.getDefaultState(), worldIn, pos);
			state = state.withProperty(BlockHorizontal.FACING, EnumFacing.HORIZONTALS[(int) (Math.random() * 4)]);
			state = state.withProperty(StateProperties.GLYPH_TYPE, this.type);
			worldIn.setBlockState(isReplacing ? pos : pos.up(), state, 3);
			worldIn.playSound(null, pos, ModSounds.CHALK_SCRIBBLE, SoundCategory.BLOCKS, 0.5f, 1f + (0.5f * player.getRNG().nextFloat()));
		}
		return EnumActionResult.SUCCESS;
	}
	

	public EnumGlyphType getType() {
		return this.type;
	}

}
