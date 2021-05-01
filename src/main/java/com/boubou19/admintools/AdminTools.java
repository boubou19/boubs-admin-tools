package com.boubou19.admintools;

import com.boubou19.admintools.commands.CommandHandler;
import com.boubou19.admintools.commands.FakeCommandSender;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Mod(modid = AdminTools.MODID, version = AdminTools.VERSION, acceptableRemoteVersions = "*")
public class AdminTools
{
    public static final String MODID = "admintools";
    public static final String VERSION = "${version}";
    public static MinecraftServer server;
    public static final Logger log = LogManager.getLogger(MODID);
    public static Config configuration = new Config();
    private int tickCounter = 0;
    public static final FakeCommandSender fakeCommandSender = new FakeCommandSender();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configuration.initConfiguration(event.getModConfigurationDirectory().getParent());
        FMLCommonHandler.instance().bus().register(new com.boubou19.admintools.EventHandler());
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
