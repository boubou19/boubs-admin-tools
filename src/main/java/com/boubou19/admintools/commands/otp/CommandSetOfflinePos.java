package com.boubou19.admintools.commands.otp;

import com.boubou19.admintools.AdminTools;
import com.boubou19.admintools.JsonManager;
import com.boubou19.admintools.PlayerCoords;
import com.boubou19.admintools.commands.base.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import scala.actors.threadpool.Arrays;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CommandSetOfflinePos extends CommandBase{
    public static final CommandSetOfflinePos instance = new CommandSetOfflinePos();

    public CommandSetOfflinePos() {
        commandName = "otp";
        commandAliases = Arrays.asList(new String[]{"at_otp"});
    }

    public void processCommand(ICommandSender sender, String[] arguments) {
        if (!(sender instanceof EntityPlayer)){
            sender.addChatMessage(new ChatComponentTranslation("this command must be executed in game"));
            return;
        }
        if (arguments.length != 5){
            sender.addChatMessage(new ChatComponentTranslation("this command must have exactly 5 parameter: the name of the offline player, the x,y,z coords and the dim number"));
            return ;
        }
        List<String> targets = JsonManager.getMatchedPlayers(AdminTools.configuration.PlayerOfflineDataPath, arguments[0]);
        if (targets.size()==0){
            sender.addChatMessage(new ChatComponentTranslation("the player " + arguments[0] + " has never logged in the server!"));
            return;
        }
        if (targets.size()!= 1){
            sender.addChatMessage(new ChatComponentTranslation(arguments[0]+" is ambiguous. Please specify full name of one of those players: "+String.join(", ", targets)));

            return;
        }
        int x, y, z, dim;
        try {
            x = net.minecraft.command.CommandBase.parseInt(sender, arguments[1]);
            y = net.minecraft.command.CommandBase.parseInt(sender, arguments[2]);
            z = net.minecraft.command.CommandBase.parseInt(sender, arguments[3]);
            dim = net.minecraft.command.CommandBase.parseInt(sender, arguments[4]);
        } catch (Throwable e) {
            sender.addChatMessage(new ChatComponentTranslation("admintools.command.synthax.error"));
            sender.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender)));
            return;
        }
        Path offlineDataFile = Paths.get(AdminTools.configuration.PlayerOfflineDataPath.toString(), targets.get(0) + ".json");
        PlayerCoords offline = new PlayerCoords(x, y, z, dim);
        JsonManager.writeOfflinePlayerCoords(offlineDataFile, offline);
        sender.addChatMessage(new ChatComponentTranslation("Player "+targets.get(0)+"'s offline location has been set to "+ x + " " + y + " " + z + " in dim "+dim ));

    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1){
            return JsonManager.getMatchedPlayers(AdminTools.configuration.PlayerOfflineDataPath, args[0]);
        }
        return null;
    }
}
