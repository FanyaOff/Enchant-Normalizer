package com.fanya.enchaments_normalizer.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Unique
    private static boolean isHandling = false;

    @Inject(
            method = "getName(Lnet/minecraft/registry/entry/RegistryEntry;I)Lnet/minecraft/text/Text;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onGetName(RegistryEntry<Enchantment> enchantmentEntry, int level, CallbackInfoReturnable<Text> cir) {
        if (isHandling) return;
        isHandling = true;

        try {
            Enchantment enchantment = enchantmentEntry.value();

            MutableText mutableText = enchantment.description().copy();

            if (enchantmentEntry.isIn(EnchantmentTags.CURSE)) {
                Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(Formatting.RED));
            } else {
                Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(Formatting.GRAY));
            }

            String romanLevel = convertToRoman(level);

            if (level != 1 || enchantment.getMaxLevel() != 1) {
                mutableText.append(ScreenTexts.SPACE).append(Text.literal(romanLevel).formatted(Formatting.GOLD));
            }
            cir.setReturnValue(mutableText);

        } finally {
            isHandling = false;
        }
    }

    private static String convertToRoman(int number) {
        String[] romanNumerals = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int[] values = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                number -= values[i];
                result.append(romanNumerals[i]);
            }
        }

        return result.toString();
    }
}