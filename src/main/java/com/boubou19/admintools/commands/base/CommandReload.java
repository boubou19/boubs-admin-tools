package com.boubou19.admintools.commands.base;

import com.boubou19.admintools.AdminTools;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

import java.util.Arrays;

public class CommandReload extends CommandBase {
    public static final CommandReload instance = new CommandReload();

    public CommandReload(){
        commandName = "reload";
        commandAliases = Arrays.asList(new String[]{"at_reload"});
    }
    public void processCommand(ICommandSender sender, String[] arguments) {
        AdminTools.configuration.loadConfiguration();
        sender.addChatMessage(new ChatComponentTranslation("admintools.config.reload.success"));
    }
}

