package dev.hexworks.core.data;

import net.fabricmc.loader.api.FabricLoader;

import dev.hexworks.core.HexWorksCore;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.StandardCopyOption;

public class PlayerDataStorage {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path DATA_DIRECTORY = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("hexworks")
            .resolve("playerdata");

    private static void ensureDataDirectory() throws IOException {
        Files.createDirectories(DATA_DIRECTORY);
    }

    public static void save(UUID playerId, PlayerData playerData) throws IOException {
        ensureDataDirectory();

        Path playerFile = DATA_DIRECTORY.resolve(playerId + ".json");

        String json = GSON.toJson(playerData.getAll());

        Files.writeString(playerFile, json);
    }
    public static PlayerData load(UUID playerId) throws IOException {
        ensureDataDirectory();

        Path playerFile = DATA_DIRECTORY.resolve(playerId + ".json");

        if (!Files.exists(playerFile)) {
            return new PlayerData();
        }

        try {
            String json = Files.readString(playerFile);
            JsonObject object = JsonParser.parseString(json).getAsJsonObject();

            PlayerData playerData = new PlayerData();

            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();

                if (value.isJsonPrimitive()) {
                    if (value.getAsJsonPrimitive().isBoolean()) {
                        playerData.setBoolean(key, value.getAsBoolean());

                    } else if (value.getAsJsonPrimitive().isString()) {
                        playerData.setString(key, value.getAsString());

                    } else if (value.getAsJsonPrimitive().isNumber()) {
                        Number number = value.getAsNumber();

                        if (number.doubleValue() == number.intValue()) {
                            playerData.setInt(key, number.intValue());
                        } else {
                            playerData.setDouble(key, number.doubleValue());
                        }
                    }
                }
            }

            return playerData;

        } catch (RuntimeException e) {
            boolean backupSucceeded = backupCorruptedFile(playerFile);

            if (backupSucceeded) {
                Files.writeString(playerFile, "{}");

                HexWorksCore.LOGGER.warn(
                        "Reset corrupted player data file {}",
                        playerFile
                );

                return new PlayerData();
            }

            throw new IOException(
                    "Player data file is corrupted and could not be backed up: " + playerFile,
                    e
            );
        }
    }

    private static boolean backupCorruptedFile(Path playerFile) {
        try {
            Path backupFile = playerFile.resolveSibling(
                    playerFile.getFileName() + ".corrupted"
            );

            Files.copy(
                    playerFile,
                    backupFile,
                    StandardCopyOption.REPLACE_EXISTING
            );

            HexWorksCore.LOGGER.warn(
                    "Backed up corrupted player data to {}",
                    backupFile
            );

            return true;

        } catch (IOException e) {
            HexWorksCore.LOGGER.error(
                    "Failed to back up corrupted player data file {}",
                    playerFile,
                    e
            );

            return false;
        }
    }

}