package com.covens.api.altar;

import java.util.UUID;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is the interface to be injected into your capability object.
 * It is used to signal to the altar that the object that has this 
 * implemented should carry extra effects.<br><br>
 * This can be implemented on:<br>
 * - Tile Entities (will apply when the block is above the altar)<br>
 * - ItemStacks (will apply when the item is placed with the hotkey on an altar, like athames)<br>
 * - Blocks (Technically, the itemstack returned by Block#getItem(World, BlockPos, IBlockState))
 * @author zabi9
 */
public interface IAltarSpecialEffect { 
	
	/**
	 * Called whenever the upgrade is discovered and added to the internal list
	 */
	public void onApply(World world, BlockPos position);

	/**
	 * @return the UUID of an effect to be added to the internal list to check if
	 * this was considered.
	 */
	public UUID getIdentifier();
	 
}
