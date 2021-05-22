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

public class CommandOTPHere extends CommandBase{
    public static final CommandOTPHere instance = new CommandOTPHere();

    public CommandOTPHere(){
        commandName = "otphere";
        commandAliases = Arrays.asList(new String[]{"at_otphere"});
    }

    public void processCommand(ICommandSender sender, String[] arguments) {
        if (!(sender instanceof EntityPlayer)){
            sender.addChatMessage(new ChatComponentTranslation("this command must be executed in game"));
            return;
        }
        if (arguments.length != 1){
            sender.addChatMessage(new ChatComponentTranslation("this command must have exactly one parameter: the name of the offline player"));
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
        Path offlineDataFile = Paths.get(AdminTools.configuration.PlayerOfflineDataPath.toString(), targets.get(0) + ".json");
        JsonManager.writeOfflinePlayerCoords(offlineDataFile, new PlayerCoords((EntityPlayer) sender));
        sender.addChatMessage(new ChatComponentTranslation("Player "+targets.get(0)+"'s offline location has been set to your current location!"));

    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1){
            return JsonManager.getMatchedPlayers(AdminTools.configuration.PlayerOfflineDataPath, args[0]);
        }
        return null;
    }
}
