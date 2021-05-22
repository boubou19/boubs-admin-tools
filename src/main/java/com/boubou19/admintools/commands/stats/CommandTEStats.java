package com.boubou19.admintools.commands.stats;

import com.boubou19.admintools.AdminTools;
import com.boubou19.admintools.commands.Utils;
import com.boubou19.admintools.commands.base.CommandBaseStats;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import java.util.*;

public class CommandTEStats extends CommandBaseStats {

    public static CommandBaseStats instance = new CommandTEStats();

    public CommandTEStats() {
        dumpPath = AdminTools.configuration.TEStatsPath;
        commandName = "testats";
        commandAliases = Arrays.asList(new String[]{"at_testats"});
        dumpType = "tile entity stats";
        reloadPath();
    }

    @Override
    public void reloadPath(){
        dumpPath = AdminTools.configuration.TEStatsPath;
    }

    @Override
    protected List<String> getStats(ICommandSender sender, String dimID, String filter){
        boolean noFilter = filter.equals("");

        int dim = parseDim(sender, dimID);

        World world = AdminTools.server.worldServerForDimension(dim);

        if (world == null) {
            throw new CommandException("admintools.command.worldNotFound");
        }

        Iterator iterator = world.loadedTileEntityList.iterator();
        Map<String, Integer> TEStats = new TreeMap<String, Integer>();
        while (iterator.hasNext()){
            TileEntity te = (TileEntity) iterator.next();
            String key = te.getClass().toString();

            if (noFilter || (!noFilter && key.toLowerCase().contains(filter.toLowerCase()))) {
                int counter = TEStats.getOrDefault(key, 0);
                TEStats.put(key, counter + 1);
            }
        }

        List<String> statsOutput = new ArrayList<String>();


        for (Map.Entry<String, Integer> entry : TEStats.entrySet()){
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
