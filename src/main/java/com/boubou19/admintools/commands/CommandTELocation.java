package com.boubou19.admintools.commands;

import com.boubou19.admintools.AdminTools;
import com.google.common.base.Throwables;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandTELocation implements ICommand {

    public static CommandTELocation instance = new CommandTELocation();
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

        return "telocation";
    }

    public int getRequiredPermissionLevel(){
        return 2;
    }

    public boolean canCommandSenderUseCommand(ICommandSender commandSender){
        return commandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    public List getCommandAliases(){
        return Arrays.asList(new String[]{"at_telocation"});
    }

    private void getStats (ICommandSender sender, String dimID){
        getStats(sender, dimID, "");
    }
    private void getStats (ICommandSender sender, String dimID, String filter){
        boolean noFilter = filter.equals("");
        int dim = 0;
        try {
            dim = CommandBase.parseInt(sender, dimID);
        } catch (Throwable e) {
            sender.addChatMessage(new ChatComponentTranslation("admintools.command.synthax.error"));
            sender.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender)));
            Throwables.propagate(e);
        }

        World world = AdminTools.server.worldServerForDimension(dim);
        if (world == null) {
            throw new CommandException("admintools.command.worldNotFound");
        }
        Iterator iterator = world.loadedTileEntityList.iterator();
        List<String> tileEntityDataList = new ArrayList<>();

        while (iterator.hasNext()){

            TileEntity e = (TileEntity) iterator.next();
            String key = e.getClass().toString();

            String tileEntityData = Utils.buildString(new String[] {
                key, " at: ", Double.toString(e.xCoord), ", ", Double.toString(e.yCoord), " ,", Double.toString(e.zCoord)
            });
            if (noFilter) {
                tileEntityDataList.add(tileEntityData);
            }
            else{
                if (key.toLowerCase().contains(filter.toLowerCase())){
                    tileEntityDataList.add(tileEntityData);
                }
            }
        }

        IChatComponent chatMessage = new ChatComponentText("tile entity dump performed");
        sender.addChatMessage(chatMessage);
        AdminTools.writeToDedicatedLogFile(AdminTools.TELocationFile, tileEntityDataList);




    }

    public void processCommand(ICommandSender sender, String[] arguments) {

        if (arguments.length == 1) {
            getStats(sender, arguments[0]);
        }
        else if (arguments.length == 2){
            getStats(sender, arguments[0], arguments[1]);
        }
        else {
            sender.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender)));
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

