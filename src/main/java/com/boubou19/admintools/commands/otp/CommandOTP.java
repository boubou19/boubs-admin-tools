package com.boubou19.admintools.commands.otp;

import cofh.lib.util.helpers.EntityHelper;
import com.boubou19.admintools.AdminTools;
import com.boubou19.admintools.JsonManager;
import com.boubou19.admintools.PlayerCoords;
import com.boubou19.admintools.commands.base.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.DimensionManager;
import scala.actors.threadpool.Arrays;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CommandOTP extends CommandBase{
    public static final CommandOTP instance = new CommandOTP();

    public CommandOTP(){
        commandName = "otp";
        commandAliases = Arrays.asList(new String[]{"at_otp"});
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
        PlayerCoords offline = JsonManager.readOfflinePlayerCoords(offlineDataFile);


        EntityPlayerMP operator = (EntityPlayerMP) sender;
        PlayerCoords operatorCoords = new PlayerCoords((EntityPlayer) sender);
        if (!offline.sameCoordAs(operatorCoords)) {
            if (offline.dim != operatorCoords.dim) { //interdim tp
                operator.mountEntity((Entity) null);
                if (!DimensionManager.isDimensionRegistered(offline.dim)) {
                    AdminTools.log.error("attempted to tp player " + operator.getDisplayName() + " in dim " + offline.dim + " but the dim does not exist. Aborting offline tp.");
                    return;
                }
                EntityHelper.transferPlayerToDimension(operator, offline.dim, AdminTools.server.getConfigurationManager());
            }
            operator.setPositionAndUpdate((double) offline.x, (double) offline.y, (double) offline.z);
            IChatComponent message = new ChatComponentText("You have been teleported to " + arguments[0] + "'s offline location");
            operator.addChatMessage(message);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1){
            return JsonManager.getMatchedPlayers(AdminTools.configuration.PlayerOfflineDataPath, args[0]);
        }
        return null;
    }
}
