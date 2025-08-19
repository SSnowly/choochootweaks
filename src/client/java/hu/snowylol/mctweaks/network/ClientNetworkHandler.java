package hu.snowylol.mctweaks.network;

import hu.snowylol.mctweaks.ChooChooTweaks;
import hu.snowylol.mctweaks.config.MinecartSpeedConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.HashMap;
import java.util.Map;

public class ClientNetworkHandler {
    private static final Map<MinecartSpeedConfig.MinecartType, Double> clientSpeeds = new HashMap<>();
    private static final double DEFAULT_SPEED = 0.4;
    private static boolean isOperator = false;
    
    public static void registerClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SyncSpeedsPayload.ID, ClientNetworkHandler::handleSpeedSync);
    }
    
    private static void handleSpeedSync(SyncSpeedsPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            updateClientSpeeds(payload.speeds());
            isOperator = payload.isOperator();
            ChooChooTweaks.LOGGER.info("Received speed sync from server. Operator: {}", isOperator);
        });
    }
    
    private static void updateClientSpeeds(Map<String, Double> speeds) {
        clientSpeeds.clear();
        for (Map.Entry<String, Double> entry : speeds.entrySet()) {
            try {
                MinecartSpeedConfig.MinecartType type = MinecartSpeedConfig.MinecartType.valueOf(entry.getKey());
                clientSpeeds.put(type, entry.getValue());
            } catch (IllegalArgumentException e) {
                ChooChooTweaks.LOGGER.warn("Unknown minecart type received: {}", entry.getKey());
            }
        }
    }
    

    
    public static void requestSync() {
        if (ClientPlayNetworking.canSend(RequestSyncPayload.ID)) {
            ClientPlayNetworking.send(new RequestSyncPayload());
        }
    }
    
    public static void setSpeed(MinecartSpeedConfig.MinecartType type, double speed) {
        clientSpeeds.put(type, speed);
        ClientPlayNetworking.send(new SetSpeedPayload(type.name(), speed));
        ChooChooTweaks.LOGGER.info("Sent speed update for {} to {} to server.", type.getDisplayName(), speed);
    }
    
    public static double getSpeed(MinecartSpeedConfig.MinecartType type) {
        return clientSpeeds.getOrDefault(type, DEFAULT_SPEED);
    }
    
    public static boolean isOperator() {
        return isOperator;
    }
    
    public static void onServerJoin() {
        requestSync();
    }
}
