package com.boubou19.admintools;

import cofh.lib.util.helpers.EntityHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.DimensionManager;

import java.nio.file.Path;
import java.nio.file.Paths;

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

    @SubscribeEvent
    public void trackdownPlayerPosition(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event){
        Path offlineDataFile = Paths.get(AdminTools.configuration.PlayerOfflineDataPath.toString(), event.player.getDisplayName()+".json");
        AdminTools.log.info("player "+event.player.getDisplayName()+" disconnected.");
        boolean success = JsonManager.writeOfflinePlayerCoords(offlineDataFile, new PlayerCoords(event));
        AdminTools.log.info("did it saved? "+success);
    }

    @SubscribeEvent
    public void resetPlayerPosition(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event){

        PlayerCoords current = new PlayerCoords(event);
        Path offlineDataFile = Paths.get(AdminTools.configuration.PlayerOfflineDataPath.toString(), event.player.getDisplayName()+".json");
        PlayerCoords offline = JsonManager.readOfflinePlayerCoords(offlineDataFile);
        if (offline == null){
            return ;
        }
        if (!offline.sameCoordAs(current)){
            if (offline.dim != current.dim){ //interdim tp
                event.player.mountEntity((Entity) null);
                if (!DimensionManager.isDimensionRegistered(offline.dim)){
                    AdminTools.log.error("attempted to tp player "+event.player.getDisplayName()+" in dim "+offline.dim+" but the dim does not exist. Aborting offline tp.");
                    return ;
                }
                EntityHelper.transferPlayerToDimension((EntityPlayerMP) event.player, offline.dim, AdminTools.server.getConfigurationManager());
            }
            event.player.setPositionAndUpdate((double) offline.x, (double) offline.y, (double) offline.z);
            IChatComponent message = new ChatComponentText("You have been teleported by an Operator.");
            event.player.addChatMessage(message);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(net.minecraftforge.event.world.WorldEvent.Unload event){
        for (Object player : event.world.playerEntities){
            PlayerCoords playerCoords = new PlayerCoords((EntityPlayer) player);
            Path offlineDataFile = Paths.get(AdminTools.configuration.PlayerOfflineDataPath.toString(), ((EntityPlayer) player).getDisplayName() + ".json");
            JsonManager.writeOfflinePlayerCoords(offlineDataFile, playerCoords);

        }
    }
}
