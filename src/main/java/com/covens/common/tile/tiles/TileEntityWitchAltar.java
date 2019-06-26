package com.covens.common.tile.tiles;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.covens.api.mp.DefaultMPContainer;
import com.covens.api.mp.MPContainer;
import com.covens.common.block.ModBlocks;
import com.covens.common.block.tiles.BlockWitchAltar;
import com.covens.common.block.tiles.BlockWitchAltar.AltarMultiblockType;
import com.covens.common.core.capability.altar.AltarCapabilities;
import com.covens.common.core.statics.ModConfig;
import com.covens.common.item.tool.ItemAthame;
import com.covens.common.item.tool.ItemBoline;
import com.covens.common.item.tool.ItemColdIronSword;
import com.covens.common.item.tool.ItemSilverSword;
import com.covens.common.lib.LibIngredients;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.Mod;
import zabi.minecraft.minerva.common.entity.UUIDs;
import zabi.minecraft.minerva.common.tileentity.ModTileEntity;

@Mod.EventBusSubscriber
public class TileEntityWitchAltar extends ModTileEntity implements ITickable {

	private static final int RADIUS = 12;
	private static final int MAX_SCORE_PER_CATEGORY = 20;
	
	private List<UUID> swordItems = Lists.newArrayList();
	private double multiplier = 1;
	private DefaultMPContainer storage = new DefaultMPContainer(0);
	private int gain = 1;
	private int baseValue = 0;
	private int refreshTimer = 0;
	private EnumDyeColor color = EnumDyeColor.RED;

	public TileEntityWitchAltar() {
	}

	@Override
	public void onLoad() {
		if (!this.world.isRemote) {
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void update() {
		if (!this.getWorld().isRemote) {
			if (this.storage.getAmount() < this.storage.getMaxAmount()) {
				this.storage.fill(this.gain);
				this.markDirty();
			}
			if (--refreshTimer < 0) {
				refreshTimer = ModConfig.altar_scan_delay;
				scanNature();
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

	private void scanNature() {
		int radius_c = RADIUS;
		if (this.getSwordIds().contains(ItemBoline.BOLINE_UUID)) {
			radius_c += 2;
		}
		HashMap<Block, Integer> map = new HashMap<Block, Integer>();
		BlockPos.MutableBlockPos.getAllInBox(this.pos.getX() - radius_c, this.pos.getY() - radius_c/2, this.pos.getZ() - radius_c, this.pos.getX() + radius_c, this.pos.getY() + radius_c/2, this.pos.getZ() + radius_c).forEach(bp -> {
			int score = getPowerValue(bp, this.world);
			if (score > 0) {
				Block block = this.world.getBlockState(bp).getBlock();
				int currentScore = 0;
				if (map.containsKey(block)) {
					currentScore = map.get(block);
				}
				int max_score = MAX_SCORE_PER_CATEGORY;
				if (getSwordIds().contains(ItemColdIronSword.COLD_IRON_UUID)) {
					max_score += 10;
				}
				if (currentScore < max_score) {
					map.put(block, currentScore + score);
				}
			}
		});
		this.refreshUpgrades();
		int maxPower = map.values().parallelStream().reduce(0, (a, b) -> a + b);
		int varietyMultiplier = 40;
		if (getSwordIds().contains(ItemSilverSword.SILVER_UUID)) {
			varietyMultiplier = 50;
		}
		maxPower += (map.keySet().size() * varietyMultiplier); // Variety is the most important thing
		baseValue = maxPower;
		this.getCapability(MPContainer.CAPABILITY, null).setMaxAmount((int) (maxPower * this.getMultiplier()));
		map.clear();
		this.markDirty();
	}
	
	private static int getPowerValue(BlockPos add, World world) {
		IBlockState blockState = world.getBlockState(add);
		Block block = blockState.getBlock();
		if (block.equals(Blocks.AIR)) {
			return 0;
		}
		if (block instanceof IPlantable || block instanceof IGrowable) {
			return 30;
		}
		if (blockState.getMaterial() == Material.LEAVES) {
			return 8;
		}
		ItemStack stack = new ItemStack(blockState.getBlock());
		if (!stack.isEmpty()) {
			if (LibIngredients.anyLog.apply(stack)) {
				return 15;
			}
		}
		return 0;
	}

	public void refreshUpgrades() {
		this.gain = 1;
		this.multiplier = 1;
		this.swordItems.clear();
		List<Object> duplicationCheckList = Lists.newArrayList();
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				BlockPos aps = this.getPos().add(dx, 0, dz);
				if (this.getWorld().getBlockState(aps).getBlock().equals(ModBlocks.witch_altar) && !this.getWorld().getBlockState(aps).getValue(BlockWitchAltar.ALTAR_TYPE).equals(AltarMultiblockType.UNFORMED)) {
					BlockPos ps = aps.up();
					if (!checkCapabilityAsTile(ps, duplicationCheckList)) {
						checkCapabilityAsItem(ps, duplicationCheckList);
					}
				}
			}
		}
		if (gain < 1) {
			gain = 1;
		}
		if (multiplier < 0) {
			multiplier = 0;
		}
		this.getCapability(MPContainer.CAPABILITY, null).setMaxAmount((int) (baseValue * multiplier));
		this.markDirty();
	}

	private boolean checkCapabilityAsTile(BlockPos ps, List<Object> duplicationCheckList) {
		TileEntity tile = world.getTileEntity(ps);
		if (tile == null) {
			return false;
		}
		boolean foundCaps = false;

		if (!duplicationCheckList.contains(tile.getClass())) {
			if (tile.hasCapability(AltarCapabilities.ALTAR_EFFECT_CAPABILITY, null)) {
				tile.getCapability(AltarCapabilities.ALTAR_EFFECT_CAPABILITY, null).onApply(world, pos);
				swordItems.add(tile.getCapability(AltarCapabilities.ALTAR_EFFECT_CAPABILITY, null).getIdentifier());
				foundCaps = true;
			}

			if (tile.hasCapability(AltarCapabilities.ALTAR_GAIN_CAPABILITY, null)) {
				this.gain += tile.getCapability(AltarCapabilities.ALTAR_GAIN_CAPABILITY, null).getAmount();
				foundCaps = true;
			}

			if (tile.hasCapability(AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY, null)) {
				this.multiplier += tile.getCapability(AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY, null).getAmount();
				foundCaps = true;
			}
			
			if (foundCaps) {
				duplicationCheckList.add(tile.getClass());
			}
		}
		return foundCaps;
	}

	@SuppressWarnings("deprecation")
	private void checkCapabilityAsItem(BlockPos ps, List<Object> duplicationCheckList) {
		IBlockState state = world.getBlockState(ps);
		ItemStack stack = null;
		Object index = null;
		if (state.getBlock() == ModBlocks.placed_item) {
			stack = ((TileEntityPlacedItem) world.getTileEntity(ps)).getItem();
			index = stack.getItem();
		} else {
			stack = state.getBlock().getItem(world, ps, state);
			index = state.getBlock();
		}

		index = index.toString();
		
		if (!duplicationCheckList.contains(index)) {
			boolean added = false;
			if (stack.hasCapability(AltarCapabilities.ALTAR_EFFECT_CAPABILITY, null)) {
				stack.getCapability(AltarCapabilities.ALTAR_EFFECT_CAPABILITY, null).onApply(world, pos);
				swordItems.add(stack.getCapability(AltarCapabilities.ALTAR_EFFECT_CAPABILITY, null).getIdentifier());
				added = true;
			}

			if (stack.hasCapability(AltarCapabilities.ALTAR_GAIN_CAPABILITY, null)) {
				this.gain += stack.getCapability(AltarCapabilities.ALTAR_GAIN_CAPABILITY, null).getAmount();
				added = true;
			}

			if (stack.hasCapability(AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY, null)) {
				this.multiplier += stack.getCapability(AltarCapabilities.ALTAR_MULTIPLIER_CAPABILITY, null).getAmount();
				added = true;
			}
			if (added) {
				duplicationCheckList.add(index);
			}
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
		tag.setInteger("baseValue", baseValue);
		tag.setInteger("refreshTimer", refreshTimer);
		final NBTTagList list = new NBTTagList();
		swordItems.forEach(id -> {
			list.appendTag(UUIDs.toNBT(id));
		});
		tag.setTag("swords", list);
		tag.setDouble("multiplier", this.multiplier);
		this.writeModSyncDataNBT(tag);
	}

	@Override
	protected void readAllModDataNBT(NBTTagCompound tag) {
		this.storage.loadFromNBT(tag.getCompoundTag("mp"));
		this.gain = tag.getInteger("gain");
		this.multiplier = tag.getDouble("multiplier");
		this.swordItems.clear();
		this.baseValue = tag.getInteger("baseValue");
		this.refreshTimer = tag.getInteger("refreshTimer");
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
