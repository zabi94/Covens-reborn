package com.covens.api;

import java.util.UUID;
import java.util.function.Supplier;

import com.covens.api.cauldron.IBrewEffect;
import com.covens.api.cauldron.IBrewModifier;
import com.covens.api.divination.IFortune;
import com.covens.api.hotbar.IHotbarAction;
import com.covens.api.incantation.IIncantation;
import com.covens.api.infusion.IInfusion;
import com.covens.api.mp.IMagicPowerExpander;
import com.covens.api.ritual.EnumGlyphType;
import com.covens.api.ritual.IRitual;
import com.covens.api.spell.ISpell;
import com.covens.api.transformation.ITransformation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;

// TODO Javadocs
public abstract class CovensAPI {

	private static CovensAPI INSTANCE;
	public EnumCreatureAttribute DEMON, SPIRIT;
	public BiomeDictionary.Type IMMUTABLE;
	
	public static final void setupAPI(CovensAPI api) {
		if (INSTANCE == null) {
			INSTANCE = api;
		} else {
			throw new IllegalStateException("Covens API already initialized");
		}
	}

	public static final CovensAPI getAPI() {
		if (INSTANCE != null) {
			return INSTANCE;
		}
		throw new IllegalStateException("Covens API not ready yet");
	}

	public abstract void registerHotbarAction(IHotbarAction action);

	public abstract void registerFortune(IFortune fortune);

	public abstract void registerIncantation(String name, IIncantation incantation);

	public abstract void registerSpell(ISpell spell);

	public abstract void registerInfusion(IInfusion infusion);

	public abstract IInfusion getPlayerInfusion(EntityPlayer player);

	public abstract void setPlayerInfusion(EntityPlayer player, IInfusion infusion);

	public abstract void setTypeAndLevel(EntityPlayer player, ITransformation type, int level, boolean isClient);

	/**
	 * @param player The player whose blood reserve is being modified
	 * @param amount The amount of blood to add (negative values will decrease the
	 *               total)
	 * @return <i>When adding</i> blood this will return true if the value changed
	 *         and false otherwise: this is <b>true</b> if there was even a little
	 *         bit of space in the pool, but the blood added was greater than the
	 *         amount that could be inserted, and <b>false</b> if the pool was
	 *         maxed.<br>
	 *         <i>When removing</i> blood this will return true if ALL the blood
	 *         requested was drained. If the amount drained is greater than the
	 *         amount available this will return false, and no blood will be drained
	 *         from the pool
	 * @throws UnsupportedOperationException if the player is not a vampire
	 */
	public abstract boolean addVampireBlood(EntityPlayer player, int amount);

	public abstract void registerCircleRitual(IRitual ritual);

	public abstract int getCirclesIntegerForRitual(EnumGlyphType small, EnumGlyphType medium, EnumGlyphType large);

	/**
	 * Register a new modifier for brews. This modifiers extend the idea of using
	 * glowstone/redstone to extend power and duration and allow you to register new
	 * types of modifiers for them. See {@link com.covens.api.cauldron.modifiers}
	 *
	 * @param modifier The modifier to register
	 */
	public abstract void registerBrewModifier(IBrewModifier modifier);

	/**
	 * Links a brew effect, a potion and a crafting ingredient together, for
	 * crafting and application on brew itemstacks
	 */
	public abstract void registerBrewEffect(IBrewEffect effect, Potion potion, Ingredient ingredient);

	public abstract Potion getPotionFromBrew(IBrewEffect effect);

	public abstract IBrewEffect getBrewFromPotion(Potion potion);

	public abstract void addSpinningThreadRecipe(ResourceLocation registryName, ItemStack output, Ingredient... inputs);

	public abstract void addOvenSmeltingRecipe(ResourceLocation registryName, ItemStack output, ItemStack byproduct, float byproductChance, Ingredient input);

	public abstract void registerFrostfireSmelting(ResourceLocation name, Ingredient input, Supplier<ItemStack> output);

	public abstract void expandPlayerMP(IMagicPowerExpander expander, EntityPlayer player);

	public abstract void removeMPExpansion(IMagicPowerExpander expander, EntityPlayer player);

	public abstract void removeMPExpansion(ResourceLocation expander, EntityPlayer player);

	public abstract void drainBloodFromEntity(EntityPlayer player, EntityLivingBase entity);

	public abstract boolean isValidFamiliar(Entity entity);
	
	public abstract boolean bindFamiliar(EntityLiving familiar, EntityPlayer player);

	public abstract void unbindFamiliar(EntityLiving familiar);
	
	public abstract void unbindFamiliar(UUID entity, UUID player);
}
