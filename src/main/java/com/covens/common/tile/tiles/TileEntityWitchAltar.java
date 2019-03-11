package com.covens.common.tile.tiles;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.covens.api.altar.UpgradeCapabilities;
import com.covens.api.mp.DefaultMPContainer;
import com.covens.api.mp.MPContainer;
import com.covens.common.block.ModBlocks;
import com.covens.common.block.tiles.BlockWitchAltar;
import com.covens.common.block.tiles.BlockWitchAltar.AltarMultiblockType;
import com.covens.common.core.helper.Log;
import com.covens.common.item.tool.ItemAthame;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.Mod;
import zabi.minecraft.minerva.common.entity.UUIDs;
import zabi.minecraft.minerva.common.tileentity.ModTileEntity;

@Mod.EventBusSubscriber
public class TileEntityWitchAltar extends ModTileEntity implements ITickable {

	private static final int RIGHT_CLICK_RECALCULATION_COOLDOWN = 200;

	private List<UUID> swordItems = Lists.newArrayList();
	private double multiplier = 1;
	private DefaultMPContainer storage = new DefaultMPContainer(0);
	private AltarScanHelper scanHelper;
	private int gain = 1;
	private int recalcCooldown = 0;
	private EnumDyeColor color = EnumDyeColor.RED;

	public TileEntityWitchAltar() {
		this.scanHelper = new AltarScanHelper(this);
	}

	@Override
	public void onLoad() {
		if (!this.world.isRemote) {
			this.scanHelper.forceFullScan();
		}
	}

	public void scheduleUpgradeCheck() {
		this.scanHelper.scheduleUpgradeCheck();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void update() {
		if (!this.getWorld().isRemote) {
			this.scanHelper.scanNature();
			if (this.storage.getAmount() < this.storage.getMaxAmount()) {
				this.storage.fill(this.gain);
				this.markDirty();
			}
			if (this.recalcCooldown > 0) {
				this.recalcCooldown--;
			}
			if (this.getSwordIds().contains(ItemAthame.ATHAME_UUID)) {
				this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos).grow(5)).forEach(player -> {
					MPContainer playerMP = player.getCapability(MPContainer.CAPABILITY, null);
					int transferValue = Math.min(20, playerMP.getMaxAmount() - playerMP.getAmount());
					if (this.storage.drain(transferValue)) {
						playerMP.fill(transferValue / 10);
					}
				});
			}
		}
	}

	protected void refreshUpgrades() {
		this.gain = 1;
		this.multiplier = 1;
		this.swordItems.clear();

		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {

				BlockPos aps = this.getPos().add(dx, 0, dz);
				if (this.getWorld().getBlockState(aps).getBlock().equals(ModBlocks.witch_altar) && !this.getWorld().getBlockState(aps).getValue(BlockWitchAltar.ALTAR_TYPE).equals(AltarMultiblockType.UNFORMED)) {
					BlockPos ps = aps.up();
					try {
						if (!checkCapabilityAsTile(ps)) {
							checkCapabilityAsItem(ps);
						}
					} catch (NullPointerException e) {
						Log.e("Errored on "+ps);
					}
				}


			}
		}

		this.markDirty();
	}

	private boolean checkCapabilityAsTile(BlockPos ps) {
		TileEntity tile = world.getTileEntity(ps);
		if (tile == null) {
			return false;
		}
		boolean foundCaps = false;

		if (tile.hasCapability(UpgradeCapabilities.ALTAR_EFFECT_CAPABILITY, null)) {
			tile.getCapability(UpgradeCapabilities.ALTAR_EFFECT_CAPABILITY, null).onApply(world, pos);
			swordItems.add(tile.getCapability(UpgradeCapabilities.ALTAR_EFFECT_CAPABILITY, null).getIdentifier());
			foundCaps = true;
		}

		if (tile.hasCapability(UpgradeCapabilities.ALTAR_GAIN_CAPABILITY, null)) {
			this.gain += tile.getCapability(UpgradeCapabilities.ALTAR_GAIN_CAPABILITY, null).getAmount();
			foundCaps = true;
		}

		if (tile.hasCapability(UpgradeCapabilities.ALTAR_MULTIPLIER_CAPABILITY, null)) {
			this.multiplier += tile.getCapability(UpgradeCapabilities.ALTAR_MULTIPLIER_CAPABILITY, null).getAmount();
			foundCaps = true;
		}

		return foundCaps;
	}

	@SuppressWarnings("deprecation")
	private void checkCapabilityAsItem(BlockPos ps) {
		IBlockState state = world.getBlockState(ps);
		ItemStack stack = null;
		if (state.getBlock() == ModBlocks.placed_item) {
			stack = ((TileEntityPlacedItem) world.getTileEntity(ps)).getItem();
		} else {
			stack = state.getBlock().getItem(world, ps, state);
		}

		if (stack.hasCapability(UpgradeCapabilities.ALTAR_EFFECT_CAPABILITY, null)) {
			stack.getCapability(UpgradeCapabilities.ALTAR_EFFECT_CAPABILITY, null).onApply(world, pos);
			swordItems.add(stack.getCapability(UpgradeCapabilities.ALTAR_EFFECT_CAPABILITY, null).getIdentifier());
		}

		if (stack.hasCapability(UpgradeCapabilities.ALTAR_GAIN_CAPABILITY, null)) {
			this.gain += stack.getCapability(UpgradeCapabilities.ALTAR_GAIN_CAPABILITY, null).getAmount();
		}

		if (stack.hasCapability(UpgradeCapabilities.ALTAR_MULTIPLIER_CAPABILITY, null)) {
			this.multiplier += stack.getCapability(UpgradeCapabilities.ALTAR_MULTIPLIER_CAPABILITY, null).getAmount();
		}

	}

	public int getCurrentGain() {
		return this.gain;
	}

	public EnumDyeColor getColor() {
		return this.color;
	}

	public void setColor(EnumDyeColor newColor) {
		this.color = newColor;
		this.markDirty();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return (newState.getBlock() != ModBlocks.witch_altar) || (newState.getValue(BlockWitchAltar.ALTAR_TYPE) != AltarMultiblockType.TILE);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == MPContainer.CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == MPContainer.CAPABILITY) {
			return MPContainer.CAPABILITY.cast(this.storage);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	protected void writeAllModDataNBT(NBTTagCompound tag) {
		tag.setTag("mp", this.storage.saveNBTTag());
		tag.setInteger("gain", this.gain);
		final NBTTagList list = new NBTTagList();
		swordItems.forEach(id -> {
			list.appendTag(UUIDs.toNBT(id));
		});
		tag.setTag("swords", list);
		tag.setDouble("multiplier", this.multiplier);
		tag.setInteger("recalcCooldown", this.recalcCooldown);
		this.writeModSyncDataNBT(tag);
	}

	@Override
	protected void readAllModDataNBT(NBTTagCompound tag) {
		this.storage.loadFromNBT(tag.getCompoundTag("mp"));
		this.gain = tag.getInteger("gain");
		this.multiplier = tag.getDouble("multiplier");
		this.recalcCooldown = tag.getInteger("recalcCooldown");
		this.swordItems.clear();
		tag.getTagList("swords", NBT.TAG_COMPOUND).forEach(base -> swordItems.add(UUIDs.fromNBT((NBTTagCompound) base)));
		this.readModSyncDataNBT(tag);
	}

	@Override
	protected void writeModSyncDataNBT(NBTTagCompound tag) {
		tag.setInteger("color", this.color.ordinal());
	}

	@Override
	protected void readModSyncDataNBT(NBTTagCompound tag) {
		this.color = EnumDyeColor.values()[tag.getInteger("color")];
	}

	public void addSwordItem(UUID id) {
		swordItems.add(id);
	}

	public void forceFullScan() {
		if (this.recalcCooldown == 0) {
			this.scanHelper.forceFullScan();
			this.recalcCooldown = RIGHT_CLICK_RECALCULATION_COOLDOWN;
		}
	}

	public List<UUID> getSwordIds() {
		return this.swordItems;
	}

	public double getMultiplier() {
		return this.multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

}
