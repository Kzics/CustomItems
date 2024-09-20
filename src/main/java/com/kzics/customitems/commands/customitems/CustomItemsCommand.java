package com.kzics.customitems.commands.customitems;

import com.kzics.customitems.CustomItems;
import com.kzics.customitems.commands.CommandBase;
import com.kzics.customitems.commands.customitems.sub.GiveCommand;
import com.kzics.customitems.commands.customitems.sub.ReloadCommand;
import com.kzics.customitems.config.ConsumableItemConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CustomItemsCommand extends CommandBase implements TabCompleter {

    private final CustomItems customItems;
    public CustomItemsCommand(CustomItems customItems){
        registerSubCommand("give", new GiveCommand(customItems));
        registerSubCommand("reload", new ReloadCommand(customItems));

        this.customItems = customItems;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("customitems.give")) completions.add("give");
            if (sender.hasPermission("customitems.reload")) completions.add("reload");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {

            Bukkit.getOnlinePlayers().forEach(player -> completions.add(player.getName()));
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {

            completions.addAll(customItems.getItemsManager().getItems().values().stream().map(ConsumableItemConfig::getId)
                    .toList());
        }

        return completions;
    }
}
