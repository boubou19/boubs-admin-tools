package com.boubou19.admintools.commands.stats;

import com.boubou19.admintools.AdminTools;
import com.boubou19.admintools.Utils;
import com.boubou19.admintools.commands.base.CommandBaseStats;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandELocation extends CommandBaseStats {

    public static final CommandBaseStats instance = new CommandELocation();

    public CommandELocation(){
        dumpPath = AdminTools.configuration.ELocationPath;
        commandName = "elocation";
        commandAliases = Arrays.asList(new String[]{"at_elocation"});
        dumpType = "entity";
        reloadPath();
    }

    @Override
    public void reloadPath(){
        dumpPath = AdminTools.configuration.ELocationPath;
    }

    @Override
    protected List<String> getStats (ICommandSender sender, String dimID, String filter){
        boolean noFilter = filter.equals("");
        int dim = parseDim(sender, dimID);

        World world = AdminTools.server.worldServerForDimension(dim);
        if (world == null) {
            throw new CommandException("admintools.command.worldNotFound");
        }
        Iterator iterator = world.loadedEntityList.iterator();
        List<String> entityDataList = new ArrayList<>();

        while (iterator.hasNext()){

            Entity e = (Entity) iterator.next();
            String key = e.getClass().toString();
            AdminTools.log.info(key.toLowerCase()+", "+filter.toLowerCase());
            String entityData = Utils.buildString(new String[] {
                key, " at: ", Integer.toString((int) e.posX), " ", Integer.toString((int) e.posY), " ", Integer.toString((int) e.posZ)
            });
            if (noFilter){
                entityDataList.add(entityData);
            } else{
                if (key.toLowerCase().contains(filter.toLowerCase())){
                    entityDataList.add(entityData);
                }
            }


        }

        return entityDataList;
    }
}

