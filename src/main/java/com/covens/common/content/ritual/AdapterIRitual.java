package com.covens.common.content.ritual;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.covens.api.CovensAPI;
import com.covens.api.ritual.EnumGlyphType;
import com.covens.api.ritual.IRitual;
import com.covens.common.lib.LibMod;
import com.covens.common.tile.tiles.TileEntityGlyph;
import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import zabi.minecraft.minerva.common.crafting.InputCounter;
import zabi.minecraft.minerva.common.crafting.NumberedInput;

public class AdapterIRitual implements IForgeRegistryEntry<AdapterIRitual> {

	public static final IForgeRegistry<AdapterIRitual> REGISTRY = new RegistryBuilder<AdapterIRitual>().setName(new ResourceLocation(LibMod.MOD_ID, "rituals")).setType(AdapterIRitual.class).setIDRange(0, 200).create();

	private List<List<ItemStack>> jei_cache;
	private int time, altarStartingPower, tickPower;
	private NonNullList<ItemStack> output  = NonNullList.create();
	private NonNullList<NumberedInput> input = NonNullList.create();
	private EnumGlyphType small, medium, big;
	private boolean allowRemote = true;
	
	private ResourceLocation registryName;
	private IRitual ritual;

	public AdapterIRitual(IRitual iritual) {
		this.ritual = iritual;
	}

	public static NonNullList<ItemStack> getItemsUsedForInput(NBTTagCompound tag) {
		NonNullList<ItemStack> list = NonNullList.create();
		NBTTagList tagList = tag.getTagList("itemsUsed", NBT.TAG_COMPOUND);
		tagList.forEach(nbt -> {
			NBTTagCompound itemTag = (NBTTagCompound) nbt;
			list.add(new ItemStack(itemTag));
		});
		return list;
	}

	public boolean isValid(EntityPlayer player, World world, BlockPos pos, List<ItemStack> recipe, BlockPos effectivePosition, int covenSize) {
		return this.ritual.isValid(player, world, pos, recipe, effectivePosition, covenSize);
	}

	public void onUpdate(@Nullable EntityPlayer player, TileEntityGlyph tile, World world, BlockPos pos, NBTTagCompound data, int ticks, BlockPos effectivePosition, int covenSize) {
		this.ritual.onUpdate(player, tile, world, pos, data, ticks, effectivePosition, covenSize);
	}

	public void onFinish(@Nullable EntityPlayer player, TileEntityGlyph tile, World world, BlockPos pos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		this.ritual.onFinish(player, tile, world, pos, data, effectivePosition, covenSize);
	}

	public void onStopped(@Nullable EntityPlayer player, TileEntityGlyph tile, World world, BlockPos pos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		this.ritual.onStopped(player, tile, world, pos, data, effectivePosition, covenSize);
	}

	public void onStarted(@Nullable EntityPlayer player, TileEntityGlyph tile, World world, BlockPos pos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		this.ritual.onStarted(player, tile, world, pos, data, effectivePosition, covenSize);
	}

	public boolean onLowPower(@Nullable EntityPlayer player, TileEntityGlyph tile, World world, BlockPos pos, NBTTagCompound data, int ticks, BlockPos effectivePosition, int covenSize) {
		return this.ritual.onLowPower(player, tile, world, pos, data, ticks, effectivePosition, covenSize);
	}

	public int getTime() {
		return time;
	}

	public NonNullList<ItemStack> getOutput(NonNullList<ItemStack> input, NBTTagCompound data) {
		NonNullList<ItemStack> result = NonNullList.withSize(output.size(), ItemStack.EMPTY);
		for (int i = 0; i < output.size(); i++) {
			result.set(i, ritual.modifyOutput(output.get(i).copy(), input, data));
		}
		return result;
	}

	public boolean isValidInput(List<ItemStack> ground, boolean circles) {
		
		List<InputCounter> counters = input.parallelStream().map(ni -> ni.getCounter()).collect(Collectors.toList());
		for (ItemStack is:ground) {
			boolean found = false;
			for (InputCounter ic:counters) {
				if (ic.count(is)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		for (InputCounter ic:counters) {
			if (!ic.isExact()) {
				return false;
			}
		}
		return circles;
	}
	
	@SideOnly(Side.CLIENT)
	public void spawnParticles(World world, BlockPos glyphPos, BlockPos effectivePos, Random rng) {
		this.ritual.onRandomDisplayTick(world, glyphPos, effectivePos, rng);
	}

	public int getCircles() {
		return CovensAPI.getAPI().getCirclesIntegerForRitual(small, medium, big);
	}

	public int getRequiredStartingPower() {
		return altarStartingPower;
	}

	public int getRunningPower() {
		return tickPower;
	}

	public NonNullList<NumberedInput> getInput() {
		return input;
	}
	
	public boolean canBeRemote() {
		return allowRemote;
	}

	public List<List<ItemStack>> getJeiInput() {
		if (this.jei_cache == null) {
			this.jei_cache = Lists.newArrayList();
			for (NumberedInput ni:input) {
				jei_cache.add(ni.getCachedStacks());
			}
		}
		return this.jei_cache;
	}

	public NonNullList<ItemStack> getOutputRaw() {
		return output;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void setAltarStartingPower(int altarStartingPower) {
		this.altarStartingPower = altarStartingPower;
	}

	public void setTickPower(int tickPower) {
		this.tickPower = tickPower;
	}

	public void setOutput(NonNullList<ItemStack> output) {
		this.output = output;
	}

	public void setInput(NonNullList<NumberedInput> input) {
		this.input = input;
	}

	public void setAllowRemote(boolean allowRemote) {
		this.allowRemote = allowRemote;
	}
	
	public void setCircles(EnumGlyphType small, EnumGlyphType medium, EnumGlyphType big) {
		this.small = small;
		this.medium = medium;
		this.big = big;
	}

	@Override
	public AdapterIRitual setRegistryName(ResourceLocation name) {
		registryName = name;
		return this;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return registryName;
	}

	@Override
	public Class<AdapterIRitual> getRegistryType() {
		return AdapterIRitual.class;
	}

}
