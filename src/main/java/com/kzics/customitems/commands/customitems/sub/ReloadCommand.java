package com.kzics.customitems.commands.customitems.sub;

import com.kzics.customitems.CustomItems;
import com.kzics.customitems.commands.ICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements ICommand {

    private final CustomItems customItems;
    public ReloadCommand(final CustomItems customItems){
        this.customItems = customItems;
    }

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

        sender.sendMessage(Component.text("Successfully reloaded items!").color(NamedTextColor.RED));
        customItems.getItemsLoader().loadItems();
    }
}
