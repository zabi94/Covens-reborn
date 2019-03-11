package com.covens.api.altar;

/**
 * This is the interface to be injected into your capability object.
 * It is used to signal to the altar that the object that has this 
 * implemented should increase the power recovery speed of an altar.<br><br>
 * This can be implemented on:<br>
 * - Tile Entities (will apply when the block is above the altar)<br>
 * - ItemStacks (will apply when the item is placed with the hotkey on an altar, like athames)<br>
 * - Blocks (Technically, the itemstack returned by Block#getItem(World, BlockPos, IBlockState))
 * @author zabi9
 */
public interface IAltarSpeedUpgrade {
	/**
	 * @return the amount of MP recovered per tick by the altar thanks to the object<br><br>
	 * Typical amounts are 1 (very low, like torches) to 6 (very high)
	 */
	public int getAmount();
}
