package com.covens.common.content.tarot;

import com.covens.api.infusion.DefaultInfusions;
import com.covens.api.mp.IMagicPowerContainer;
import com.covens.api.transformation.DefaultTransformations;
import com.covens.common.content.crystalBall.capability.CapabilityFortune;
import com.covens.common.content.familiar.FamiliarController;
import com.covens.common.content.infusion.capability.InfusionCapability;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.core.capability.familiar.CapabilityFamiliarOwner;
import com.google.common.base.Predicates;

public class ModTarots {

	private ModTarots() {
	}

	public static void init() {
		TarotHandler.registerTarot(new QuickTarot("enderman", p -> p.getCapability(InfusionCapability.CAPABILITY, null).getType() == DefaultInfusions.END, null, null));
		TarotHandler.registerTarot(new QuickTarot("diamonds", p -> p.getCapability(CapabilityFortune.CAPABILITY, null).getFortune() != null, p -> p.getCapability(CapabilityFortune.CAPABILITY, null).getFortune().isNegative(), null));
		TarotHandler.registerTarot(new QuickTarot("iron_golem", p -> p.getCapability(InfusionCapability.CAPABILITY, null).getType() == DefaultInfusions.OVERWORLD, null, null));
		TarotHandler.registerTarot(new QuickTarot("wither_skeleton", p -> p.getCapability(InfusionCapability.CAPABILITY, null).getType() == DefaultInfusions.NETHER, null, null));
		TarotHandler.registerTarot(new QuickTarot("star", p -> p.getCapability(InfusionCapability.CAPABILITY, null).getType() == DefaultInfusions.DREAM, null, null));
		TarotHandler.registerTarot(new QuickTarot("nitwit", p -> (p.getCapability(IMagicPowerContainer.CAPABILITY, null).getMaxAmount() / 800) > 0, null, p -> p.getCapability(IMagicPowerContainer.CAPABILITY, null).getMaxAmount() / 800));
		TarotHandler.registerTarot(new QuickTarot("moon", p -> (p.getCapability(CapabilityTransformation.CAPABILITY, null).getType() == DefaultTransformations.VAMPIRE) || (p.getCapability(CapabilityTransformation.CAPABILITY, null).getType() == DefaultTransformations.WEREWOLF), p -> p.getCapability(CapabilityTransformation.CAPABILITY, null).getType() == DefaultTransformations.WEREWOLF, p -> p.getCapability(CapabilityTransformation.CAPABILITY, null).getLevel()));
		TarotHandler.registerTarot(new QuickTarot("silver_sword", p -> p.getCapability(CapabilityTransformation.CAPABILITY, null).getType() == DefaultTransformations.HUNTER, p -> false, p -> p.getCapability(CapabilityTransformation.CAPABILITY, null).getLevel()));
		TarotHandler.registerTarot(new QuickTarot("cat", p -> p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).familiarCount > 0, Predicates.not(FamiliarController::hasFamiliarsInRange), p -> p.getCapability(CapabilityFamiliarOwner.CAPABILITY, null).familiarCount));
		
		TarotHandler.registerTarot(new QuickTarot("hermit", Predicates.alwaysFalse(), null, null));
		TarotHandler.registerTarot(new QuickTarot("ender_dragon", Predicates.alwaysFalse(), null, null));
		TarotHandler.registerTarot(new QuickTarot("evoker", Predicates.alwaysFalse(), null, null));
		TarotHandler.registerTarot(new QuickTarot("guardian", Predicates.alwaysFalse(), null, null));
		TarotHandler.registerTarot(new QuickTarot("illusioner", Predicates.alwaysFalse(), null, null));
		TarotHandler.registerTarot(new QuickTarot("stronghold", Predicates.alwaysFalse(), null, null));
		TarotHandler.registerTarot(new QuickTarot("sun", Predicates.alwaysFalse(), null, null));
		TarotHandler.registerTarot(new QuickTarot("world", Predicates.alwaysFalse(), null, null));
		TarotHandler.registerTarot(new QuickTarot("witch", Predicates.alwaysFalse(), null, null));
		TarotHandler.registerTarot(new QuickTarot("wither", Predicates.alwaysFalse(), null, null));
		TarotHandler.registerTarot(new QuickTarot("zombie", Predicates.alwaysFalse(), null, null));
		TarotHandler.registerTarot(new QuickTarot("mounts", Predicates.alwaysFalse(), null, null));
	}

}
