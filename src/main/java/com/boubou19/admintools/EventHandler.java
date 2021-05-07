package com.boubou19.admintools;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.command.ICommandManager;

import static com.boubou19.admintools.AdminTools.fakeCommandSender;

public class EventHandler {
    private int tickCounter = 0;

    @SubscribeEvent
    public void sendCommandsPeriodically(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.START) {
            tickCounter += 1;
        }

        if (tickCounter == AdminTools.configuration.delaySecondBetween2Trigger){
            tickCounter = 0;
            if (AdminTools.configuration.useAutomatedCommands) {
                ICommandManager icommandmanager = AdminTools.server.getCommandManager();
                for (String command : AdminTools.configuration.automatedCommands) {
                    icommandmanager.executeCommand(fakeCommandSender, command);
                }
            }
        }
    }
}
