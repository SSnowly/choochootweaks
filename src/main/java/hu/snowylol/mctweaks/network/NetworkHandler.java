package hu.snowylol.mctweaks.network;

import hu.snowylol.mctweaks.ChooChooTweaks;
import hu.snowylol.mctweaks.MinecartSpeedManager;
import hu.snowylol.mctweaks.config.MinecartSpeedConfig;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NetworkHandler {
    public static final Identifier SYNC_SPEEDS_ID = Identifier.of(ChooChooTweaks.MOD_ID, "sync_speeds");
    public static final Identifier SET_SPEED_ID = Identifier.of(ChooChooTweaks.MOD_ID, "set_speed");
    public static final Identifier REQUEST_SYNC_ID = Identifier.of(ChooChooTweaks.MOD_ID, "request_sync");
    
    private static boolean checkOperatorPermission(ServerPlayerEntity player) {
        return player.getServer().getPlayerManager().isOperator(player.getGameProfile());
    }
    
    public static void registerServerPackets() {
        PayloadTypeRegistry.playS2C().register(SyncSpeedsPayload.ID, SyncSpeedsPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SetSpeedPayload.ID, SetSpeedPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestSyncPayload.ID, RequestSyncPayload.CODEC);
        
        ServerPlayNetworking.registerGlobalReceiver(RequestSyncPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            context.server().execute(() -> {
                sendSpeedSync(player);
            });
        });
        
        ServerPlayNetworking.registerGlobalReceiver(SetSpeedPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            context.server().execute(() -> {
                if (!checkOperatorPermission(player)) {
                    ChooChooTweaks.LOGGER.warn("Player {} tried to set minecart speed without operator permissions", player.getName().getString());
                    return;
                }
                
                try {
                    MinecartSpeedConfig.MinecartType type = MinecartSpeedConfig.MinecartType.valueOf(payload.type());
                    MinecartSpeedManager.setMinecartSpeed(type, payload.speed(), context.server());
                    
                    for (ServerPlayerEntity p : context.server().getPlayerManager().getPlayerList()) {
                        sendSpeedSync(p);
                    }
                    
                    ChooChooTweaks.LOGGER.info("Player {} set {} speed to {}", 
                        player.getName().getString(), type.getDisplayName(), payload.speed());
                } catch (IllegalArgumentException e) {
                    ChooChooTweaks.LOGGER.warn("Invalid minecart type from client: {}", payload.type());
                }
            });
        });
    }
    
    public static void sendSpeedSync(ServerPlayerEntity player) {
        MinecartSpeedConfig config = MinecartSpeedManager.getConfig(player.getServer());
        boolean isOperator = checkOperatorPermission(player);
        
        SyncSpeedsPayload payload = new SyncSpeedsPayload(config, isOperator);
        ServerPlayNetworking.send(player, payload);
    }
    
    public static void onPlayerJoin(ServerPlayerEntity player) {
        sendSpeedSync(player);
    }
}
