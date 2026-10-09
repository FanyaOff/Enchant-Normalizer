package com.fanya.enchaments_normalizer;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Enchaments_normalizer implements ModInitializer
{
	public static final String MOD_ID = "enchaments_normalizer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		LOGGER.info("Enchantments Normalizer loaded (26.3)");
	}
}
