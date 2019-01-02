package com.covens.common.content.cauldron.brews;

import java.util.HashMap;
import java.util.Map;

import com.covens.api.cauldron.DefaultModifiers;
import com.covens.api.cauldron.IBrewModifierList;
import com.covens.common.block.ModBlocks;
import com.covens.common.content.cauldron.BrewMod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGlazedTerracotta;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public class PotionIceWorld extends BrewMod {

	private final Map<Block, IBlockState> stateMap = new HashMap<>();

	public PotionIceWorld() {
		super("ice_world", true, 0xB0E0E6, true, 0);
		this.stateMap.put(Blocks.GRASS_PATH, Blocks.PACKED_ICE.getDefaultState());
		this.stateMap.put(Blocks.GRAVEL, Blocks.PACKED_ICE.getDefaultState());
		this.stateMap.put(Blocks.COBBLESTONE, Blocks.PACKED_ICE.getDefaultState());
		this.stateMap.put(Blocks.LOG, Blocks.PACKED_ICE.getDefaultState());
		this.stateMap.put(Blocks.LOG2, Blocks.PACKED_ICE.getDefaultState());
		this.stateMap.put(Blocks.DIRT, Blocks.SNOW.getDefaultState());
		this.stateMap.put(Blocks.GRASS, Blocks.SNOW.getDefaultState());
		this.stateMap.put(Blocks.SAND, Blocks.SNOW.getDefaultState());
		this.stateMap.put(Blocks.MYCELIUM, Blocks.SNOW.getDefaultState());
		this.stateMap.put(Blocks.OBSIDIAN, Blocks.PACKED_ICE.getDefaultState());
		this.stateMap.put(Blocks.SOUL_SAND, Blocks.SNOW.getDefaultState());
		this.stateMap.put(Blocks.WOOL, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE));
		this.stateMap.put(Blocks.FARMLAND, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
		this.stateMap.put(Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.BLUE));
		this.stateMap.put(Blocks.GLASS, Blocks.STAINED_GLASS.getDefaultState().withProperty(BlockStainedGlass.COLOR, EnumDyeColor.BLUE));
		this.stateMap.put(Blocks.STAINED_GLASS, Blocks.STAINED_GLASS.getDefaultState().withProperty(BlockStainedGlass.COLOR, EnumDyeColor.BLUE));
		this.stateMap.put(Blocks.STAINED_GLASS_PANE, Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.BLUE));
		this.stateMap.put(Blocks.HARDENED_CLAY, Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE));
		this.stateMap.put(Blocks.STAINED_HARDENED_CLAY, Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE));
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double mult) {
		entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 80 * (amplifier + 1), amplifier / 2));
	}

	@Override
	public void applyInWorld(World world, BlockPos pos, EnumFacing side, IBrewModifierList modifiers, EntityLivingBase thrower) {
		int box = 2 + modifiers.getLevel(DefaultModifiers.RADIUS).orElse(0);

		BlockPos posI = pos.add(box, box / 2, box);
		BlockPos posF = pos.add(-box, -box / 2, -box);

		Iterable<MutableBlockPos> spots = BlockPos.getAllInBoxMutable(posI, posF);
		for (BlockPos spot : spots) {
			if (spot.distanceSq(pos) < (2 + ((box * box) / 2))) {
				IBlockState state = world.getBlockState(spot);
				if (world.rand.nextInt(4) <= (modifiers.getLevel(DefaultModifiers.POWER).orElse(0) / 2)) {
					if (state.getBlock() instanceof BlockLeaves) {
						world.setBlockState(spot, ModBlocks.fake_ice.getDefaultState(), 3);
					} else if (state.getBlock() instanceof BlockPlanks) {
						world.setBlockState(spot, ModBlocks.fake_ice.getDefaultState(), 3);
					} else if (state.getBlock() == Blocks.STONE) {
						world.setBlockState(spot, ModBlocks.fake_ice.getDefaultState(), 3);
					} else if (state.getBlock() == Blocks.SANDSTONE) {
						world.setBlockState(spot, ModBlocks.fake_ice.getDefaultState(), 3);
					} else if (state.getBlock() == ModBlocks.nethersteel) {
						world.setBlockState(spot, Blocks.PACKED_ICE.getDefaultState(), 3);
					} else if (state.getBlock() instanceof BlockLog) {
						world.setBlockState(spot, Blocks.PACKED_ICE.getDefaultState(), 3);
					} else if (this.stateMap.containsKey(state.getBlock())) {
						world.setBlockState(spot, this.stateMap.get(state.getBlock()), 3);
					} else if (state.getBlock() instanceof BlockGlazedTerracotta) {
						world.setBlockState(spot, Blocks.BLUE_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockHorizontal.FACING, state.getValue(BlockHorizontal.FACING)), 3);
					}
				}
			}
		}
	}
}
