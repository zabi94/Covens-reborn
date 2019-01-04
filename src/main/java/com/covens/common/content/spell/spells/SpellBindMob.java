package com.covens.common.content.spell.spells;

import com.covens.api.CovensAPI;
import com.covens.common.content.spell.Spell;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class SpellBindMob extends Spell {

	public SpellBindMob(int cost, int color, EnumSpellType type, String name, String mod_id) {
		super(cost, color, type, name, mod_id);
	}

	@Override
	public void performEffect(RayTraceResult rtrace, EntityLivingBase caster, World world) {
		if (rtrace.typeOfHit == Type.ENTITY && CovensAPI.getAPI().isValidFamiliar(rtrace.entityHit)) {
			CovensAPI.getAPI().bindFamiliar(rtrace.entityHit, (EntityPlayer) caster);
		}
	}
	
	@Override
	public boolean canBeUsed(World world, BlockPos pos, EntityLivingBase caster) {
		return caster instanceof EntityPlayer;
	}

}
