package com.kzics.customitems.items;

import com.kzics.customitems.CustomItems;
import com.kzics.customitems.enums.ActivationType;
import com.kzics.customitems.obj.ConsumableEffects;
import com.kzics.customitems.obj.TargetInfo;
import com.kzics.customitems.utils.SerializerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ConsumableItem extends ItemStack {

    public static NamespacedKey itemKey = new NamespacedKey(CustomItems.getInstance(), "consumable_items");
    public static NamespacedKey effectsKey = new NamespacedKey(CustomItems.getInstance(), "consumable_effects");

    public ConsumableItem(String name, String id, List<String> lore, Material material, int use, int cooldown, ActivationType activationType, TargetInfo targetInfo, ConsumableEffects effects) {
        super(material);

        ItemMeta meta = getItemMeta();

        meta.displayName(Component.text(name));
        meta.setLore(lore);

        meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING
                , String.format("%s;%s;%s;%s;%s;%s",id, use, cooldown
                        ,activationType, targetInfo.targetType(), targetInfo.radius()));

        meta.getPersistentDataContainer().set(effectsKey, PersistentDataType.STRING, SerializerUtils.serializeEffects(effects));

        setItemMeta(meta);
    }
}
