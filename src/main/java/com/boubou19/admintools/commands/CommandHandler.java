package com.boubou19.admintools.commands;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommandHandler {
    public static void initCommands(FMLServerStartingEvent event) {

        event.registerServerCommand(CommandTPS.instance);
    }
}
