package com.covens.common.tile.tiles;

import java.util.UUID;

import javax.annotation.Nonnull;

import com.covens.api.mp.IMagicPowerConsumer;
import com.covens.common.Covens;
import com.covens.common.core.net.NetworkHandler;
import com.covens.common.core.net.messages.TarotMessage;
import com.covens.common.item.ModItems;
import com.covens.common.lib.LibGui;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import zabi.minecraft.minerva.common.tileentity.ModTileEntity;
import zabi.minecraft.minerva.common.utils.entity.PlayerHelper;

public class TileEntityTarotsTable extends ModTileEntity {

	private static final int READ_COST = 2000;
	private IMagicPowerConsumer altarTracker = IMagicPowerConsumer.CAPABILITY.getDefaultInstance();

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return false;
	}

	public void read(@Nonnull ItemStack tarotDeck, @Nonnull EntityPlayer reader) {
		if (!reader.world.isRemote) {
			if (this.checkDeck(tarotDeck) && this.altarTracker.drainAltarFirst(reader, this.pos, this.world.provider.getDimension(), READ_COST)) {
				reader.openGui(Covens.instance, LibGui.TAROT.ordinal(), reader.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
				EntityPlayerMP readee = (EntityPlayerMP) PlayerHelper.getPlayerAcrossDimensions(UUID.fromString(tarotDeck.getTagCompound().getString("read_id")));
				if (readee != null) {
					NetworkHandler.HANDLER.sendTo(new TarotMessage(readee), (EntityPlayerMP) reader);
				} else {
					reader.sendStatusMessage(new TextComponentTranslation("item.tarots.error_reading"), true);
				}
			} else {
				reader.sendStatusMessage(new TextComponentTranslation("item.tarots.error_reading"), true);
			}
		}
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == IMagicPowerConsumer.CAPABILITY) {
			return IMagicPowerConsumer.CAPABILITY.cast(this.altarTracker);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (capability == IMagicPowerConsumer.CAPABILITY) || super.hasCapability(capability, facing);
	}

	private boolean checkDeck(ItemStack tarotDeck) {
		return ((tarotDeck.getItem() == ModItems.tarots) && tarotDeck.hasTagCompound() && tarotDeck.getTagCompound().hasKey("read_id") && tarotDeck.getTagCompound().hasKey("read_name"));
	}

	@Override
	protected void writeAllModDataNBT(NBTTagCompound tag) {
		tag.setTag("altar", this.altarTracker.writeToNbt());
	}

	@Override
	protected void readAllModDataNBT(NBTTagCompound tag) {
		this.altarTracker.readFromNbt(tag.getCompoundTag("altar"));
	}

	@Override
	protected void writeModSyncDataNBT(NBTTagCompound tag) {

	}

	@Override
	protected void readModSyncDataNBT(NBTTagCompound tag) {

	}
}
