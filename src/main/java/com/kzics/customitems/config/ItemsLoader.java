package com.kzics.customitems.config;

import com.kzics.customitems.enums.ActivationType;
import com.kzics.customitems.enums.TargetType;
import com.kzics.customitems.items.ConsumableItem;
import com.kzics.customitems.obj.ConsumableEffects;
import com.kzics.customitems.obj.TargetInfo;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemsLoader {


    public ItemsLoader() {

    }


    public void loadItem(YamlConfiguration configuration) {
        final String name = configuration.getString("name");
        final String id = configuration.getString("id");
        final List<String> lore = configuration.getStringList("lore");
        final int uses = configuration.getInt("uses");
        final int cooldown = configuration.getInt("cooldown"); //Cooldown in seconds
        final Material material = Material.valueOf(configuration.getString("material"));

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

        for (String key : configuration.getConfigurationSection("effects").getKeys(false)) {

            PotionEffectType type = PotionEffectType.getByName(configuration.getString("effects." + key + ".type"));
            int duration = configuration.getInt("effects." + key + ".duration");
            int amplifier = configuration.getInt("effects." + key + ".amplifier");

            potionEffects.add(new PotionEffect(type, duration, amplifier));
        }

        final ConsumableEffects consumableEffects = new ConsumableEffects(potionEffects.toArray(potionEffects.toArray(new PotionEffect[0])));

        ConsumableItem consumableItem = new ConsumableItem(name, id, lore, material, uses, cooldown, activationType, targetInfo, consumableEffects);
    }
}
