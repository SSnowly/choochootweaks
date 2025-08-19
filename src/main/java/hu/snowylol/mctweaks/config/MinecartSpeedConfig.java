package hu.snowylol.mctweaks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.entity.vehicle.SpawnerMinecartEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.server.MinecraftServer;

import hu.snowylol.mctweaks.ChooChooTweaks;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MinecartSpeedConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_NAME = "choochoo_tweaks_speeds.json";
    private static final double DEFAULT_SPEED = 0.4;
    
    public enum MinecartType {
        EMPTY_MINECART("empty_minecart"),
        MINECART_WITH_PLAYER("minecart_with_player"),
        MINECART_WITH_ENTITY("minecart_with_entity"),
        CHEST_MINECART("chest_minecart"),
        FURNACE_MINECART("furnace_minecart"),
        HOPPER_MINECART("hopper_minecart"),
        TNT_MINECART("tnt_minecart"),
        SPAWNER_MINECART("spawner_minecart"),
        COMMAND_BLOCK_MINECART("command_block_minecart");
        
        private final String key;
        
        MinecartType(String key) {
            this.key = key;
        }
        
        public String getKey() {
            return key;
        }
        
        public String getDisplayName() {
            return switch (this) {
                case EMPTY_MINECART -> "Empty Minecart";
                case MINECART_WITH_PLAYER -> "Minecart with Player";
                case MINECART_WITH_ENTITY -> "Minecart with Entity";
                case CHEST_MINECART -> "Chest Minecart";
                case FURNACE_MINECART -> "Furnace Minecart";
                case HOPPER_MINECART -> "Hopper Minecart";
                case TNT_MINECART -> "TNT Minecart";
                case SPAWNER_MINECART -> "Spawner Minecart";
                case COMMAND_BLOCK_MINECART -> "Command Block Minecart";
            };
        }
        
        public static MinecartType fromMinecart(AbstractMinecartEntity minecart) {
            if (minecart instanceof MinecartEntity) {
                if (!minecart.getPassengerList().isEmpty()) {
                    if (minecart.getFirstPassenger() != null && minecart.getFirstPassenger().isPlayer()) {
                        return MINECART_WITH_PLAYER;
                    } else {
                        return MINECART_WITH_ENTITY;
                    }
                }
                return EMPTY_MINECART;
            } else if (minecart instanceof ChestMinecartEntity) {
                return CHEST_MINECART;
            } else if (minecart instanceof FurnaceMinecartEntity) {
                return FURNACE_MINECART;
            } else if (minecart instanceof HopperMinecartEntity) {
                return HOPPER_MINECART;
            } else if (minecart instanceof TntMinecartEntity) {
                return TNT_MINECART;
            } else if (minecart instanceof SpawnerMinecartEntity) {
                return SPAWNER_MINECART;
            } else if (minecart instanceof CommandBlockMinecartEntity) {
                return COMMAND_BLOCK_MINECART;
            }
            return EMPTY_MINECART;
        }
    }
    
    private Map<String, Double> speeds = new HashMap<>();
    
    public MinecartSpeedConfig() {
        for (MinecartType type : MinecartType.values()) {
            speeds.put(type.getKey(), DEFAULT_SPEED);
        }
    }
    
    public double getSpeed(MinecartType type) {
        return speeds.getOrDefault(type.getKey(), DEFAULT_SPEED);
    }
    
    public void setSpeed(MinecartType type, double speed) {
        speeds.put(type.getKey(), Math.max(0.0, Math.min(10000.0, speed)));
    }
    
    public Map<String, Double> getAllSpeeds() {
        return new HashMap<>(speeds);
    }
    
    public void resetToDefaults() {
        for (MinecartType type : MinecartType.values()) {
            speeds.put(type.getKey(), DEFAULT_SPEED);
        }
    }
    
    public static File getConfigFile(MinecraftServer server) {
        File worldDir = server.getRunDirectory().toFile();
        File saveDir = new File(worldDir, "saves");
        File levelDir = new File(saveDir, server.getSaveProperties().getLevelName());
        return new File(levelDir, CONFIG_FILE_NAME);
    }
    
    public static MinecartSpeedConfig load(MinecraftServer server) {
        File configFile = getConfigFile(server);
        
        if (!configFile.exists()) {
            ChooChooTweaks.LOGGER.info("Creating new minecart speed config for world");
            MinecartSpeedConfig config = new MinecartSpeedConfig();
            config.save(server);
            return config;
        }
        
        try (FileReader reader = new FileReader(configFile)) {
            MinecartSpeedConfig config = GSON.fromJson(reader, MinecartSpeedConfig.class);
            if (config == null) {
                config = new MinecartSpeedConfig();
            }
            
            for (MinecartType type : MinecartType.values()) {
                if (!config.speeds.containsKey(type.getKey())) {
                    config.speeds.put(type.getKey(), DEFAULT_SPEED);
                }
            }
            
            ChooChooTweaks.LOGGER.info("Loaded minecart speed config for world");
            return config;
        } catch (IOException e) {
            ChooChooTweaks.LOGGER.error("Failed to load minecart speed config, using defaults", e);
            return new MinecartSpeedConfig();
        }
    }
    
    public void save(MinecraftServer server) {
        File configFile = getConfigFile(server);
        
        try {
            configFile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(this, writer);
            }
            ChooChooTweaks.LOGGER.info("Saved minecart speed config for world");
        } catch (IOException e) {
            ChooChooTweaks.LOGGER.error("Failed to save minecart speed config", e);
        }
    }
}
