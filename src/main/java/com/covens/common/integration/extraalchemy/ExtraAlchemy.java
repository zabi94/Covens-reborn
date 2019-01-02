package com.covens.common.integration.extraalchemy;

import com.covens.api.CovensAPI;
import com.covens.common.content.cauldron.BrewVanilla;
import com.covens.common.lib.LibIngredients;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

public class ExtraAlchemy {

	@ObjectHolder(value = "extraalchemy:effect.magnetism")
	public static final Potion magnetism = null;

	public static void init() {
		if (magnetism != null) {
			CovensAPI.getAPI().registerBrewEffect(new BrewVanilla(magnetism), magnetism, LibIngredients.ingotIron);
		}
	}

}
