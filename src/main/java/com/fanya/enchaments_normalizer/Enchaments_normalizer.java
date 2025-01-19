package com.fanya.enchaments_normalizer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class Enchaments_normalizer implements ModInitializer, ClientModInitializer {

    public static final String MOD_ID = "romanlevelmod";

    @Override
    public void onInitialize() {
        System.out.println("RomanLevelMod initialized!");
    }

    @Override
    public void onInitializeClient() {
    }
}
