package com.fanya.enchaments_normalizer.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin
{
	@Unique
	private static boolean enchaments_normalizer$handling = false;

	/**
	 * 26.3 official name: getFullname(Holder, int).
	 * Falls back gracefully if signature drifts (require handled by mixins.json).
	 */
	@Inject(
		method = "getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void enchaments_normalizer$onGetFullname(Holder<Enchantment> enchantmentEntry, int level, CallbackInfoReturnable<Component> cir)
	{
		if (enchaments_normalizer$handling)
		{
			return;
		}
		enchaments_normalizer$handling = true;
		try
		{
			Enchantment enchantment = enchantmentEntry.value();
			MutableComponent name = enchantment.description().copy();

			if (enchantmentEntry.is(EnchantmentTags.CURSE))
			{
				ComponentUtils.mergeStyles(name, Style.EMPTY.withColor(ChatFormatting.RED));
			}
			else
			{
				ComponentUtils.mergeStyles(name, Style.EMPTY.withColor(ChatFormatting.GRAY));
			}

			if (level != 1 || enchantment.getMaxLevel() != 1)
			{
				name.append(CommonComponents.SPACE)
					.append(Component.literal(enchaments_normalizer$toRoman(level)).withStyle(ChatFormatting.GOLD));
			}
			cir.setReturnValue(name);
		}
		finally
		{
			enchaments_normalizer$handling = false;
		}
	}

	@Unique
	private static String enchaments_normalizer$toRoman(int number)
	{
		if (number <= 0)
		{
			return String.valueOf(number);
		}
		String[] numerals = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
		int[] values = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < values.length; i++)
		{
			while (number >= values[i])
			{
				number -= values[i];
				result.append(numerals[i]);
			}
		}
		return result.toString();
	}
}
