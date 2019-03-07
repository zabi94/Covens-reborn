package com.covens.common.content.ritual.rituals;

import java.util.List;
import java.util.Optional;

import com.covens.api.ritual.IRitual;
import com.covens.api.transformation.DefaultTransformations;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.core.helper.Log;
import com.covens.common.world.BiomeChangingUtils.BiomeChangerWalker;
import com.covens.common.world.biome.ModBiomes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class RitualCreateVampireLair implements IRitual {

	private static final int RADIUS = 10;

	@Override
	public void onFinish(EntityPlayer player, TileEntity tile, World world, BlockPos circlePos, NBTTagCompound data, BlockPos effectivePos, int covenSize) {
		BiomeChangerWalker walker = new BiomeChangerWalker(Biome.getIdForBiome(ModBiomes.VAMPIRE_LAIR));
		MutableBlockPos mpos = new MutableBlockPos();
		int blocksUsed = 0;
		for (int i = this.getAvailableBlocks(player) < 317 ? 1 : RADIUS; i <= RADIUS; i++) {
			mpos.setPos(effectivePos.getX() - RADIUS, 0, effectivePos.getZ() - RADIUS);
			for (int x = -RADIUS; x <= RADIUS; x++) {
				for (int z = -RADIUS; z <= RADIUS; z++) {
					if ((((x * x) + (z * z)) <= (RADIUS * RADIUS)) && (this.getAvailableBlocks(player) > 0)) {
						if (walker.visit(world, mpos)) {
							blocksUsed++;
						}
					}
					mpos.move(EnumFacing.SOUTH);
				}
				mpos.move(EnumFacing.NORTH, (2 * RADIUS) + 1);
				mpos.move(EnumFacing.EAST);
			}
			if (this.getAvailableBlocks(player) == 0) {
				break;
			}
		}
		this.removeVampireLairAvailableBlocks(player, blocksUsed);
		walker.complete();
	}

	private int getAvailableBlocks(EntityPlayer player) {
		// TODO
		player.getName();// Shut codacy up
		return 30;
	}

	private void removeVampireLairAvailableBlocks(EntityPlayer player, int blocksUsed) {
		Log.i("Remove blocks from " + player.getName() + " pool: " + blocksUsed);
		// TODO
	}

	@Override
	public Optional<ITextComponent> isValid(EntityPlayer player, World world, BlockPos mainGlyphPos, List<ItemStack> recipe, BlockPos effectivePosition, int covenSize) {
		if ((player.getCapability(CapabilityTransformation.CAPABILITY, null).getType() != DefaultTransformations.VAMPIRE) || (this.getAvailableBlocks(player) <= 0)) {
			return Optional.of(new TextComponentTranslation("ritual.problem.not_vampire"));
		}
		return Optional.empty();
	}

}
