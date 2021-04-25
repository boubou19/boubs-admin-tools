package com.boubou19.admintools;

import com.boubou19.admintools.commands.CommandHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Mod(modid = AdminTools.MODID, version = AdminTools.VERSION, acceptableRemoteVersions = "*")
public class AdminTools
{
    public static final String MODID = "admintools";
    public static final String VERSION = "${version}";
    public static MinecraftServer server;
    public static final Logger log = LogManager.getLogger(MODID);
    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        server = event.getServer();
        CommandHandler.initCommands(event);
    }

    public static void writeToDedicatedLogFile(List<String> lines){

        Path file = Paths.get(FMLServerHandler.instance().getSavesDirectory() + File.separator + "logs" +File.separator + "tps.log");
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        }
        catch (IOException e){
            log.error(e);
        }
    }
}
