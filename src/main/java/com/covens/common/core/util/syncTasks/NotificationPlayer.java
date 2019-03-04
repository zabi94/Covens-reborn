package com.covens.common.core.util.syncTasks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import zabi.minecraft.minerva.common.entity.synchronization.SyncTask;

public class NotificationPlayer extends SyncTask<EntityPlayer> {
	
	private String content;
	private boolean actionBar;
	
	public NotificationPlayer() {
		// Required
	}

	public NotificationPlayer(ITextComponent text, boolean actionBar) {
		content = ITextComponent.Serializer.componentToJson(text);
		this.actionBar = actionBar;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		content = nbt.getString("content");
		actionBar = nbt.getBoolean("actionBar");
	}

	@Override
	protected void execute(EntityPlayer player) {
		ITextComponent text = ITextComponent.Serializer.jsonToComponent(content);
		player.sendStatusMessage(text, actionBar);
	}

	@Override
	protected void writeToNBT(NBTTagCompound nbt) {
		nbt.setString("content", content);
		nbt.setBoolean("actionBar", actionBar);
	}

}
