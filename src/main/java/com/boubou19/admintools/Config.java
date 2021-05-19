package com.boubou19.admintools;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Config {
    public static final String CATEGORY_COMMANDS = "commands";
    public static final String CATEGORY_MISC = "misc";
    public static final String CATEGORY_OFFLINE_DATA = "offline data";
    public static Configuration config;
    public static String rootInstance;
    public static String cfgPath;

    public static String defaultLogPath = Paths.get("logs", "admintools", "tps").toString();
    public Path EStatsPath;
    public Path TEStatsPath;
    public Path CLocationPath;
    public Path ELocationPath;
    public Path TELocationPath;
    public Path TPSPath;
    public Path mapPath;
    public Path PlayerOfflineDataPath;
    public String playerOfflineDataPathString;
    public int delaySecondBetween2Trigger;
    public boolean useAutomatedCommands;
    public boolean mapPathSet;
    public List<Path> ocAdminPaths;
    public String[] automatedCommands;

    public void initConfiguration(String rootInstance){
        cfgPath = Paths.get(rootInstance, "config", "boubsAdminTools.cfg").toString();
        this.rootInstance = rootInstance;
        loadConfiguration();
        loadAdminComputers();
    }

    public void loadAdminComputers(){
        ocAdminPaths = new ArrayList<>();
        if (!mapPathSet){
            AdminTools.log.warn("map path has not been set, cannot read admin oc computers");
            return;
        }
        Path ocPath = Paths.get(mapPath.toString(), "opencomputers");
        File ocDir = new File(ocPath.toString());
        if (!ocDir.exists()){
            AdminTools.log.warn("no opencomputer found in the map folder, check your config/map");
            return;
        }

        for (File computerDir: ocDir.listFiles()){
            Path adminPath = Paths.get(computerDir.getAbsolutePath().toString(), "admin");
            if (new File(adminPath.toString()).exists()){
                ocAdminPaths.add(adminPath);
                AdminTools.log.info(adminPath.toAbsolutePath().toString()+" will receive dumps");
            }
        }
    }

    public void loadConfiguration(){
        config = new Configuration(new File(cfgPath));
        String logPathString = config.get("log paths", "log path", defaultLogPath, "where the logs should be located. If the path is not valid, it will fall back to '"+defaultLogPath+"'").getString();
        EStatsPath = Paths.get(rootInstance, logPathString, "e_stats.log").toAbsolutePath();
        TEStatsPath = Paths.get(rootInstance, logPathString, "te_stats.log").toAbsolutePath();
        CLocationPath = Paths.get(rootInstance, logPathString, "c_location.log").toAbsolutePath();
        ELocationPath = Paths.get(rootInstance, logPathString, "e_location.log").toAbsolutePath();
        TELocationPath = Paths.get(rootInstance, logPathString, "te_location.log").toAbsolutePath();
        TPSPath = Paths.get(rootInstance, logPathString, "tps.log").toAbsolutePath();

        delaySecondBetween2Trigger = config.get(CATEGORY_COMMANDS, "delay", 100, "Delay in seconds between 2 trigger of the commands").getInt();
        useAutomatedCommands = config.get(CATEGORY_COMMANDS, "use automated command", true, "if true enables automated commands triggers").getBoolean();

        String mapName = config.get(CATEGORY_MISC, "map name", "", "relative path to the map folder").getString();
        if(mapName.equals("")){
            AdminTools.log.warn("the map path has not been set, integration will not fully work");
            mapPathSet = false;
        }
        else{
            mapPathSet = true;
            mapPath = Paths.get(rootInstance, mapName).toAbsolutePath();
            AdminTools.log.info("the map path has been set to: "+mapPath.toString());
        }
        automatedCommands = config.get(CATEGORY_COMMANDS, "command list", new String[]{"at_tps"}, "list of commands executed between two triggers").getStringList();
        playerOfflineDataPathString = config.get(CATEGORY_OFFLINE_DATA, "path", "offline pos", "path to the offline player data file. Default to ./offline pos").getString();
        PlayerOfflineDataPath = Paths.get(rootInstance, playerOfflineDataPathString).toAbsolutePath();
        config.save();
        initFolders();

    }

    public void initFolders(){
        Path[] folderArray = new Path[]{
                PlayerOfflineDataPath,
                TPSPath.getParent() //admin logs folder
        };
        for (Path folderPath : folderArray) {
            File folder = new File(folderPath.toAbsolutePath().toString());
            if (!folder.exists()) {
                folder.mkdirs();
                AdminTools.log.info("folder '"+folderPath.toAbsolutePath().toString()+"' did not exist, created it");
            }
        }
    }
}
