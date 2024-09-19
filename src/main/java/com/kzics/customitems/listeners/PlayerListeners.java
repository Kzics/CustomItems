package com.kzics.customitems.listeners;

import com.kzics.customitems.CustomItems;
import com.kzics.customitems.config.ConsumableItemConfig;
import com.kzics.customitems.enums.ActivationType;
import com.kzics.customitems.enums.TargetType;
import com.kzics.customitems.items.ConsumableItem;
import com.kzics.customitems.manager.EffectManager;
import com.kzics.customitems.obj.ConsumableEffects;
import com.kzics.customitems.utils.SerializerUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerListeners implements Listener {

    private final CustomItems customItems;

    // Cooldown map storing player UUIDs and the item type's cooldown end time
    private final Map<UUID, Map<String, Long>> itemCooldowns = new ConcurrentHashMap<>();
    private final Map<Player, Long> lastMessageTime = new HashMap<>();

    private static final long COOLDOWN_DURATION = 5000;

    public PlayerListeners(CustomItems customItems) {
        this.customItems = customItems;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        for (ConsumableItemConfig consumableItem : customItems.getItemsManager().getItems().values()) {
            player.getInventory().addItem(new ConsumableItem(consumableItem.getName(), consumableItem.getId(), consumableItem.getLore(), consumableItem.getMaterial(), consumableItem.getUse(), consumableItem.getCooldown(), consumableItem.getActivationType(), consumableItem.getTargetInfo(), consumableItem.getConsumableEffects()));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getItem();

        if (itemStack == null || itemStack.getItemMeta() == null) return;

        if (!itemStack.getItemMeta().getPersistentDataContainer().has(ConsumableItem.itemKey, PersistentDataType.STRING)) {
            return;
        }

        String[] itemInfo = itemStack.getItemMeta().getPersistentDataContainer()
                .get(ConsumableItem.itemKey, PersistentDataType.STRING).split(";");

        String itemId = itemInfo[0];
        long cooldown = Long.parseLong(itemInfo[2]);
        ActivationType activationType = ActivationType.valueOf(itemInfo[3]);
        int usesLeft = Integer.parseInt(itemInfo[1]);

        if (usesLeft <= 0) {
            destroyItem(player, itemStack);
            return;
        }

        if (activationType != ActivationType.RIGHT_CLICK) return;

        if (isOnCooldown(player, itemId)) {
            long currentTime = System.currentTimeMillis();
            if (lastMessageTime.containsKey(player)) {
                long lastTime = lastMessageTime.get(player);
                if (currentTime - lastTime < COOLDOWN_DURATION) {
                    return;
                }
            }

            lastMessageTime.put(player, currentTime);
            player.sendMessage(Component.text("This item is under cooldown!").color(NamedTextColor.RED));
            return;
        }

        // Start cooldown for this item type
        setCooldown(player, itemId, cooldown);

        usesLeft -= 1;

        TargetType targetType = TargetType.valueOf(itemInfo[4]);
        ConsumableEffects effects = SerializerUtils.deserializeEffects(itemStack.getItemMeta().getPersistentDataContainer().get(ConsumableItem.effectsKey, PersistentDataType.STRING));

        applyEffectsBasedOnTarget(player, null, targetType, effects, itemInfo);

        if (usesLeft <= 0) {
            destroyItem(player, itemStack);
        } else {
            ConsumableItemConfig config = customItems.getItemsManager().getItem(itemId);
            ConsumableItem consumableItem = new ConsumableItem(config.getName(), config.getId(), config.getLore(), config.getMaterial(), usesLeft, config.getCooldown(), config.getActivationType(), config.getTargetInfo(), config.getConsumableEffects());
            player.getInventory().setItemInMainHand(consumableItem);
        }
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        Player target = (Player) event.getEntity();
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack == null || itemStack.getItemMeta() == null) return;

        if (!itemStack.getItemMeta().getPersistentDataContainer().has(ConsumableItem.itemKey, PersistentDataType.STRING)) {
            return;
        }

        String[] itemInfo = itemStack.getItemMeta().getPersistentDataContainer()
                .get(ConsumableItem.itemKey, PersistentDataType.STRING).split(";");

        String itemId = itemInfo[0];
        long cooldown = Long.parseLong(itemInfo[2]);
        ActivationType activationType = ActivationType.valueOf(itemInfo[3]);

        if (activationType != ActivationType.HIT) return;

        if (isOnCooldown(player, itemId)) {
            long currentTime = System.currentTimeMillis();
            if (lastMessageTime.containsKey(player)) {
                long lastTime = lastMessageTime.get(player);
                if (currentTime - lastTime < COOLDOWN_DURATION) {
                    return;
                }
            }

            lastMessageTime.put(player, currentTime);
            player.sendMessage(Component.text("This item is under cooldown!").color(NamedTextColor.RED));
            return;
        }

        // Start cooldown for this item type
        setCooldown(player, itemId, cooldown);

        int usesLeft = Integer.parseInt(itemInfo[1]);
        if (usesLeft <= 0) {
            destroyItem(player, itemStack);
        }

        TargetType targetType = TargetType.valueOf(itemInfo[4]);
        ConsumableEffects effects = SerializerUtils.deserializeEffects(itemStack.getItemMeta().getPersistentDataContainer().get(ConsumableItem.effectsKey, PersistentDataType.STRING));

        applyEffectsBasedOnTarget(player, target, targetType, effects, itemInfo);

        if (usesLeft - 1 <= 0) {
            destroyItem(player, itemStack);
        } else {
            ConsumableItemConfig config = customItems.getItemsManager().getItem(itemId);
            ConsumableItem consumableItem = new ConsumableItem(config.getName(), config.getId(), config.getLore(), config.getMaterial(), usesLeft, config.getCooldown(), config.getActivationType(), config.getTargetInfo(), config.getConsumableEffects());
            player.getInventory().setItemInMainHand(consumableItem);
        }
    }

    private void applyEffectsBasedOnTarget(Player player, Player target, TargetType targetType, ConsumableEffects effects, String[] itemInfo) {
        switch (targetType) {
            case SELF:
                EffectManager.applyEffectsToSelf(player, Arrays.asList(effects.getPotionEffects()));
                break;
            case AREA:
                double radius = Double.parseDouble(itemInfo[5]);
                EffectManager.applyEffectsInArea(player, Arrays.asList(effects.getPotionEffects()), radius);
                break;
            case TARGET:
                if (target != null) {
                    EffectManager.applyEffectsOnHit(target, Arrays.asList(effects.getPotionEffects()));
                }
                break;
            default:
                break;
        }
    }

    private void destroyItem(Player player, ItemStack itemStack) {
        if (itemStack.getAmount() > 1) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        } else {
            player.getInventory().remove(itemStack);
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
    }

    private boolean isOnCooldown(Player player, String itemId) {
        Map<String, Long> playerCooldowns = itemCooldowns.getOrDefault(player.getUniqueId(), new HashMap<>());
        long currentTime = System.currentTimeMillis();
        return playerCooldowns.getOrDefault(itemId, 0L) > currentTime;
    }

    private void setCooldown(Player player, String itemId, long cooldown) {
        Map<String, Long> playerCooldowns = itemCooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        playerCooldowns.put(itemId, System.currentTimeMillis() + (cooldown * 1000));
    }
}
