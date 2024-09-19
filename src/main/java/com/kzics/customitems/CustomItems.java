package com.kzics.customitems;

import com.kzics.customitems.config.ItemsLoader;
import com.kzics.customitems.listeners.PlayerListeners;
import com.kzics.customitems.manager.ItemsManager;
import com.kzics.customitems.utils.ClanUtils;
import me.ulrich.clans.Clans;
import me.ulrich.clans.api.ClanAPIManager;
import me.ulrich.clans.data.ClanEnum;
import me.ulrich.clans.interfaces.ClanAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItems extends JavaPlugin {

    private static CustomItems instance;
    private ItemsManager itemsManager;
    private ItemsLoader itemsLoader;
    private ClanUtils clanUtils;
    private ClanAPI clanAPI;


    @Override
    public void onEnable() {
        instance = this;
        setupClanAPI();

        if(!getDataFolder().exists()) getDataFolder().mkdir();
        itemsManager = new ItemsManager();
        itemsLoader = new ItemsLoader(this);
        itemsLoader.loadItems();
        clanUtils = new ClanUtils(clanAPI);



        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
    }

    public ItemsManager getItemsManager() {
        return itemsManager;
    }

    public ItemsLoader getItemsLoader() {
        return itemsLoader;
    }

    public ClanUtils getClanUtils() {
        return clanUtils;
    }

    public static CustomItems getInstance() {
        return instance;
    }
    public void setupClanAPI() {
        Plugin ultimateClansPlugin = Bukkit.getPluginManager().getPlugin("UltimateClans");

        if (ultimateClansPlugin != null && ultimateClansPlugin.isEnabled()) {
            this.clanAPI = ((ClanAPI) ultimateClansPlugin);
        } else {
            System.out.println("UltimateClans n'est pas trouvé ou activé.");
        }
    }
}
