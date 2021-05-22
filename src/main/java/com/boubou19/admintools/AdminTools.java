package com.boubou19.admintools;

import com.boubou19.admintools.commands.FakeCommandSender;
import com.boubou19.admintools.commands.stats.CommandHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Mod(modid = AdminTools.MODID, version = AdminTools.VERSION, acceptableRemoteVersions = "*")
public class AdminTools
{
    public static final String MODID = "admintools";
    public static final String VERSION = "${version}";
    public static MinecraftServer server;
    public static final Logger log = LogManager.getLogger(MODID);
    public static Config configuration = new Config();
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

    @EventHandler
    public void OnServerStopping(FMLServerStoppingEvent event){
        for (WorldServer server : AdminTools.server.worldServers){
            for (Object player : server.playerEntities){
                PlayerCoords playerCoords = new PlayerCoords((EntityPlayer) player);
                Path offlineDataFile = Paths.get(AdminTools.configuration.PlayerOfflineDataPath.toString(), ((EntityPlayer) player).getDisplayName() + ".json");
                JsonManager.writeOfflinePlayerCoords(offlineDataFile, playerCoords);
                AdminTools.log.info("saving "+ ((EntityPlayer) player).getDisplayName()+"'s coords");
            }
        }
    }

    @NetworkCheckHandler
    public boolean getModList(Map<String, String> mods, Side side){


        if (side == Side.CLIENT && AdminTools.configuration.enableModAnalyzer) { // server side
            Map<String, String> clientMods = new TreeMap<>();
            Map<String, String> serverMods = new TreeMap<>();
            Map<String, String> mistmatchMods = new TreeMap<>();
            clientMods.putAll(mods);

            Map<String, ModContainer> modMap = Loader.instance().getIndexedModList();
            for (Map.Entry<String, ModContainer> entry : modMap.entrySet()) {
                if(clientMods.containsKey(entry.getKey())){
                    if (!entry.getValue().getDisplayVersion().equals(clientMods.get(entry.getKey()))){
                        mistmatchMods.put(entry.getKey(),  entry.getValue().getDisplayVersion());
                    }
                    clientMods.remove(entry.getKey());
                }
                else{
                    serverMods.put(entry.getKey(), entry.getValue().getDisplayVersion());
                }
            }
            AdminTools.log.info("clientside only mods:");
            Utils.displayMap(clientMods);
            AdminTools.log.info("serverside only mods:");
            Utils.displayMap(serverMods);
            AdminTools.log.info("mismatching version from server:");
            Utils.displayMap(mistmatchMods);
        }
        return true;
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
