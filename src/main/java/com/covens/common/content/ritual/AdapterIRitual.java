package com.covens.common.content.ritual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.covens.api.ritual.IRitual;
import com.covens.common.lib.LibMod;
import com.covens.common.tile.tiles.TileEntityGlyph;
import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class AdapterIRitual implements IForgeRegistryEntry<AdapterIRitual> {

	public static final IForgeRegistry<AdapterIRitual> REGISTRY = new RegistryBuilder<AdapterIRitual>().setName(new ResourceLocation(LibMod.MOD_ID, "rituals")).setType(AdapterIRitual.class).setIDRange(0, 200).create();

	private List<List<ItemStack>> jei_cache;
	private IRitual ritual;

	public AdapterIRitual(IRitual iritual) {
		this.ritual = iritual;

		for (int i = 0; i < this.ritual.getInput().size(); i++) {
			Ingredient ing = this.ritual.getInput().get(i);
			if (ing.getMatchingStacks().length == 0) {
				throw new IllegalArgumentException("Ritual inputs must be valid: ingredient #" + i + " for " + this.ritual.getRegistryName() + " has no matching items");
			}
		}

		if (this.ritual.getInput().size() == 0) {
			throw new IllegalArgumentException("Cannot have an empty input in a ritual");
		}
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
		return this.ritual.getTime();
	}

	public NonNullList<ItemStack> getOutput(NonNullList<ItemStack> input, NBTTagCompound data) {
		return this.ritual.getOutput(input, data);
	}

	public boolean isValidInput(List<ItemStack> ground, boolean circles) {
		ArrayList<ItemStack> checklist = new ArrayList<ItemStack>(ground.size());
		for (ItemStack item : ground) {
			for (int j = 0; j < item.getCount(); j++) {
				ItemStack copy = item.copy();
				copy.setCount(1);
				checklist.add(copy);
			}
		}

		if (checklist.size() != this.ritual.getInput().size()) {
			return false;
		}
		ArrayList<Ingredient> removalList = new ArrayList<Ingredient>(this.ritual.getInput());

		for (ItemStack stack_on_ground : checklist) {
			Ingredient found = null;
			for (Ingredient ingredient : removalList) {
				if (ingredient.apply(stack_on_ground)) {
					found = ingredient;
					break;
				}
			}
			if (found == null) {
				return false;
			}
			removalList.remove(found);
		}
		if (!removalList.isEmpty()) {
			return false;
		}
		return circles;
	}

	public int getCircles() {
		return this.ritual.getCircles();
	}

	public int getRequiredStartingPower() {
		return this.ritual.getRequiredStartingPower();
	}

	public int getRunningPower() {
		return this.ritual.getRunningPower();
	}

	public NonNullList<Ingredient> getInput() {
		return this.ritual.getInput();
	}

	public List<List<ItemStack>> getJeiInput() {
		if (this.jei_cache == null) {
			this.generateCache();
		}
		return this.jei_cache;
	}

	private void generateCache() { // FIXME LibIngredients.anyDye has the wrong stack number
		this.jei_cache = Lists.newArrayList();
		HashMap<Ingredient, Integer> sizes = new HashMap<>();
		for (Ingredient i : this.getInput()) {
			if (sizes.containsKey(i)) {
				sizes.put(i, sizes.get(i) + 1);
			} else {
				sizes.put(i, 1);
			}
		}
		for (Ingredient i : sizes.keySet()) {
			List<ItemStack> l = Lists.newArrayList();
			for (ItemStack is : i.getMatchingStacks()) {
				ItemStack nis = is.copy();
				nis.setCount(sizes.get(i));
				l.add(nis);
			}
			this.jei_cache.add(l);
		}
	}

	public NonNullList<ItemStack> getOutputRaw() {
		return this.ritual.getOutputRaw();
	}

	@Override
	public AdapterIRitual setRegistryName(ResourceLocation name) {
		this.ritual.setRegistryName(name);
		return this;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return this.ritual.getRegistryName();
	}

	@Override
	public Class<AdapterIRitual> getRegistryType() {
		return AdapterIRitual.class;
	}

}
