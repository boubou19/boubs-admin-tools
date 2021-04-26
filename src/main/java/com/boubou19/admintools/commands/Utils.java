package com.boubou19.admintools.commands;

import net.minecraft.command.ICommandSender;

public class Utils {
    public static String buildString(String[] stringArray){
        StringBuilder stringBuilder = new StringBuilder();
        for (String elem : stringArray){
            stringBuilder.append(elem);
        }
        return stringBuilder.toString();
    }

    public static boolean isServerSendingCommand(ICommandSender sender){
        return sender.getCommandSenderName().equals("Server") || sender.getCommandSenderName().equals("@");
    }
}
