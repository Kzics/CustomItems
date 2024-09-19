package com.kzics.customitems.commands.customitems.sub;

import com.kzics.customitems.commands.ICommand;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements ICommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getPermission() {
        return "customitems.reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }
}
