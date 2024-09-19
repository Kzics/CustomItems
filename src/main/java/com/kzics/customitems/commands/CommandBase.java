package com.kzics.customitems.commands;

import com.kzics.vbounty.utils.ColorsUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandBase implements CommandExecutor {
    public CommandBase() {
    }

    protected Map<String, ICommand> subCommands = new HashMap<>();

    public void registerSubCommand(String name, ICommand command) {
        subCommands.put(name.toLowerCase(), command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) return false;
        if (args.length > 0) {
            ICommand subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null) {
                if(!sender.hasPermission(subCommand.getPermission())){
                    sender.sendMessage(ColorsUtil.translate.apply("You don't have the permission"));
                    return true;
                }
                subCommand.execute(sender, args);
                return true;
            }
            //subCommands.get("help").execute(sender, args);
        }

        return false;
    }
}
