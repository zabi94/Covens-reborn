package com.covens.common.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import com.covens.common.core.helper.Log;
import com.covens.common.core.helper.PlayerHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
public class EntitySyncHelper {

	private static HashMap<UUID, ArrayList<SyncTask<EntityPlayer>>> playerList = new HashMap<>();
	private static HashMap<UUID, ArrayList<SyncTask<EntityLivingBase>>> entityList = new HashMap<>();
	private static final String DATA_TAG = "covens:tasks";
	private static final Object playerLock = new Object();
	private static final Object entityLock = new Object();

	public static boolean executeOnPlayerAvailable(UUID playerUUID, SyncTask<EntityPlayer> action) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			throw new IllegalStateException("Don't invoke CreatureSyncHelper from client");
		}
		if (UUIDs.isNull(playerUUID)) {
			throw new IllegalStateException("You can only queue valid player UUIDs");
		}
		EntityPlayer p = PlayerHelper.getPlayerAcrossDimensions(playerUUID);
		if (p != null) {
			action.entityFound(p);
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

	public static boolean executeOnEntityAvailable(UUID uniqueID, SyncTask<EntityLivingBase> action) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			throw new IllegalStateException("Don't invoke CreatureSyncHelper from client");
		}
		if (UUIDs.isNull(uniqueID)) {
			throw new IllegalStateException("You can only queue valid UUIDs");
		}
		EntityLivingBase ent = (EntityLivingBase) getEntityAcrossDimensions(uniqueID);
		if (ent != null) {
			action.entityFound(ent);
			DimensionManager.getWorld(0).getMinecraftServer().addScheduledTask(action);
			return true;
		} else {
			synchronized (entityLock) {
				ArrayList<SyncTask<EntityLivingBase>> list;
				if (entityList.containsKey(uniqueID)) {
					list = entityList.get(uniqueID);
				} else {
					list = new ArrayList<SyncTask<EntityLivingBase>>();
					entityList.put(uniqueID, list);
				}
				list.add(action);
				DimensionManager.getWorld(0).getMapStorage().getOrLoadData(TaskData.class, DATA_TAG).markDirty();
			}
			return false;
		}
	}
	
	public static void cleanMessagesForEntity(UUID uniqueID) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			throw new IllegalStateException("Don't invoke CreatureSyncHelper from client");
		}
		EntityLivingBase ent = (EntityLivingBase) getEntityAcrossDimensions(uniqueID);
		if (ent == null) {
			synchronized (entityLock) {
				if (entityList.containsKey(uniqueID)) {
					entityList.get(uniqueID).clear();
				}
				DimensionManager.getWorld(0).getMapStorage().getOrLoadData(TaskData.class, DATA_TAG).markDirty();
			}
		}
	}

	@Nullable
	public static EntityLivingBase getEntityAcrossDimensions(UUID uniqueID) {
		for (WorldServer ws : DimensionManager.getWorlds()) {
			Entity e = ws.getEntityFromUuid(uniqueID);
			if (e != null) {
				if (e instanceof EntityLivingBase) {
					return (EntityLivingBase) e;
				} else {
					throw new IllegalStateException("Entity with UUID " + uniqueID + " is not an EntityLivingBase");
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
			ArrayList<SyncTask<EntityPlayer>> bufferList = new ArrayList<>();
			synchronized (playerLock) {
				if (playerList.containsKey(id)) {
					ArrayList<SyncTask<EntityPlayer>> list = playerList.get(id);
					bufferList.addAll(list);
					list.clear();
					playerList.remove(id);
					DimensionManager.getWorld(0).getMapStorage().getOrLoadData(TaskData.class, DATA_TAG).markDirty();
				}
			}
			bufferList.stream().forEach(task -> DimensionManager.getWorld(0).getMinecraftServer().addScheduledTask(task.entityFound((EntityLivingBase) evt.getEntity())));
		} else if (evt.getEntity() instanceof EntityLivingBase) {
			UUID id = evt.getEntity().getPersistentID();
			ArrayList<SyncTask<EntityLivingBase>> bufferList = new ArrayList<>();
			synchronized (entityLock) {
				if (entityList.containsKey(id)) {
					ArrayList<SyncTask<EntityLivingBase>> list = entityList.get(id);
					bufferList.addAll(list);
					list.clear();
					entityList.remove(id);
					DimensionManager.getWorld(0).getMapStorage().getOrLoadData(TaskData.class, DATA_TAG).markDirty();
				}
			}
			bufferList.stream().forEach(task -> DimensionManager.getWorld(0).getMinecraftServer().addScheduledTask(task.entityFound((EntityLivingBase) evt.getEntity())));
		}
	}

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load evt) {
		if (evt.getWorld().isRemote || (evt.getWorld().provider.getDimension() != 0)) {
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
			this.writeToNBT(tag);
			return tag;
		}
		
		protected SyncTask<T> entityFound(EntityLivingBase entity) {
			onEntityConnected(entity);
			return this;
		}

		protected abstract void onEntityConnected(EntityLivingBase entity);

		protected abstract void writeToNBT(NBTTagCompound tag);
	}

	public static class TaskData extends WorldSavedData {

		public TaskData() {
			super(DATA_TAG);
		}
		
		public TaskData(String data) {
			super(data);
		}

		@Override
		public void readFromNBT(NBTTagCompound tag) {
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
		private static void unpackEntities(HashMap<UUID, ArrayList<SyncTask<EntityLivingBase>>> entityListIn, NBTTagList tagList) {
			tagList.forEach(entTag -> {
				NBTTagCompound tag = (NBTTagCompound) entTag;
				UUID id = UUIDs.fromNBT(tag);
				NBTTagList tasks = tag.getTagList("tasks", NBT.TAG_COMPOUND);
				ArrayList<SyncTask<EntityLivingBase>> nlist = new ArrayList<>();
				tasks.forEach(base -> {
					NBTTagCompound nbt = (NBTTagCompound) base;
					String className = nbt.getString("covens:type");
					try {
						SyncTask<EntityLivingBase> task = (SyncTask<EntityLivingBase>) Class.forName(className).newInstance();
						task.deserializeNBT(nbt);
						nlist.add(task);
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				});
				entityListIn.put(id, nlist);
			});
		}

		@SuppressWarnings("unchecked")
		private static void unpackPlayers(HashMap<UUID, ArrayList<SyncTask<EntityPlayer>>> playerListIn, NBTTagList tagList) {
			tagList.forEach(entTag -> {
				NBTTagCompound tag = (NBTTagCompound) entTag;
				UUID id = UUIDs.fromNBT(tag);
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
				playerListIn.put(id, nlist);
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
				for (UUID id : playerList.keySet()) {
					NBTTagCompound playerContainer = UUIDs.toNBT(id);
					NBTTagList tasks = new NBTTagList();
					playerList.get(id).forEach(st -> tasks.appendTag(st.serializeNBT()));
					playerContainer.setTag("tasks", tasks);
					players.appendTag(playerContainer);
				}
			}
			synchronized (entityLock) {
				for (UUID id : entityList.keySet()) {
					NBTTagCompound entityContainer = UUIDs.toNBT(id);
					NBTTagList tasks = new NBTTagList();
					entityList.get(id).forEach(st -> tasks.appendTag(st.serializeNBT()));
					entityContainer.setTag("tasks", tasks);
					players.appendTag(entityContainer);
				}
			}
			return tag;
		}
	}
}
