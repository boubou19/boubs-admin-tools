package com.boubou19.admintools.commands;

import com.boubou19.admintools.AdminTools;
import com.boubou19.admintools.commands.base.CommandBaseStats;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandTELocation extends CommandBaseStats {

    public static final CommandBaseStats instance = new CommandTELocation();

    public CommandTELocation(){
        dumpPath = AdminTools.configuration.TELocationPath;
        commandName = "telocation";
        commandAliases = Arrays.asList(new String[]{"at_telocation"});
        dumpType = "tile entity";
    }

    @Override
    protected List<String> getStats (ICommandSender sender, String dimID, String filter){
        boolean noFilter = filter.equals("");
        int dim = parseDim(sender, dimID);

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
                key, " at: ", Integer.toString(e.xCoord), " ", Integer.toString(e.yCoord), " ", Integer.toString(e.zCoord)
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

