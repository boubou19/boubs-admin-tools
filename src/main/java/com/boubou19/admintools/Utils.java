package com.boubou19.admintools;

import com.boubou19.admintools.AdminTools;
import net.minecraft.command.ICommandSender;

import java.util.Map;

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

    public static void displayMap(Map<String, String> map){
        for (Map.Entry<String, String> entry: map.entrySet()){
            AdminTools.log.info(entry.getKey()+":"+entry.getValue());
        }
    }
}
