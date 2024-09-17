package com.kzics.customitems;

import org.bukkit.plugin.java.JavaPlugin;

public class CustomItems extends JavaPlugin {

    private static CustomItems instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static CustomItems getInstance() {
        return instance;
    }
}
