package com.kzics.customitems.config;

import com.kzics.customitems.enums.ActivationType;
import com.kzics.customitems.obj.ConsumableEffects;
import com.kzics.customitems.obj.TargetInfo;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.List;

public class ConsumableItemConfig {

    private final String name;
    private final String id;
    private final List<String> lore;
    private final Material material;
    private final int use;
    private final int cooldown;
    private final ActivationType activationType;
    private final TargetInfo targetInfo;
    private final ConsumableEffects consumableEffects;
    private final boolean affectingClanMember;
    private final Sound sound;
    public ConsumableItemConfig(String name, String id , List<String> lore, Material material, int use, int cooldown, ActivationType activationType, TargetInfo targetInfo, ConsumableEffects effects, boolean affectingClanMember, Sound sound){
        this.name = name;
        this.id = id;
        this.lore = lore;
        this.material = material;
        this.use = use;
        this.cooldown = cooldown;
        this.activationType = activationType;
        this.targetInfo = targetInfo;
        this.consumableEffects = effects;
        this.affectingClanMember = affectingClanMember;
        this.sound = sound;
    }

    public Sound getSound() {
        return sound;
    }

    public boolean isAffectingClanMember() {
        return affectingClanMember;
    }

    public Material getMaterial() {
        return material;
    }

    public ActivationType getActivationType() {
        return activationType;
    }

    public ConsumableEffects getConsumableEffects() {
        return consumableEffects;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getUse() {
        return use;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TargetInfo getTargetInfo() {
        return targetInfo;
    }
}
