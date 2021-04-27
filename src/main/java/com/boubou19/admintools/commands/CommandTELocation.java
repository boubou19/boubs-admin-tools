package com.boubou19.admintools.commands;

import com.boubou19.admintools.AdminTools;
import com.boubou19.admintools.commands.base.CommandBaseLocation;
import com.google.common.base.Throwables;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandTELocation extends CommandBaseLocation {

    public CommandTELocation(){
        dumpPath = AdminTools.TELocationFile;
        commandName = "telocation";
        commandAliases = Arrays.asList(new String[]{"at_telocation"});
        dumpType = "tile entity";
    }

    @Override
    protected List<String> getStats (ICommandSender sender, String dimID, String filter){
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

        return tileEntityDataList;
    }
}

