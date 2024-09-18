package com.kzics.customitems.listeners;

import com.kzics.customitems.CustomItems;
import com.kzics.customitems.enums.ActivationType;
import com.kzics.customitems.enums.TargetType;
import com.kzics.customitems.items.ConsumableItem;
import com.kzics.customitems.manager.EffectManager;
import com.kzics.customitems.obj.ConsumableEffects;
import com.kzics.customitems.utils.SerializerUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class PlayerListeners implements Listener {

    private final CustomItems customItems;

    public PlayerListeners(CustomItems customItems) {
        this.customItems = customItems;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        for (ConsumableItem consumableItem : customItems.getItemsManager().getItems().values()) {
            player.getInventory().addItem(consumableItem);
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

        ActivationType activationType = ActivationType.valueOf(itemInfo[3]);
        int usesLeft = Integer.parseInt(itemInfo[1]);
        if(usesLeft <= 0){
            destroyItem(player, itemStack);
        }

        usesLeft -= 1;

        itemInfo[1] = String.valueOf(usesLeft);

        if(activationType != ActivationType.RIGHT_CLICK) return;

        TargetType targetType = TargetType.valueOf(itemInfo[4]);

        ConsumableEffects effects = SerializerUtils.deserializeEffects(itemStack.getItemMeta().getPersistentDataContainer().get(ConsumableItem.effectsKey, PersistentDataType.STRING));

        applyEffectsBasedOnTarget(player, null, targetType, effects, itemInfo);

        if(usesLeft - 1 <= 0){
            destroyItem(player, itemStack);

        }else{
            itemStack.getItemMeta().getPersistentDataContainer().set(
                    ConsumableItem.itemKey,
                    PersistentDataType.STRING,
                    String.join(";", itemInfo)
            );
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

        int usesLeft = Integer.parseInt(itemInfo[1]);

        if(usesLeft <= 0){
            destroyItem(player, itemStack);
        }

        usesLeft -= 1;

        itemInfo[1] = String.valueOf(usesLeft);


        ActivationType activationType = ActivationType.valueOf(itemInfo[3]);
        TargetType targetType = TargetType.valueOf(itemInfo[4]);
        ConsumableEffects effects = SerializerUtils.deserializeEffects(itemStack.getItemMeta().getPersistentDataContainer().get(ConsumableItem.effectsKey, PersistentDataType.STRING));

        if (activationType == ActivationType.HIT) {
            applyEffectsBasedOnTarget(player, target, targetType, effects, itemInfo);
        }

        if(usesLeft - 1 <= 0){
            destroyItem(player, itemStack);

        }else{
            itemStack.getItemMeta().getPersistentDataContainer().set(
                    ConsumableItem.itemKey,
                    PersistentDataType.STRING,
                    String.join(";", itemInfo)
            );
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
}
