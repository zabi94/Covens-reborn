package com.covens.common.content.enchantments;

import com.covens.api.CovensAPI;
import com.covens.api.mp.PlayerMPExpander;
import com.covens.common.lib.LibMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class EnchantmentExtraMP extends BaublesEnchantment {

	private static final ResourceLocation expander_name = new ResourceLocation(LibMod.MOD_ID, "enchantment_extra_mp");

	protected EnchantmentExtraMP() {
		super("extra_mp", Rarity.RARE, 3);
	}

	@Override
	public void onEquipped(EntityPlayer player) {
		this.updateExpansion(player);
	}

	@Override
	public void onUnequipped(EntityPlayer player) {
		this.updateExpansion(player);
	}

	public void updateExpansion(EntityPlayer player) {
		int currentLevel = this.getMaxLevelOnPlayer(player);
		CovensAPI.getAPI().removeMPExpansion(expander_name, player);
		if (currentLevel > 0) {
			CovensAPI.getAPI().expandPlayerMP(new EnchantmentExpander(currentLevel), player);
		}
	}

	public static class EnchantmentExpander implements PlayerMPExpander {

		private int amount;

		public EnchantmentExpander(int level) {
			this.amount = (1 << level) * 100;
		}

		@Override
		public ResourceLocation getID() {
			return expander_name;
		}

		@Override
		public int getExtraAmount(EntityPlayer p) {
			return this.amount;
		}

	}
}
