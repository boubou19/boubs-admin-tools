package com.boubou19.admintools.commands.stats;

import com.boubou19.admintools.AdminTools;
import com.boubou19.admintools.Utils;
import com.boubou19.admintools.commands.base.CommandBaseStats;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import java.util.*;

public class CommandEStats extends CommandBaseStats {

    public static CommandBaseStats instance = new CommandEStats();

    public CommandEStats(){
        dumpPath = AdminTools.configuration.EStatsPath;
        commandName = "estats";
        commandAliases = Arrays.asList(new String[]{"at_estats"});
        dumpType = "entity stats";
        reloadPath();
    }

    @Override
    public void reloadPath(){
        dumpPath = AdminTools.configuration.EStatsPath;
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
        Map<String, Integer> EStats = new TreeMap<String, Integer>();
        while (iterator.hasNext()){
            Entity e = (Entity) iterator.next();
            String key = e.getClass().toString();

            if (noFilter || (!noFilter && key.toLowerCase().contains(filter.toLowerCase()))) {
                int counter = EStats.getOrDefault(key, 0);
                EStats.put(key, counter + 1);
            }
        }

        List<String> statsOutput = new ArrayList<String>();


        for (Map.Entry<String, Integer> entry : EStats.entrySet()){
            String stat = Utils.buildString(new String[]{entry.getKey(), ": ", Integer.toString(entry.getValue())});
            if (!Utils.isServerSendingCommand(sender)){
                IChatComponent chatMessage = new ChatComponentText(stat);
                sender.addChatMessage(chatMessage);
            }

            statsOutput.add(stat);
        }

        return statsOutput;
    }
}

