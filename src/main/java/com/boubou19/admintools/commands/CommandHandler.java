package com.boubou19.admintools.commands;

import com.boubou19.admintools.commands.base.CommandBaseStats;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommandHandler {
    public static CommandBaseStats[] commandArray = new CommandBaseStats[]{
            CommandTPS.instance,
            CommandTELocation.instance,
            CommandELocation.instance,
            CommandCLocation.instance,
            CommandTEStats.instance,
            CommandEStats.instance,
    };

    public static void initCommands(FMLServerStartingEvent event) {
        for (CommandBaseStats command : commandArray){
            event.registerServerCommand(command.instance);
        }
    }
}
