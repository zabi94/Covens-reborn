package com.covens.common.core.net.messages;

import com.covens.common.block.ModBlocks;
import com.covens.common.tile.tiles.TileEntityPlacedItem;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.minerva.common.network.SimpleMessage;

public class PlaceHeldItemMessage extends SimpleMessage<PlaceHeldItemMessage> {

	public BlockPos clicked = null;

	public PlaceHeldItemMessage() {
	}

	public PlaceHeldItemMessage(BlockPos pos) {
		this.clicked = pos;
	}

	@Override
	public IMessage handleMessage(MessageContext context) {
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
			EntityPlayer p = context.getServerHandler().player;
			if (this.canReplace(p.world) && this.isFaceBelowSolid(p.world)) {
				p.world.setBlockState(this.clicked, ModBlocks.placed_item.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.fromAngle(p.rotationYaw)), 3);
				TileEntityPlacedItem te = (TileEntityPlacedItem) p.world.getTileEntity(this.clicked);
				te.setItem(p.getHeldItemMainhand().splitStack(1));
			}
		});
		return null;
	}

	private boolean canReplace(World world) {
		return world.getBlockState(this.clicked).getBlock().isReplaceable(world, this.clicked);
	}

	private boolean isFaceBelowSolid(World world) {
		return world.getBlockState(this.clicked.down()).getBlockFaceShape(world, this.clicked.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
	}

}
