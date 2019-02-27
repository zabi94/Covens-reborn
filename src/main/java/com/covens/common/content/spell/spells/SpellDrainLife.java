package com.covens.common.content.spell.spells;

import com.covens.api.transformation.DefaultTransformations;
import com.covens.common.content.spell.Spell;
import com.covens.common.content.transformation.CapabilityTransformation;
import com.covens.common.potion.ModPotions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SpellDrainLife extends Spell {

	public SpellDrainLife(int cost, int color, EnumSpellType type, String name, String mod_id) {
		super(cost, color, type, name, mod_id);
	}

	@Override
	public void performEffect(RayTraceResult rtrace, EntityLivingBase caster, World world) {
		caster.addPotionEffect(new PotionEffect(ModPotions.vampire_leech, 800, 0, false, true));
	}

	@Override
	public boolean canBeUsed(World world, BlockPos pos, EntityLivingBase caster) {
		return isPlayerAndVampire(caster) && caster.getActivePotionEffect(ModPotions.vampire_leech) == null;
	}
	
	public static boolean isPlayerAndVampire(EntityLivingBase entity) {
		return (entity instanceof EntityPlayer && entity.getCapability(CapabilityTransformation.CAPABILITY, null).getType().equals(DefaultTransformations.VAMPIRE));
	}
	
}
