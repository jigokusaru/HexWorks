package dev.hexworks.core.data;

import net.minecraft.server.level.ServerPlayer;
import dev.hexworks.core.HexWorksCore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final Map<UUID, PlayerData> players = new HashMap<>();

    public PlayerData get(UUID playerId) {
        PlayerData existingData = players.get(playerId);

        if (existingData != null) {
            return existingData;
        }

        try {
            PlayerData loadedData = PlayerDataStorage.load(playerId);
            players.put(playerId, loadedData);
            return loadedData;
        } catch (IOException e) {
            HexWorksCore.LOGGER.error(
                    "Failed to load player data for {}",
                    playerId,
                    e
            );

            return new PlayerData();
        }
    }

    public PlayerData get(ServerPlayer player) {
        return get(player.getUUID());
    }

    public void save(UUID playerId) {
        PlayerData playerData = players.get(playerId);

        if (playerData == null) {
            return;
        }

        try {
            PlayerDataStorage.save(playerId, playerData);
        } catch (IOException e) {
            HexWorksCore.LOGGER.error(
                    "Failed to save player data for {}",
                    playerId,
                    e
            );
        }
    }

    public void save(ServerPlayer player) {
        save(player.getUUID());
    }

    public void saveAll() {
        for (UUID playerId : players.keySet()) {
            save(playerId);
        }
    }

    public boolean has(UUID playerId) {
        return players.containsKey(playerId);
    }

    public void remove(UUID playerId) {
        players.remove(playerId);
    }

    public int size() {
        return players.size();
    }
}