package com.boubou19.admintools;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Config {
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
    public int delaySecondBetween2Trigger;
    public boolean useTPSCommand;
    public boolean mapPathSet;
    public List<Path> ocAdminPaths;

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
        delaySecondBetween2Trigger = config.get("commands", "delay", 100, "Delay in seconds between 2 trigger of the commands").getInt();
        useTPSCommand = config.get("commands", "use_tps_command", true, "if true enables regular tps triggers").getBoolean();
        String mapName = config.get("misc", "map name", "", "relative path to the map folder").getString();
        if(mapName.equals("")){
            AdminTools.log.warn("the map path has not been set, integration will not fully work");
            mapPathSet = false;
        }
        else{
            mapPathSet = true;
            mapPath = Paths.get(rootInstance, mapName).toAbsolutePath();
            AdminTools.log.info("the map path has been set to: "+mapPath.toString());
        }
        config.save();
    }
}
