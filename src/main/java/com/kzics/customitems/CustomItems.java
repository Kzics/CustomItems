package com.kzics.customitems;

import com.kzics.customitems.config.ItemsLoader;
import com.kzics.customitems.listeners.PlayerListeners;
import com.kzics.customitems.manager.ItemsManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItems extends JavaPlugin {

    private static CustomItems instance;
    private ItemsManager itemsManager;
    private ItemsLoader itemsLoader;


    @Override
    public void onEnable() {
        instance = this;
        if(!getDataFolder().exists()) getDataFolder().mkdir();
        itemsManager = new ItemsManager();
        itemsLoader = new ItemsLoader(this);
        itemsLoader.loadItems();

        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
    }

    public ItemsManager getItemsManager() {
        return itemsManager;
    }

    public ItemsLoader getItemsLoader() {
        return itemsLoader;
    }

    public static CustomItems getInstance() {
        return instance;
    }
}
