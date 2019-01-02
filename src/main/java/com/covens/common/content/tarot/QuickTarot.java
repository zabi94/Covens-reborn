package com.covens.common.content.tarot;

import java.util.function.Function;
import java.util.function.Predicate;

import com.covens.api.divination.ITarot;
import com.covens.common.lib.LibMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class QuickTarot implements ITarot {

	private ResourceLocation rl;
	private ResourceLocation regName = null;
	private String unlocalizedName;
	private Predicate<EntityPlayer> apply, reverse;
	private Function<EntityPlayer, Integer> getNum;

	public QuickTarot(String name, Predicate<EntityPlayer> apply, Predicate<EntityPlayer> reverse, Function<EntityPlayer, Integer> getNum) {
		validateAll(name, apply);
		this.unlocalizedName = "tarot." + name + ".name";
		this.rl = new ResourceLocation(LibMod.MOD_ID, "textures/gui/tarots/" + name + ".png");
		this.reverse = reverse;
		if (reverse == null) {
			this.reverse = p -> p.getRNG().nextBoolean();
		}
		this.getNum = getNum;
		if (getNum == null) {
			this.getNum = p -> -1;
		}
		this.apply = apply;
		this.setRegistryName(new ResourceLocation(LibMod.MOD_ID, name));
	}

	private static final void validateAll(Object... objects) {
		for (Object o : objects) {
			if (o == null) {
				throw new IllegalArgumentException("Arguments cannot be null");
			}
		}
	}

	@Override
	public ResourceLocation getTexture() {
		return this.rl;
	}

	@Override
	public boolean isApplicableToPlayer(EntityPlayer player) {
		return this.apply.test(player);
	}

	@Override
	public boolean hasNumber(EntityPlayer player) {
		return this.getNum.apply(player) >= 0;
	}

	@Override
	public boolean isReversed(EntityPlayer player) {
		return this.reverse.test(player);
	}

	@Override
	public String getTranslationKey() {
		return this.unlocalizedName;
	}

	@Override
	public int getNumber(EntityPlayer player) {
		return this.getNum.apply(player);
	}

	@Override
	public ITarot setRegistryName(ResourceLocation nameIn) {
		this.regName = nameIn;
		return this;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return this.regName;
	}

	@Override
	public Class<ITarot> getRegistryType() {
		return ITarot.class;
	}

}
