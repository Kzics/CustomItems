package com.kzics.customitems.commands.customitems.sub;

import com.kzics.customitems.CustomItems;
import com.kzics.customitems.commands.ICommand;
import com.kzics.customitems.config.ConsumableItemConfig;
import com.kzics.customitems.items.ConsumableItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand implements ICommand {

    private final CustomItems customItems;
    public GiveCommand(CustomItems customItems){
        this.customItems = customItems;
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getPermission() {
        return "customitems.give";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(args.length != 3) return;

        String playerName = args[1];
        Player player = Bukkit.getPlayer(playerName);
        if(player == null) {
            sender.sendMessage(Component.text("Can't give an item to this player").color(NamedTextColor.RED));
            return;
        }
        String item = args[2];

        ConsumableItemConfig config = customItems.getItemsManager().getItem(item);

        player.getInventory().addItem(new ConsumableItem(config));
        player.sendMessage(Component.text("Received a consumable item").color(NamedTextColor.GREEN));
    }
}
