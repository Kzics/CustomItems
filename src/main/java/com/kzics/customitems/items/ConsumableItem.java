package com.kzics.customitems.items;

import com.kzics.customitems.CustomItems;
import com.kzics.customitems.config.ConsumableItemConfig;
import com.kzics.customitems.enums.ActivationType;
import com.kzics.customitems.obj.ConsumableEffects;
import com.kzics.customitems.obj.TargetInfo;
import com.kzics.customitems.utils.SerializerUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConsumableItem extends ItemStack {

    public static NamespacedKey itemKey = new NamespacedKey(CustomItems.getInstance(), "consumable_items");
    public static NamespacedKey effectsKey = new NamespacedKey(CustomItems.getInstance(), "consumable_effects");
    public static NamespacedKey cooldownKey = new NamespacedKey(CustomItems.getInstance(), "consumable_cooldown");

    public ConsumableItem(ConsumableItemConfig config){
        this(config.getName(), config.getId(), config.getLore(), config.getMaterial(), config.getUse(), config.getCooldown(), config.getActivationType(), config.getTargetInfo(), config.getConsumableEffects(), config.isAffectingClanMember(), config.getSound());
    }
    public ConsumableItem(String name, String id, List<String> lore, Material material, int use, int cooldown, ActivationType activationType, TargetInfo targetInfo, ConsumableEffects effects, boolean affectClanMembers, Sound sound) {
        super(material);

        ItemMeta meta = getItemMeta();

        String displayName = replacePlaceholders(name, use, cooldown, activationType, targetInfo);
        meta.displayName(parseColoredText(displayName));

        List<Component> loreComponents = lore.stream()
                .map(line -> replacePlaceholders(line, use, cooldown, activationType, targetInfo))
                .map(ConsumableItem::parseColoredText)
                .collect(Collectors.toList());

        meta.lore(loreComponents);

        meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING
                , String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s", id, use, cooldown
                        , activationType, targetInfo.targetType(), targetInfo.radius(), UUID.randomUUID(), affectClanMembers, sound.name()));

        meta.getPersistentDataContainer().set(effectsKey, PersistentDataType.STRING, SerializerUtils.serializeEffects(effects));

        long cooldownExpiryTime = System.currentTimeMillis() + (cooldown * 1000L);
        meta.getPersistentDataContainer().set(cooldownKey, PersistentDataType.LONG, cooldownExpiryTime);

        setItemMeta(meta);
    }

    public static String replacePlaceholders(String text, int uses, int cooldown, ActivationType activationType, TargetInfo targetInfo) {
        return text
                .replace("{use}", String.valueOf(uses))
                .replace("{cooldown}", String.valueOf(cooldown))
                .replace("{activation}", activationType.getFormatted())
                .replace("{target}", targetInfo.targetType().getFormatted())
                .replace("{radius}", String.valueOf(targetInfo.radius()));
    }

    public static Component parseColoredText(String text) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(text);
        Component component = Component.text("");

        int lastEnd = 0;
        while (matcher.find()) {
            String colorCode = matcher.group();
            int start = matcher.start();
            int end = matcher.end();

            if (start > lastEnd) {
                component = component.append(Component.text(text.substring(lastEnd, start)));
            }

            if (end < text.length()) {
                String coloredText = text.substring(end, text.indexOf("#", end) == -1 ? text.length() : text.indexOf("#", end));
                component = component.append(Component.text(coloredText, TextColor.fromHexString(colorCode)));
                lastEnd = end + coloredText.length();
            }
        }

        if (lastEnd < text.length()) {
            component = component.append(Component.text(text.substring(lastEnd)));
        }

        return component;
    }


}
