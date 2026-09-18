package dev.hexworks.core.data;

import dev.hexworks.core.HexWorksCore;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class PlayerDataEvents {

    public static void register() {

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            HexWorksCore.PLAYER_DATA.get(handler.player);

            HexWorksCore.LOGGER.info(
                    "Initialized player data for {}",
                    handler.player.getUUID()
            );
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            HexWorksCore.PLAYER_DATA.save(handler.player);
            HexWorksCore.PLAYER_DATA.remove(handler.player.getUUID());

            HexWorksCore.LOGGER.info(
                    "Released player data for {}",
                    handler.player.getUUID()
            );
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            HexWorksCore.PLAYER_DATA.saveAll();

            HexWorksCore.LOGGER.info("Saved all player data.");
        });
    }
}