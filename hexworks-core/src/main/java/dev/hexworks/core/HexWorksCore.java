package dev.hexworks.core;

import net.fabricmc.api.ModInitializer;
import dev.hexworks.core.data.PlayerDataManager;
import dev.hexworks.core.data.PlayerData;
import dev.hexworks.core.data.PlayerDataStorage;
import dev.hexworks.core.data.PlayerDataEvents;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HexWorksCore implements ModInitializer {

    public static final String MOD_ID = "hexworks-core";
    public static final PlayerDataManager PLAYER_DATA = new PlayerDataManager();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initialized.");
        PlayerDataEvents.register();
    }
}