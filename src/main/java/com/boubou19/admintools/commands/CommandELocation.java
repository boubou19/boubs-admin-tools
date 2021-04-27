package com.boubou19.admintools.commands;

import com.boubou19.admintools.AdminTools;
import com.google.common.base.Throwables;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import java.util.*;

public class CommandELocation implements ICommand {

    public static CommandTEStats instance = new CommandTEStats();

    public int compareTo(ICommand p_compareTo_1_)
    {
        return this.getCommandName().compareTo(p_compareTo_1_.getCommandName());
    }

    public int compareTo(Object p_compareTo_1_)
    {
        return this.compareTo((ICommand)p_compareTo_1_);
    }

    public String getCommandUsage(ICommandSender commandSender) {
        return "admintools.command." + getCommandName() + ".syntax";
    }

    public String getCommandName() {

        return "elocation";
    }

    public int getRequiredPermissionLevel(){
        return 2;
    }

    public boolean canCommandSenderUseCommand(ICommandSender commandSender){
        return commandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    public List getCommandAliases(){
        return Arrays.asList(new String[]{"at_elocation"});
    }

    private void getStats (ICommandSender sender, String dimID){

        int dim = 0;
        try {
            dim = CommandBase.parseInt(sender, dimID);
        } catch (Throwable e) {
            sender.addChatMessage(new ChatComponentTranslation("admintools.command.synthax.error"));
            sender.addChatMessage(new ChatComponentTranslation("admintools.command." + getCommandName() + ".syntax"));
            Throwables.propagate(e);
        }

        World world = AdminTools.server.worldServerForDimension(dim);
        if (world == null) {
            throw new CommandException("admintools.command.worldNotFound");
        }
        Iterator iterator = world.loadedEntityList.iterator();
        List<String> entityDataList = new ArrayList<>();

        while (iterator.hasNext()){

             Entity e = (Entity) iterator.next();
            if (e instanceof EntityLivingBase) {
                e = (EntityLivingBase) e;
                String key = e.getClass().toString();
                String entityData = Utils.buildString(new String[] {
                    key, " at: ", Double.toString(e.posX), ", ", Double.toString(e.posY), " ,", Double.toString(e.posZ)
                });
                entityDataList.add(entityData);
            }
        }

        IChatComponent chatMessage = new ChatComponentText("entity dump performed");
        sender.addChatMessage(chatMessage);
        AdminTools.writeToDedicatedLogFile(AdminTools.ELocationFile, entityDataList);




    }

    public void processCommand(ICommandSender sender, String[] arguments) {

        if (arguments.length != 1) {
            sender.addChatMessage(new ChatComponentTranslation("admintools.command." + getCommandName() + ".syntax"));
        } else {
            getStats(sender, arguments[0]);
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {

        if (args.length == 1) {
            List<String> worldIDs = new ArrayList<String>();
            for (World world : AdminTools.server.worldServers) {
                worldIDs.add(Integer.toString(world.provider.dimensionId));
            }
            return CommandBase.getListOfStringsMatchingLastWord(args, worldIDs.toArray(new String[] { "" }));
        }
        return null;
    }

    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
    {
        return false;
    }
}

