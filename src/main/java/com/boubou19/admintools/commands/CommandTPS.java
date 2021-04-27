package com.boubou19.admintools.commands;

import com.boubou19.admintools.AdminTools;
import com.google.common.base.Throwables;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

public class CommandTPS implements ICommand {

    public static CommandTPS instance = new CommandTPS();

    private static DecimalFormat floatfmt = new DecimalFormat("##0.00");

    private double getTickTimeSum(long[] times) {

        long timesum = 0L;
        if (times == null) {
            return 0.0D;
        }
        for (int i = 0; i < times.length; i++) {
            timesum += times[i];
        }

        return timesum / times.length;
    }

    private double getTickMs(World world) {

        return getTickTimeSum(world == null ? AdminTools.server.tickTimeArray : (long[]) AdminTools.server.worldTickTimes.get(Integer
                .valueOf(world.provider.dimensionId))) * 1.0E-006D;
    }

    public int compareTo(ICommand p_compareTo_1_)
    {
        return this.getCommandName().compareTo(p_compareTo_1_.getCommandName());
    }

    public int compareTo(Object p_compareTo_1_)
    {
        return this.compareTo((ICommand)p_compareTo_1_);
    }

    private double getTps(double tickTimeMS) {

        double tps = 1000.0D / tickTimeMS;
        return tps > 20.0D ? 20.0D : tps;
    }

    public String getCommandUsage(ICommandSender commandSender) {
        return "admintools.command." + getCommandName() + ".syntax";
    }
    public String getCommandName() {

        return "tps";
    }

    public int getRequiredPermissionLevel(){
        return 2;
    }

    public boolean canCommandSenderUseCommand(ICommandSender commandSender){
        return commandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    public List getCommandAliases(){
        return Arrays.asList(new String[]{"at_tps"});
    }

    private void tpsAll(ICommandSender sender){
        double tickms;
        double tps;
        String dimId;
        String dimName;
        String load;
        Map<Double, String[]> sortedTickTimeMap = new TreeMap<Double, String[]>();
        ChatComponentText chatMessage;
        List<String> tpsOutput = new ArrayList<String>();

        int totalLoadedChunks = 0;
        int totalEntities = 0;
        int totalTe = 0;
        int totalWorlds = 0;
        double totalTickTime = 0;

        int loadedChunks = 0;
        int entities = 0;
        int te = 0;

        for (World world : AdminTools.server.worldServers) {
            loadedChunks = world.getChunkProvider().getLoadedChunkCount();
            entities = world.loadedEntityList.size();
            te = world.loadedTileEntityList.size();

            totalLoadedChunks += loadedChunks;
            totalEntities += entities;
            totalTe += te;
            totalWorlds += 1;

            tickms = getTickMs(world);
            totalTickTime += tickms;
            dimId = Integer.toString(world.provider.dimensionId);
            dimName = world.provider.getDimensionName();
            String[] dim = new String[]{
                    dimId,
                    dimName,
                    Integer.toString(loadedChunks),
                    Integer.toString(entities),
                    Integer.toString(te)
            };

            sortedTickTimeMap.put(tickms, dim);
        }

        for(Map.Entry<Double,String[]> entry : sortedTickTimeMap.entrySet()) {
            String[] entryValue = entry.getValue();
            tickms = entry.getKey();
            tps = getTps(tickms);

            String[] messageContent = new String[]{
                    entryValue[1],
                    " [",
                    entryValue[0],
                    "]: ",
                    floatfmt.format(tps),
                    " TPS / ",
                    floatfmt.format(tickms),
                    " ms. Loaded chunks: ",
                    entryValue[2],
                    ". Loaded entities: ",
                    entryValue[3],
                    ". Loaded TE: ",
                    entryValue[4],
                    "."
            };

            String stat = Utils.buildString(messageContent);
            if (!Utils.isServerSendingCommand(sender)){
                chatMessage = new ChatComponentText(stat);
                sender.addChatMessage(chatMessage);
            }
            else{
                tpsOutput.add(stat);
            }
        }
        tps = getTps(totalTickTime);
        String[] messageContent = new String[]{
                "Overall: ",
                floatfmt.format(tps),
                " TPS / ",
                floatfmt.format(totalTickTime),
                " ms. Loaded chunks: ",
                Integer.toString(totalLoadedChunks),
                ". Loaded entities: ",
                Integer.toString(totalEntities),
                ". Loaded TE: ",
                Integer.toString(totalTe),
                ". Loaded dims: ",
                Integer.toString(totalWorlds),
                "."
        };

        String stat = Utils.buildString(messageContent);

        if (!Utils.isServerSendingCommand(sender)){
            chatMessage = new ChatComponentText(stat);
            sender.addChatMessage(chatMessage);
        }
        else{
            tpsOutput.add(stat);
            AdminTools.writeToDedicatedLogFile(AdminTools.TPSFile, tpsOutput);
        }
    }

    private void tpsDim(ICommandSender sender, String dimID){
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

        double tickms = getTickMs(world);
        double tps = getTps(tickms);
        StringBuilder stringBuilder = new StringBuilder();

        String[] messageContent = new String[]{
                world.provider.getDimensionName(),
                " [",
                Integer.toString(dim),
                "]: ",
                floatfmt.format(tps),
                " TPS/ ",
                floatfmt.format(tickms),
                "ms. Loaded chunks: ",
                Integer.toString(world.getChunkProvider().getLoadedChunkCount()),
                ". Loaded entities: ",
                Integer.toString(world.loadedEntityList.size()),
                ". Loaded TE: ",
                Integer.toString(world.loadedTileEntityList.size())
        };
        for (String str : messageContent){
            stringBuilder.append(str);
        }

        ChatComponentText chatMessage = new ChatComponentText(stringBuilder.toString());
        sender.addChatMessage(chatMessage);

    }

    public void processCommand(ICommandSender sender, String[] arguments) {

        if (arguments.length < 1) {
            tpsAll(sender);
        } else {
            tpsDim(sender, arguments[0]);
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