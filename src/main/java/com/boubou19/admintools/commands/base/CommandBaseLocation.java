package com.boubou19.admintools.commands.base;

import com.boubou19.admintools.AdminTools;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CommandBaseLocation extends CommandBase {
    protected String dumpType;

    protected void setDumpType(String dumpType) {
        this.dumpType = dumpType;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] arguments) {
        if (arguments.length == 1) {
            List<String> locations = getStats(sender, arguments[0]);
            if (locations != null) {
                writeDump(locations, sender, dumpType);
            }
            else{
                sender.addChatMessage(new ChatComponentTranslation("locations is null, tell boubou the command name"));
            }
        } else if (arguments.length == 2) {
            List<String> locations = getStats(sender, arguments[0], arguments[1]);
            if (locations != null) {
                writeDump(locations, sender, dumpType);
            }
            else{
                sender.addChatMessage(new ChatComponentTranslation("locations is null, tell boubou the command name"));
            }
        } else {
            sender.addChatMessage(new ChatComponentTranslation(getCommandUsage(sender)));
        }
    }

    protected void writeDump(List<String> dataList, ICommandSender sender, String dumpType){
        writeDump(dataList);
        IChatComponent chatMessage = new ChatComponentText(dumpType + " dump performed");
        sender.addChatMessage(chatMessage);
    }


    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] arguments){
        if (arguments.length == 1) {
            List<String> worldIDs = new ArrayList<String>();
            for (World world : AdminTools.server.worldServers) {
                worldIDs.add(Integer.toString(world.provider.dimensionId));
            }
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(arguments, worldIDs.toArray(new String[] { "" }));
        }
        return null;
    }
}
