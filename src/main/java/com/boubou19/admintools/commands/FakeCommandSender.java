package com.boubou19.admintools.commands;

import com.boubou19.admintools.AdminTools;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class FakeCommandSender implements ICommandSender {
    public String getCommandSenderName() {
        return "boubou_21";
    }

    public IChatComponent func_145748_c_() {
        return new ChatComponentText(getCommandSenderName());
    }

    public void addChatMessage(IChatComponent p_145747_1_) {
        //does nothing, as we don't want logs to be spammed
    }

    public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
        //it can use every command, it's the server and we trust it ;)
        return true;
    }

    public ChunkCoordinates getPlayerCoordinates() {
        return new ChunkCoordinates(0, 0, 0);
    }

    public World getEntityWorld() {
        return AdminTools.server.worldServerForDimension(0);
    }
}
