package com.covens.common.core.net;

import com.covens.client.gui.GuiApiary;
import com.covens.client.gui.GuiDistillery;
import com.covens.client.gui.GuiOven;
import com.covens.client.gui.GuiTarots;
import com.covens.client.gui.GuiThreadSpinner;
import com.covens.common.container.ContainerApiary;
import com.covens.common.container.ContainerDistillery;
import com.covens.common.container.ContainerFake;
import com.covens.common.container.ContainerOven;
import com.covens.common.container.ContainerThreadSpinner;
import com.covens.common.lib.LibGui;
import com.covens.common.tile.tiles.TileEntityApiary;
import com.covens.common.tile.tiles.TileEntityDistillery;
import com.covens.common.tile.tiles.TileEntityOven;
import com.covens.common.tile.tiles.TileEntityTarotsTable;
import com.covens.common.tile.tiles.TileEntityThreadSpinner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		switch (LibGui.values()[ID]) {
			case APIARY:
				return tile instanceof TileEntityApiary ? new ContainerApiary(player.inventory, (TileEntityApiary) tile) : null;
			case OVEN:
				return tile instanceof TileEntityOven ? new ContainerOven(player.inventory, (TileEntityOven) tile) : null;
			case THREAD_SPINNER:
				return tile instanceof TileEntityThreadSpinner ? new ContainerThreadSpinner(player.inventory, (TileEntityThreadSpinner) tile) : null;
			case TAROT:
				return new ContainerFake();// No container
			case DISTILLERY:
				return tile instanceof TileEntityDistillery ? new ContainerDistillery(player.inventory, (TileEntityDistillery) tile) : null;
			default:
				return null;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		switch (LibGui.values()[ID]) {
			case APIARY:
				return tile instanceof TileEntityApiary ? new GuiApiary(player.inventory, (TileEntityApiary) tile) : null;
			case OVEN:
				return tile instanceof TileEntityOven ? new GuiOven((ContainerOven) this.getServerGuiElement(ID, player, world, x, y, z), player.inventory) : null;
			case THREAD_SPINNER:
				return tile instanceof TileEntityThreadSpinner ? new GuiThreadSpinner(player.inventory, (TileEntityThreadSpinner) tile) : null;
			case TAROT:
				return tile instanceof TileEntityTarotsTable ? new GuiTarots() : null;
			case DISTILLERY:
				return tile instanceof TileEntityDistillery ? new GuiDistillery((ContainerDistillery) this.getServerGuiElement(ID, player, world, x, y, z)) : null;
			default:
				return null;
		}
	}
}
