package com.kzics.customitems.config;

import com.kzics.customitems.CustomItems;
import com.kzics.customitems.enums.ActivationType;
import com.kzics.customitems.enums.TargetType;
import com.kzics.customitems.items.ConsumableItem;
import com.kzics.customitems.obj.ConsumableEffects;
import com.kzics.customitems.obj.TargetInfo;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemsLoader {


    private final CustomItems customItems;
    public ItemsLoader(CustomItems customItems) {
        this.customItems = customItems;
    }


    public void loadItems(){
        File itemsFolder = new File(customItems.getDataFolder(), "items");

        if(!itemsFolder.exists()) itemsFolder.mkdir();


        File[] files = itemsFolder.listFiles(new YamlFilter());
        if(files == null) return;

        for (File file : files){
            System.out.println(file.getName());
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

            loadItem(configuration);
        }
    }

    public void loadItem(YamlConfiguration configuration) {
        final String name = configuration.getString("name");
        final String id = configuration.getString("id");
        final List<String> lore = configuration.getStringList("lore");
        final int uses = configuration.getInt("uses");
        final int cooldown = configuration.getInt("cooldown"); //Cooldown in seconds
        final Material material = Material.valueOf(configuration.getString("material"));
        final Sound sound = Sound.valueOf(configuration.getString("sound"));

        ActivationType activationType;
        TargetType targetType;
        try {
            activationType = ActivationType.valueOf(configuration.getString("activation"));
        } catch (Exception e) {
            throw new IllegalArgumentException("activation type can't be found!");
        }

        try {
            targetType = TargetType.valueOf(configuration.getString("target.type"));
        } catch (Exception e) {
            throw new IllegalArgumentException("target type key can't be found! Please check your files");
        }

        final int radius = configuration.getInt("radius", -1);

        TargetInfo targetInfo = new TargetInfo(targetType, radius);

        List<PotionEffect> potionEffects = new ArrayList<>();

        List<?> effectsList = configuration.getList("effects");
        boolean affectClanMember = configuration.getBoolean("affect-clan-member");


        for (Object effectObj : effectsList) {
            if (effectObj instanceof Map) {
                Map<?, ?> effectMap = (Map<?, ?>) effectObj;

                // Convertissez explicitement en Map<String, Object>
                String typeStr = (String) effectMap.get("type");
                int duration = (int) effectMap.get("duration");
                int amplifier = (int) effectMap.get("amplifier");

                PotionEffectType type = PotionEffectType.getByName(typeStr);
                if (type != null) {
                    PotionEffect potionEffect = new PotionEffect(type, duration * 20, amplifier); // Multiplier par 20 pour obtenir la durée en ticks
                    potionEffects.add(potionEffect);
                } else {
                    throw new IllegalArgumentException("PotionEffectType invalid: " + typeStr);
                }
            } else {
                throw new IllegalArgumentException("Error .");
            }
        }
        final ConsumableEffects consumableEffects = new ConsumableEffects(potionEffects.toArray(potionEffects.toArray(new PotionEffect[0])));

        ConsumableItemConfig consumableItem = new ConsumableItemConfig(name, id, lore, material, uses, cooldown, activationType, targetInfo, consumableEffects, affectClanMember, sound );

        customItems.getItemsManager().addItem(id, consumableItem);

        customItems.getLogger().info("Successfully loaded " + id + " item!");
    }
}
