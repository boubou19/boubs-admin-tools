package com.boubou19.admintools.commands;

import com.boubou19.admintools.commands.base.CommandBase;
import com.boubou19.admintools.commands.base.CommandReload;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.ICommand;

public class CommandHandler {
    public static CommandBase[] commandArray = new CommandBase[]{
            CommandTPS.instance,
            CommandTELocation.instance,
            CommandELocation.instance,
            CommandCLocation.instance,
            CommandTEStats.instance,
            CommandEStats.instance,
            CommandReload.instance
    };

    public static void initCommands(FMLServerStartingEvent event) {

        for(ICommand command : commandArray){
            event.registerServerCommand(command);
        }
    }

    public static void reloadCommands(){
        for(CommandBase command : commandArray){
            command.reload();
        }
    }
}
