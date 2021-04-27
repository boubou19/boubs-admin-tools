package com.boubou19.admintools.commands;

import com.boubou19.admintools.AdminTools;
import com.boubou19.admintools.commands.base.CommandBaseLocation;
import com.google.common.base.Throwables;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class CommandCLocation extends CommandBaseLocation {
    public static final ICommand instance = new CommandCLocation();
    public CommandCLocation() {
        dumpPath = AdminTools.CLocationFile;
        commandName = "clocation";
        commandAliases = Arrays.asList(new String[]{"at_clocation"});
        dumpType = "chunk";
    }

    @Override
    protected List<String> getStats(ICommandSender sender, String dimID, String filter){
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

        List<String> chunkDataList = null;

        if (!(world.getChunkProvider() instanceof ChunkProviderServer)) {
            return chunkDataList;
        }

        chunkDataList = new ArrayList<>();

        ChunkProviderServer chunkProviderServer = (ChunkProviderServer) world.getChunkProvider();

        Iterator iterator = chunkProviderServer.loadedChunks.iterator();
        chunkDataList.add(Utils.buildString(new String[]{"chunk dump of dim ", dimID, "(", world.provider.getDimensionName(), ")"}));

        while (iterator.hasNext()){
            Chunk chunk = (Chunk) iterator.next();
            String key = chunk.getClass().toString();

            String chunkData = Utils.buildString(new String[] {
                    key, " at: [", Integer.toString(chunk.xPosition), ", ", Integer.toString(chunk.zPosition), "]"
            });
            chunkDataList.add(chunkData);
        }

        return chunkDataList;
    }
}
