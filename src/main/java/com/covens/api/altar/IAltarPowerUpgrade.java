package com.covens.api.altar;

/**
 * This is the interface to be injected into your capability object.
 * It is used to signal to the altar that the object that has this 
 * implemented should increase the total power multiplier of an altar.<br><br>
 * This can be implemented on:<br>
 * - Tile Entities (will apply when the block is above the altar)<br>
 * - ItemStacks (will apply when the item is placed with the hotkey on an altar, like athames)<br>
 * - Blocks (Technically, the itemstack returned by Block#getItem(World, BlockPos, IBlockState))
 * @author zabi9
 */
public interface IAltarPowerUpgrade {
	/**
	 * @return the <i>additional</i> amount of multiplier for an altar<br><br>
	 * <b>Example:</b><br>when returning a 0.3 amount, an otherwise completely  
	 * unmodified altar will have a total multiplier of (1 + 0.3 = 1.3)<br><br>
	 * Typical amounts are 0.01 (very low) to 0.5 (very high, this is a 50% increase alone)
	 */
	public double getAmount();
}
