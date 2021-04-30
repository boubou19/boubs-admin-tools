package com.boubou19.admintools;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


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

    public void initConfiguration(String rootInstance){
        cfgPath = Paths.get(rootInstance, "config", "boubsAdminTools.cfg").toString();
        this.rootInstance = rootInstance;
        loadConfiguration();

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
        config.save();
    }
}
