package com.kzics.customitems.manager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public class EffectManager {

    public static void applyEffectsToSelf(Player player, Collection<PotionEffect> effects) {
        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
        }
    }

    public static void applyEffectsInArea(Player player, Collection<PotionEffect> effects, double radius) {
        Location location = player.getLocation();
        Collection<Entity> entities = player.getWorld().getNearbyEntities(location, radius, radius, radius, entity -> entity instanceof LivingEntity);

        for (Entity entity : entities) {
            if(!(entity instanceof Player)) continue;
            Player target = (Player) entity;

            for (PotionEffect effect : effects) {
                target.addPotionEffect(effect);
            }
        }
    }

    public static void applyEffectsOnHit(LivingEntity target, Collection<PotionEffect> effects) {
        for (PotionEffect effect : effects) {
            target.addPotionEffect(effect);
        }
    }
}