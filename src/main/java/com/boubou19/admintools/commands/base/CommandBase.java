package com.boubou19.admintools.commands.base;

import com.boubou19.admintools.AdminTools;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CommandBase implements ICommand{
    public static CommandBase instance = new CommandBase();
    protected Path dumpPath;
    protected String commandName;
    protected List<String> commandAliases;

    public void reload(){

    }

    protected List<String> getStats (ICommandSender sender, String dimID){
        return getStats(sender, dimID, "");
    }

    protected List<String> getStats (ICommandSender sender, String dimID, String filter){
        List<String> stats = new ArrayList<>();
        return stats;
    }

    protected void writeDump(List<String> dataList){
        AdminTools.writeToDedicatedLogFile(dumpPath, dataList);
    }

    public int compareTo(ICommand p_compareTo_1_)
    {
        return this.getCommandName().compareTo(p_compareTo_1_.getCommandName());
    }

    public int compareTo(Object p_compareTo_1_)
    {
        return this.compareTo((ICommand)p_compareTo_1_);
    }

    public String getCommandUsage(ICommandSender commandSender) {
        return "admintools.command." + getCommandName() + ".syntax";
    }
    public String getCommandName() {

        return commandName;
    }

    public int getRequiredPermissionLevel(){
        return 2;
    }

    public boolean canCommandSenderUseCommand(ICommandSender commandSender){
        return commandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    public List getCommandAliases(){
        return commandAliases;
    }

    public void processCommand(ICommandSender sender, String[] arguments) {

    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
    {
        return false;
    }
}
