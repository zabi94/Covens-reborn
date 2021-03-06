package com.covens.common.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.covens.common.core.helper.NBTHelper;
import com.covens.common.item.ModItems;
import com.covens.common.item.magic.ItemTaglock;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandCreateTaglock extends CommandBase {

	private static final List<String> aliases = Arrays.asList("createTaglock");

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return "createTaglock";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/createTaglock <name> <UUID>";
	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if ((args.length != 1) && (args.length != 2)) {
			throw new WrongUsageException("commands.create_taglock.usage");
		} else if (sender instanceof EntityPlayer) {
			ItemStack stack = new ItemStack(ModItems.taglock);
			NBTHelper.setString(stack, ItemTaglock.TAGLOCK_ENTITY_NAME, args[0]);
			if (args.length == 2) {
				NBTHelper.setUniqueID(stack, ItemTaglock.TAGLOCK_ENTITY, UUID.fromString(args[1]));
			} else {
				boolean found = false;
				for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
					if (player.getName().equals(args[0])) {
						NBTHelper.setUniqueID(stack, ItemTaglock.TAGLOCK_ENTITY, player.getUniqueID());
						found = true;
						break;
					}
				}
				if (!found) {
					throw new WrongUsageException("commands.create_taglock.no_player.usage");
				}
			}
			((EntityPlayer) sender).addItemStackToInventory(stack);
		} else {
			throw new CommandException("commands.error.no_console");
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length == 1) {
			ArrayList<String> possibilities = new ArrayList<>();
			server.getPlayerList().getPlayers().stream().filter(player -> player.getName().startsWith(args[0])).forEach(player -> possibilities.add(player.getName()));
			if (!possibilities.isEmpty()) {
				return possibilities;
			}
		}
		return super.getTabCompletions(server, sender, args, targetPos);
	}
}
