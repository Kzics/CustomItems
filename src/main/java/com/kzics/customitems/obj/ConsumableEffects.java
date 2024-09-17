package com.kzics.customitems.obj;

import org.bukkit.potion.PotionEffect;

public class ConsumableEffects {


    private final PotionEffect[] potionEffects;

    public ConsumableEffects(PotionEffect... effects){
        this.potionEffects = effects;
    }

    public PotionEffect[] getPotionEffects() {
        return potionEffects;
    }
}
