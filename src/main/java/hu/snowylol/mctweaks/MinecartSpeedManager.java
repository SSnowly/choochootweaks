package hu.snowylol.mctweaks;

import hu.snowylol.mctweaks.config.MinecartSpeedConfig;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;

public class MinecartSpeedManager {
    private static final Map<String, MinecartSpeedConfig> worldConfigs = new HashMap<>();
    
    public static MinecartSpeedConfig getConfig(MinecraftServer server) {
        if (server == null) {
            return new MinecartSpeedConfig();
        }
        
        String worldName = server.getSaveProperties().getLevelName();
        return worldConfigs.computeIfAbsent(worldName, k -> MinecartSpeedConfig.load(server));
    }
    
    public static void saveConfig(MinecraftServer server) {
        if (server == null) return;
        
        String worldName = server.getSaveProperties().getLevelName();
        MinecartSpeedConfig config = worldConfigs.get(worldName);
        if (config != null) {
            config.save(server);
        }
    }
    
    public static double getMinecartSpeed(AbstractMinecartEntity minecart, MinecraftServer server) {
        MinecartSpeedConfig config = getConfig(server);
        MinecartSpeedConfig.MinecartType type = MinecartSpeedConfig.MinecartType.fromMinecart(minecart);
        return config.getSpeed(type);
    }
    
    public static void setMinecartSpeed(MinecartSpeedConfig.MinecartType type, double speed, MinecraftServer server) {
        MinecartSpeedConfig config = getConfig(server);
        config.setSpeed(type, speed);
        saveConfig(server);
    }
    
    public static void clearWorldConfig(String worldName) {
        worldConfigs.remove(worldName);
    }
    
    public static void onServerStopping(MinecraftServer server) {
        saveConfig(server);
        clearWorldConfig(server.getSaveProperties().getLevelName());
    }
}
