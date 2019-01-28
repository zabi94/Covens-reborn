package com.covens.api.ritual;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRitual {

	/**
	 * Override this to change the output given by {@link IRitual#getOutputRaw()}
	 * based on what was used, like damaging some of the input stacks.
	 * Use this to damage items, or return an empty container
	 *
	 * @param input the items used to trigger the ritual
	 * @param tag   the acessory tag
	 * @return an itemstack that reflects the changes (if any) made by the ritual to the default output
	 */
	default @Nonnull ItemStack modifyOutput(ItemStack originalOutput, NonNullList<ItemStack> input, NBTTagCompound tag) {
		return originalOutput;
	}

	/**
	 * This method is called every tick if the altar has not enough power to keep it
	 * running. This method is called in place of
	 * {@link #onUpdate(EntityPlayer, TileEntity, World, BlockPos, NBTTagCompound, int)}
	 *
	 * @param player            The player that activated the ritual, or null
	 * @param tile              the TileEntityGlyph performing the ritual
	 * @param world             the world the ritual is being performed into
	 * @param mainGlyphPos      the position of the tile
	 * @param effectivePosition the position where the ritual should take place
	 * @param covenSize         the size of the coven performing this ritual, player
	 *                          included
	 * @param data              the accessory tag
	 * @param ticks             how many ticks passed since activation
	 * @return true if the ritual should be stopped after this, false otherwise
	 */
	default boolean onLowPower(@Nullable EntityPlayer player, TileEntity tile, World world, BlockPos mainGlyphPos, NBTTagCompound data, int ticks, BlockPos effectivePosition, int covenSize) {
		return false;
	}

	/**
	 * This method gets called when the ritual is triggered by a player
	 *
	 * @param player            The player that activated the ritual, or null
	 * @param tile              The TileEntityGlyph performing the ritual
	 * @param world             The world the ritual is being performed into
	 * @param mainGlyphPos      The position of the tile
	 * @param data              The accessory tag
	 * @param effectivePosition the position where the ritual should take place
	 * @param covenSize         the size of the coven performing this ritual, player
	 *                          included
	 */
	default void onStarted(@Nullable EntityPlayer player, TileEntity tile, World world, BlockPos mainGlyphPos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		//Override
	}

	/**
	 * This method gets called when the ritual is stopped before completion by a
	 * player This method is never called if
	 * {@link #onFinish(EntityPlayer, TileEntity, World, BlockPos, NBTTagCompound)}
	 * is called
	 *
	 * @param player            The player that activated the ritual, or null
	 * @param tile              the TileEntityGlyph performing the ritual
	 * @param world             the world the ritual is being performed into
	 * @param mainGlyphPos      the position of the tile
	 * @param data              the accessory tag
	 * @param effectivePosition the position where the ritual should take place
	 * @param covenSize         the size of the coven performing this ritual, player
	 *                          included
	 */
	default void onStopped(@Nullable EntityPlayer player, TileEntity tile, World world, BlockPos mainGlyphPos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		//Override
	}

	/**
	 * This method gets called when the ritual time expires, before stopping
	 * automatically This method is never called if
	 * {@link #onStopped(EntityPlayer, TileEntity, World, BlockPos, NBTTagCompound)}
	 * is called
	 *
	 * @param player            The player that activated the ritual, or null
	 * @param tile              the TileEntityGlyph performing the ritual
	 * @param world             the world the ritual is being performed into
	 * @param mainGlyphPos      the position of the tile
	 * @param data              the accessory tag
	 * @param effectivePosition the position where the ritual should take place
	 * @param covenSize         the size of the coven performing this ritual, player
	 *                          included
	 */
	default void onFinish(@Nullable EntityPlayer player, TileEntity tile, World world, BlockPos mainGlyphPos, NBTTagCompound data, BlockPos effectivePosition, int covenSize) {
		//Override
	}

	/**
	 * This method gets called every tick since the ritual was activated, if it has
	 * enough power to run. If it doesn't,
	 * {@link #onLowPower(EntityPlayer, TileEntity, World, BlockPos, NBTTagCompound, int)}
	 * gets called instead
	 *
	 * @param player            The player that activated the ritual, or null
	 * @param tile              the TileEntityGlyph performing the ritual
	 * @param world             the world the ritual is being performed into
	 * @param mainGlyphPos      the position of the tile
	 * @param data              the accessory tag
	 * @param ticks             how many ticks passed since activation
	 * @param effectivePosition the position where the ritual should take place
	 * @param covenSize         the size of the coven performing this ritual, player
	 *                          included
	 */
	default void onUpdate(@Nullable EntityPlayer player, TileEntity tile, World world, BlockPos mainGlyphPos, NBTTagCompound data, int ticks, BlockPos effectivePosition, int covenSize) {
		//Override
	}

	/**
	 * This method is used to check other pre-conditions, different from the input
	 * items (dimension, activating player, nearby blocks, lunar phase...)
	 *
	 * @param player            The player that activated the ritual, or null
	 * @param world             the world the ritual is being performed into
	 * @param mainGlyphPos      the position of the tile
	 * @param recipe            the list of items used to trigger this ritual
	 * @param effectivePosition the position where the ritual should take place
	 * @param covenSize         the size of the coven performing this ritual, player
	 *                          included
	 * @return
	 */
	default boolean isValid(EntityPlayer player, World world, BlockPos mainGlyphPos, List<ItemStack> recipe, BlockPos effectivePosition, int covenSize) {
		return true;
	}

}
