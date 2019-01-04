/**
 * This class was created by <ZaBi94> on Dec 10th, 2017.
 * It's distributed as part of Covens under
 * the MIT license.
 */

package com.covens.common.content.spell;

import com.covens.api.spell.ISpell.EnumSpellType;
import com.covens.common.content.spell.spells.SpellActivation;
import com.covens.common.content.spell.spells.SpellBindMob;
import com.covens.common.content.spell.spells.SpellBlink;
import com.covens.common.content.spell.spells.SpellCallThunderstorm;
import com.covens.common.content.spell.spells.SpellDestabilization;
import com.covens.common.content.spell.spells.SpellDisarming;
import com.covens.common.content.spell.spells.SpellInfuseLife;
import com.covens.common.content.spell.spells.SpellLesserBlinking;
import com.covens.common.content.spell.spells.SpellMagnet;
import com.covens.common.content.spell.spells.SpellPoke;
import com.covens.common.content.spell.spells.SpellSelfHeal;
import com.covens.common.content.spell.spells.SpellSlowness;
import com.covens.common.content.spell.spells.SpellWater;
import com.covens.common.lib.LibMod;

public class ModSpells {

	public static Spell magnet, poke, water, activation, slowness, lesser_blink, blink, explosion, 
	disarming, infuse_life, self_heal, call_storm, tame;

	public static void init() {
		magnet = new SpellMagnet(1, 0xa5cec9, EnumSpellType.PROJECTILE_BLOCK, "magnet", LibMod.MOD_ID);
		poke = new SpellPoke(1, 0x6d1e10, EnumSpellType.PROJECTILE_ALL, "poke", LibMod.MOD_ID);
		water = new SpellWater(2, 0x1644a7, EnumSpellType.PROJECTILE_BLOCK, "water", LibMod.MOD_ID);
		activation = new SpellActivation(1, 0x42b3bd, EnumSpellType.PROJECTILE_BLOCK, "activation", LibMod.MOD_ID);
		slowness = new SpellSlowness(10, 0x4d6910, EnumSpellType.PROJECTILE_ENTITY, "slowness", LibMod.MOD_ID);
		lesser_blink = new SpellLesserBlinking(5, 0x9042bd, EnumSpellType.INSTANT, "lesser_blink", LibMod.MOD_ID);
		blink = new SpellBlink(10, 0xcb33e7, EnumSpellType.PROJECTILE_BLOCK, "blink", LibMod.MOD_ID);
		explosion = new SpellDestabilization(12, 0x5e0505, EnumSpellType.PROJECTILE_ALL, "explosion", LibMod.MOD_ID);
		disarming = new SpellDisarming(15, 0xffbb7c, EnumSpellType.PROJECTILE_ALL, "disarming", LibMod.MOD_ID);
		infuse_life = new SpellInfuseLife(5, 0xf6546a, EnumSpellType.PROJECTILE_ALL, "infuse_life", LibMod.MOD_ID);
		self_heal = new SpellSelfHeal(4, 0xd20057, EnumSpellType.INSTANT, "self_heal", LibMod.MOD_ID);
		call_storm = new SpellCallThunderstorm(15, 0x000033, EnumSpellType.INSTANT, "call_storm", LibMod.MOD_ID);
		tame = new SpellBindMob(5, 0xFF0000, EnumSpellType.PROJECTILE_ENTITY, "tame", LibMod.MOD_ID);
		registerAll();
	}

	private static void registerAll() {
		Spell.SPELL_REGISTRY.registerAll(
				magnet, poke, water, activation, slowness, 
				lesser_blink, blink, explosion, disarming, 
				infuse_life, self_heal, call_storm, tame
				);
	}

}
