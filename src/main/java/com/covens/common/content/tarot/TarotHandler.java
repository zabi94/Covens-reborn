package com.covens.common.content.tarot;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.covens.api.divination.ITarot;
import com.covens.common.content.enchantments.ModEnchantments;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

public class TarotHandler {

	@ObjectHolder(value = "covens:silver_sword")
	public static final ITarot SILVER_SWORD = null;
	
	public static IForgeRegistry<ITarot> REGISTRY = null;
	private static final int MAX_CARDS_PER_READING = 5;

	private TarotHandler() {
		// NO-OP
	}

	public static void registerTarot(ITarot tarot) {
		REGISTRY.register(tarot);
	}

	public static ArrayList<TarotInfo> getTarotsForPlayer(EntityPlayer player) {

		ArrayList<TarotInfo> res = new ArrayList<TarotInfo>(5);
		if (SILVER_SWORD.isApplicableToPlayer(player)) {
			res.addAll(REGISTRY.getValuesCollection().parallelStream() //
					.filter(it -> player.getRNG().nextBoolean() && !it.equals(SILVER_SWORD)) //
					.map(it -> new TarotInfo(it, player)) //
					.collect(Collectors.toList()));
			while (res.size() >= MAX_CARDS_PER_READING) {
				res.remove(player.getRNG().nextInt(res.size() - 1));
			}
			res.add(new TarotInfo(SILVER_SWORD, player));
		} else {
			res.addAll(REGISTRY.getValuesCollection().parallelStream() //
					.filter(it -> it.isApplicableToPlayer(player) && !it.equals(SILVER_SWORD)) //
					.map(it -> new TarotInfo(it, player)) //
					.collect(Collectors.toList()));
			while (res.size() >= MAX_CARDS_PER_READING) {
				res.remove(player.getRNG().nextInt(res.size()));
			}
		}

		return res;
	}

	public static class TarotInfo {

		private ITarot tarot;
		private boolean reversed;
		private int number = -1;

		private TarotInfo(ITarot tarot, boolean reversed, int number) {
			this.tarot = tarot;
			this.reversed = reversed;
			if (number >= 0) {
				this.number = number;
			}
		}

		TarotInfo(ITarot tarot, EntityPlayer player) {
			this(tarot, tarot.isReversed(player), tarot.hasNumber(player) ? tarot.getNumber(player) : -1);
		}

		public static ArrayList<TarotInfo> fromBuffer(ByteBuf buf) {
			ArrayList<TarotInfo> res = new ArrayList<TarotInfo>(5);
			int size = buf.readInt();
			for (int i = 0; i < size; i++) {
				boolean reversed = buf.readBoolean();
				int num = buf.readInt();
				String name = ByteBufUtils.readUTF8String(buf);
				ITarot tarot = TarotHandler.REGISTRY.getValue(new ResourceLocation(name));
				TarotInfo ti = new TarotInfo(tarot, reversed, num);
				res.add(ti);
			}
			return res;
		}

		public boolean isReversed() {
			return this.reversed;
		}

		public boolean hasNumber() {
			return this.number >= 0;
		}

		public int getNumber() {
			return this.number;
		}

		public ResourceLocation getTexture() {
			return this.tarot.getTexture();
		}

		public String getTranslationKey() {
			return this.tarot.getTranslationKey();
		}

		public String getRegistryName() {
			return this.tarot.getRegistryName().toString();
		}

		@Override
		public String toString() {
			return this.getTranslationKey() + ", " + this.number + (this.reversed ? ", reversed" : "");
		}
	}

	public static boolean isPlayerProtected(EntityPlayer p) {
		return p.getRNG().nextInt(10) < ModEnchantments.occultation.getTotalLevelOnPlayer(p);
	}

}
