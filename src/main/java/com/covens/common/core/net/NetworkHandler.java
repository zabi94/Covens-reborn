package com.covens.common.core.net;

import com.covens.client.fx.ParticleF;
import com.covens.common.core.net.messages.EnergySync;
import com.covens.common.core.net.messages.EntityInternalBloodChanged;
import com.covens.common.core.net.messages.FetchFamiliarsMessage;
import com.covens.common.core.net.messages.InfusionChangedMessage;
import com.covens.common.core.net.messages.ParticleMessage;
import com.covens.common.core.net.messages.PlaceHeldItemMessage;
import com.covens.common.core.net.messages.PlayerFamiliarsDefinition;
import com.covens.common.core.net.messages.PlayerTransformationChangedMessage;
import com.covens.common.core.net.messages.PlayerUsedAbilityMessage;
import com.covens.common.core.net.messages.SelectFamiliar;
import com.covens.common.core.net.messages.SmokeSpawn;
import com.covens.common.core.net.messages.SpawnAngryParticlesAroundEntity;
import com.covens.common.core.net.messages.TarotMessage;
import com.covens.common.core.net.messages.WitchFireTP;
import com.covens.common.core.net.messages.WitchfireFlame;
import com.covens.common.lib.LibMod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import zabi.minecraft.minerva.common.network.SimpleMessage;


public final class NetworkHandler {

	public static final SimpleNetworkWrapper HANDLER = new SimpleNetworkWrapper(LibMod.MOD_ID);

	private static int nextId = 0;

	private NetworkHandler() {
	}

	public static void init() {
		HANDLER.registerMessage(ParticleMessage.ParticleMessageHandler.class, ParticleMessage.class, next(), Side.CLIENT);
		HANDLER.registerMessage(TarotMessage.TarotMessageHandler.class, TarotMessage.class, next(), Side.CLIENT);
		HANDLER.registerMessage(PlayerFamiliarsDefinition.Handler.class, PlayerFamiliarsDefinition.class, next(), Side.CLIENT);
		registerSimpleMessage(PlayerTransformationChangedMessage.class, next(), Side.CLIENT);
		registerSimpleMessage(EntityInternalBloodChanged.class, next(), Side.CLIENT);
		registerSimpleMessage(WitchfireFlame.class, next(), Side.CLIENT);
		registerSimpleMessage(EnergySync.class, next(), Side.CLIENT);
		registerSimpleMessage(InfusionChangedMessage.class, next(), Side.CLIENT);
		registerSimpleMessage(SmokeSpawn.class, next(), Side.CLIENT);
		registerSimpleMessage(SpawnAngryParticlesAroundEntity.class, next(), Side.CLIENT);

		registerSimpleMessage(PlayerUsedAbilityMessage.class, next(), Side.SERVER);
		registerSimpleMessage(WitchFireTP.class, next(), Side.SERVER);
		registerSimpleMessage(PlaceHeldItemMessage.class, next(), Side.SERVER);
		registerSimpleMessage(FetchFamiliarsMessage.class, next(), Side.SERVER);
		registerSimpleMessage(SelectFamiliar.class, next(), Side.SERVER);
	}

	private static <MSG extends SimpleMessage<MSG>> void registerSimpleMessage(Class<MSG> clazz, int id, Side side) {
		HANDLER.registerMessage(clazz, clazz, id, side);
	}

	public static void spawnParticle(ParticleF particleF, World world, double x, double y, double z, int amount, double xSpeed, double ySpeed, double zSpeed, int... args) {
		NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(world.provider.getDimension(), x, y, z, 10);
		HANDLER.sendToAllAround(new ParticleMessage(particleF, x, y, z, amount, xSpeed, ySpeed, zSpeed, args), point);
	}

	public static void sendTileUpdateNearbyPlayers(TileEntity tile) {
		final IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		tile.getWorld().notifyBlockUpdate(tile.getPos(), state, state, 8);
	}

	public static void updateToNearbyPlayers(World worldObj, BlockPos pos) {
		final IBlockState state = worldObj.getBlockState(pos);
		worldObj.notifyBlockUpdate(pos, state, state, 8);
	}

	public static int next() {
		nextId++;
		return nextId - 1;
	}
}
