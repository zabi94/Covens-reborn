package com.covens.common.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import com.covens.common.core.helper.Log;
import com.covens.common.core.helper.PlayerHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber
public class CreatureSyncHelper {

	private static HashMap<UUID, ArrayList<SyncTask<EntityPlayer>>> playerList = new HashMap<>();
	private static HashMap<UUID, ArrayList<SyncTask<EntityLiving>>> entityList = new HashMap<>();
	private static final String DATA_TAG = "covens:tasks";
	private static final Object playerLock = new Object();
	private static final Object entityLock = new Object();

	public static boolean executeOnPlayerAvailable(UUID playerUUID, SyncTask<EntityPlayer> action) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			throw new RuntimeException("Don't invoke CreatureSyncHelper from client");
		}
		EntityPlayer p = PlayerHelper.getPlayerAcrossDimensions(playerUUID);
		if (p != null) {
			DimensionManager.getWorld(0).getMinecraftServer().addScheduledTask(action);
			return true;
		} else {
			synchronized (playerLock) {
				ArrayList<SyncTask<EntityPlayer>> list;
				if (playerList.containsKey(playerUUID)) {
					list = playerList.get(playerUUID);
				} else {
					list = new ArrayList<SyncTask<EntityPlayer>>();
					playerList.put(playerUUID, list);
				}
				list.add(action);
				DimensionManager.getWorld(0).getMapStorage().getOrLoadData(TaskData.class, DATA_TAG).markDirty();
			}
			return false;
		}
	}
	
	public static boolean executeOnEntityLivingAvailable(UUID uniqueID, SyncTask<EntityLiving> action) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			throw new RuntimeException("Don't invoke CreatureSyncHelper from client");
		}
		EntityLiving ent = getEntityAcrossDimensions(uniqueID);
		if (ent != null) {
			DimensionManager.getWorld(0).getMinecraftServer().addScheduledTask(action);
			return true;
		} else {
			synchronized (entityLock) {
				ArrayList<SyncTask<EntityLiving>> list;
				if (entityList.containsKey(uniqueID)) {
					list = entityList.get(uniqueID);
				} else {
					list = new ArrayList<SyncTask<EntityLiving>>();
					entityList.put(uniqueID, list);
				}
				list.add(action);
				DimensionManager.getWorld(0).getMapStorage().getOrLoadData(TaskData.class, DATA_TAG).markDirty();
			}
			return false;
		}
	}

	@Nullable
	public static EntityLiving getEntityAcrossDimensions(UUID uniqueID) {
		for (WorldServer ws:DimensionManager.getWorlds()) {
			Entity e = ws.getEntityFromUuid(uniqueID);
			if (e != null) {
				if (e instanceof EntityLiving) {
					return (EntityLiving) e;
				} else {
					throw new IllegalStateException("Entity with UUID "+uniqueID+" is not an EntityLiving");
				}
			}
		}
		return null;
	}

	@SubscribeEvent
	public static void entityJoin(EntityJoinWorldEvent evt) {
		if (evt.getWorld().isRemote) {
			return;
		}
		if (evt.getEntity() instanceof EntityPlayerMP) {
			UUID id = EntityPlayer.getUUID(((EntityPlayerMP) evt.getEntity()).getGameProfile());
			synchronized (playerLock) {
				if (playerList.containsKey(id)) {
					ArrayList<SyncTask<EntityPlayer>> list = playerList.get(id);
					MinecraftServer server = DimensionManager.getWorld(0).getMinecraftServer();
					list.stream().forEach(task -> server.addScheduledTask(task));
					list.clear();
					playerList.remove(id);
					DimensionManager.getWorld(0).getMapStorage().getOrLoadData(TaskData.class, DATA_TAG).markDirty();
				}
			}
		} else if (evt.getEntity() instanceof EntityLiving) {
			UUID id = evt.getEntity().getPersistentID();
			synchronized (entityLock) {
				if (entityList.containsKey(id)) {
					ArrayList<SyncTask<EntityLiving>> list = entityList.get(id);
					MinecraftServer server = DimensionManager.getWorld(0).getMinecraftServer();
					list.stream().forEach(task -> server.addScheduledTask(task));
					list.clear();
					entityList.remove(id);
					DimensionManager.getWorld(0).getMapStorage().getOrLoadData(TaskData.class, DATA_TAG).markDirty();
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load evt) {
		if (evt.getWorld().isRemote || evt.getWorld().provider.getDimension() != 0) {
			return;
		}
		Log.i("Loading SyncTask data");
		WorldSavedData data = evt.getWorld().getMapStorage().getOrLoadData(TaskData.class, DATA_TAG);
		if (data == null) {
			Log.i("Creating new SyncTask storage");
			data = new TaskData();
			evt.getWorld().getMapStorage().setData(DATA_TAG, data);
			data.markDirty();
		}
	}
	
	public static abstract class SyncTask<T extends EntityLivingBase> implements Runnable, INBTSerializable<NBTTagCompound> {
		
		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("covens:type", this.getClass().getCanonicalName());
			writeToNBT(tag);
			return tag;
		}

		protected abstract void writeToNBT(NBTTagCompound tag);
	}
	
	public static class TaskData extends WorldSavedData {
		
		public TaskData() {
			super(DATA_TAG);
		}
		
		public TaskData(String s) {
			this();
		}

		@Override
		public void readFromNBT(NBTTagCompound tag) {
			Log.i("Reading from NBT:\n"+tag);
			synchronized (playerLock) {
				playerList = new HashMap<>();
				unpackPlayers(playerList, tag.getTagList("players", NBT.TAG_COMPOUND));
			}
			synchronized (entityLock) {
				entityList = new HashMap<>();
				unpackEntities(entityList, tag.getTagList("entities", NBT.TAG_COMPOUND));
			}
		}

		@SuppressWarnings("unchecked")
		private void unpackEntities(HashMap<UUID, ArrayList<SyncTask<EntityLiving>>> entityList, NBTTagList tagList) {
			tagList.forEach(entTag -> {
				NBTTagCompound tag = (NBTTagCompound) entTag;
				UUID id = new UUID(tag.getLong("msb"), tag.getLong("lsb"));
				NBTTagList tasks = tag.getTagList("tasks", NBT.TAG_COMPOUND);
				ArrayList<SyncTask<EntityLiving>> nlist = new ArrayList<>();
				tasks.forEach(base -> {
					NBTTagCompound nbt = (NBTTagCompound) base;
					String className = nbt.getString("covens:type");
					try {
						SyncTask<EntityLiving> task = (SyncTask<EntityLiving>) Class.forName(className).newInstance();
						task.deserializeNBT(nbt);
						nlist.add(task);
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				});
				entityList.put(id, nlist);
			});
		}

		@SuppressWarnings("unchecked")
		private void unpackPlayers(HashMap<UUID, ArrayList<SyncTask<EntityPlayer>>> playerList, NBTTagList tagList) {
			tagList.forEach(entTag -> {
				NBTTagCompound tag = (NBTTagCompound) entTag;
				UUID id = new UUID(tag.getLong("msb"), tag.getLong("lsb"));
				NBTTagList tasks = tag.getTagList("tasks", NBT.TAG_COMPOUND);
				ArrayList<SyncTask<EntityPlayer>> nlist = new ArrayList<>();
				tasks.forEach(base -> {
					NBTTagCompound nbt = (NBTTagCompound) base;
					String className = nbt.getString("covens:type");
					try {
						SyncTask<EntityPlayer> task = (SyncTask<EntityPlayer>) Class.forName(className).newInstance();
						task.deserializeNBT(nbt);
						nlist.add(task);
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				});
				playerList.put(id, nlist);
			});
		}

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound compound) {
			NBTTagCompound tag = new NBTTagCompound();
			NBTTagList players = new NBTTagList();
			NBTTagList entities = new NBTTagList();
			tag.setTag("players", players);
			tag.setTag("entities", entities);
			synchronized (playerLock) {
				for (UUID id:playerList.keySet()) {
					NBTTagCompound playerContainer = new NBTTagCompound();
					playerContainer.setLong("msb", id.getMostSignificantBits());
					playerContainer.setLong("lsb", id.getLeastSignificantBits());
					NBTTagList tasks = new NBTTagList();
					playerList.get(id).forEach(st -> tasks.appendTag(st.serializeNBT()));
					playerContainer.setTag("tasks", tasks);
					players.appendTag(playerContainer);
				}
			}
			synchronized (entityLock) {
				for (UUID id:entityList.keySet()) {
					NBTTagCompound entityContainer = new NBTTagCompound();
					entityContainer.setLong("msb", id.getMostSignificantBits());
					entityContainer.setLong("lsb", id.getLeastSignificantBits());
					NBTTagList tasks = new NBTTagList();
					entityList.get(id).forEach(st -> tasks.appendTag(st.serializeNBT()));
					entityContainer.setTag("tasks", tasks);
					players.appendTag(entityContainer);
				}
			}
			Log.i("Writing to NBT:\n"+tag);
			return tag;
		}
	}
}
