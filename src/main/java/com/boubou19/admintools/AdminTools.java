package com.boubou19.admintools;

import com.boubou19.admintools.commands.CommandHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
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
    //public static final Path TPSFile = Paths.get(FMLServerHandler.instance().getSavesDirectory() + File.separator + "logs" +File.separator + "tps.log");
    //public static final Path TEStatsFile = Paths.get(FMLServerHandler.instance().getSavesDirectory() + File.separator + "logs" +File.separator + "te_stats.log");
    public static final Path TPSFile = Paths.get("." + File.separator + "logs" + File.separator + "tps.log");
    public static final Path TEStatsFile = Paths.get("." + File.separator + "logs" + File.separator + "te_stats.log");
    public static final Path EStatsFile = Paths.get("." + File.separator + "logs" + File.separator + "e_stats.log");
    public static final Path ELocationFile = Paths.get("." + File.separator + "logs" + File.separator + "e_location.log");
    public static final Path TELocationFile = Paths.get("." + File.separator + "logs" + File.separator + "te_location.log");
    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        server = event.getServer();
        CommandHandler.initCommands(event);
    }

    public static void writeToDedicatedLogFile(Path file, List<String> lines){

        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        }
        catch (IOException e){
            log.error(e);
        }
    }
}
