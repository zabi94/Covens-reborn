package com.covens.common.content.cauldron.brews;

import java.util.List;

import com.covens.api.cauldron.DefaultModifiers;
import com.covens.api.cauldron.IBrewModifierList;
import com.covens.common.content.cauldron.BrewMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class PotionPlant extends BrewMod {

	public PotionPlant() {
		super("plant", true, 0x003506, true, 0, 10);
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase e, int amplifier, double health) {
		if (e.world.isAirBlock(e.getPosition())) {
			e.setPositionAndUpdate(e.posX, e.posY > 3 ? e.posY - 1 : e.posY, e.posZ);
		}
	}

	@Override
	public boolean hasInWorldEffect() {
		return true;
	}

	@Override
	public void applyInWorld(World world, BlockPos posIn, EnumFacing side, IBrewModifierList modifiers, EntityLivingBase thrower) {
		BlockPos pos = posIn;
		int amplifier = modifiers.getLevel(DefaultModifiers.POWER).orElse(0);
		if (side != null) {
			pos = pos.offset(side);
		}
		AxisAlignedBB space = new AxisAlignedBB(pos).grow(2 + MathHelper.clamp(amplifier, 0, 5));
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, space, input -> (input != null) && this.isSeed(input.getItem()));
		if (!items.isEmpty()) {
			this.plantAll(items, world, pos, amplifier);
		}
	}

	private boolean isSeed(ItemStack stack) {
		return stack.getItem() instanceof IPlantable;
	}

	private void plantAll(List<EntityItem> items, World world, BlockPos pos, int amplifier) {
		int box = 1 + (int) (amplifier / 2F);

		BlockPos posI = pos.add(box, 1, box);
		BlockPos posF = pos.add(-box, -1, -box);

		Iterable<BlockPos> spots = BlockPos.getAllInBox(posI, posF);

		for (EntityItem item : items) {
			ItemStack stack = item.getItem();
			for (BlockPos spot : spots) {
				if (stack.isEmpty()) {
					item.setDead();
					break;
				}
				IBlockState state = world.getBlockState(spot);
				IPlantable seed = (IPlantable) stack.getItem();
				if (world.isAirBlock(spot.up()) && state.getBlock().canSustainPlant(state, world, spot, EnumFacing.UP, seed)) {
					world.setBlockState(spot.up(), seed.getPlant(world, spot.up()));
					stack.shrink(1);
				}
			}
		}
	}

}
