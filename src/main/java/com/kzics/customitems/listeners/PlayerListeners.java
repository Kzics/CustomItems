package com.kzics.customitems.listeners;

import com.kzics.customitems.CustomItems;
import com.kzics.customitems.items.ConsumableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListeners implements Listener {

    private final CustomItems customItems;
    public PlayerListeners(CustomItems customItems){
        this.customItems = customItems;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();

        for (ConsumableItem consumableItem : customItems.getItemsManager().getItems().values()){
            System.out.println("received an item");
            player.getInventory().addItem(consumableItem);
        }
    }
}
