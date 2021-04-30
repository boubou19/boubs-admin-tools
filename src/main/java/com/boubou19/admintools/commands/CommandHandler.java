package com.boubou19.admintools.commands;

import com.boubou19.admintools.commands.base.CommandReload;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommandHandler {
    public static void initCommands(FMLServerStartingEvent event) {

        event.registerServerCommand(CommandTPS.instance);
        event.registerServerCommand(CommandTELocation.instance);
        event.registerServerCommand(CommandELocation.instance);
        event.registerServerCommand(CommandCLocation.instance);
        event.registerServerCommand(CommandTEStats.instance);
        event.registerServerCommand(CommandEStats.instance);
        event.registerServerCommand(CommandReload.instance);
    }
}
