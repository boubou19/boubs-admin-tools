package com.boubou19.admintools.commands;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommandHandler {
    public static void initCommands(FMLServerStartingEvent event) {

        event.registerServerCommand(CommandTPS.instance);
        event.registerServerCommand(CommandTELocation.instance);
        event.registerServerCommand(CommandELocation.instance);
        event.registerServerCommand(CommandTEStats.instance);
        event.registerServerCommand(CommandEStats.instance);
    }
}
