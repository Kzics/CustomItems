package com.kzics.customitems.utils;

import com.kzics.customitems.obj.ConsumableEffects;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SerializerUtils {

    public static String serializeEffects(ConsumableEffects effects) {
        StringBuilder serializedEffects = new StringBuilder();

        for (PotionEffect effect : effects.getPotionEffects()) {
            // Format: EffectType:Duration:Amplifier; (for example: SPEED:8:4;)
            serializedEffects.append(effect.getType().getName())
                    .append(":")
                    .append(effect.getDuration())
                    .append(":")
                    .append(effect.getAmplifier())
                    .append(";");
        }

        return serializedEffects.toString();
    }

    public static ConsumableEffects deserializeEffects(String effectsString) {
        String[] effectDataArray = effectsString.split(";");
        PotionEffect[] potionEffects = new PotionEffect[effectDataArray.length];

        for (int i = 0; i < effectDataArray.length; i++) {
            String[] effectData = effectDataArray[i].split(":");
            PotionEffect potionEffect = new PotionEffect(
                    PotionEffectType.getByName(effectData[0]), // Effect type
                    Integer.parseInt(effectData[1]), // Duration
                    Integer.parseInt(effectData[2])  // Amplifier
            );
            potionEffects[i] = potionEffect;
        }

        return new ConsumableEffects(potionEffects);
    }
}
