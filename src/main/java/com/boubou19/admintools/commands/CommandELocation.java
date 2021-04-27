package com.boubou19.admintools.commands;

import com.boubou19.admintools.AdminTools;
import com.boubou19.admintools.commands.base.CommandBaseLocation;
import com.google.common.base.Throwables;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandELocation extends CommandBaseLocation{

    public static final ICommand instance = new CommandELocation();

    public CommandELocation(){
        dumpPath = AdminTools.ELocationFile;
        commandName = "elocation";
        commandAliases = Arrays.asList(new String[]{"at_elocation"});
        dumpType = "entity";
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
        Iterator iterator = world.loadedEntityList.iterator();
        List<String> entityDataList = new ArrayList<>();

        while (iterator.hasNext()){

            Entity e = (Entity) iterator.next();
            if (e instanceof EntityLivingBase) {
                e = (EntityLivingBase) e;
                String key = e.getClass().toString();
                AdminTools.log.info(key.toLowerCase()+", "+filter.toLowerCase());
                String entityData = Utils.buildString(new String[] {
                    key, " at: ", Double.toString(e.posX), ", ", Double.toString(e.posY), " ,", Double.toString(e.posZ)
                });
                if (noFilter){
                    entityDataList.add(entityData);
                } else{
                    if (key.toLowerCase().contains(filter.toLowerCase())){
                        entityDataList.add(entityData);
                    }
                }

            }
        }

        return entityDataList;
    }
}

