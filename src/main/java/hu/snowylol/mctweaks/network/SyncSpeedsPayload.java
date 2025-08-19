package hu.snowylol.mctweaks.network;

import hu.snowylol.mctweaks.ChooChooTweaks;
import hu.snowylol.mctweaks.config.MinecartSpeedConfig;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public record SyncSpeedsPayload(Map<String, Double> speeds, boolean isOperator) implements CustomPayload {
    public static final CustomPayload.Id<SyncSpeedsPayload> ID = new CustomPayload.Id<>(Identifier.of(ChooChooTweaks.MOD_ID, "sync_speeds"));
    
    public static final PacketCodec<RegistryByteBuf, SyncSpeedsPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.map(HashMap::new, PacketCodecs.STRING, PacketCodecs.DOUBLE), SyncSpeedsPayload::speeds,
        PacketCodecs.BOOLEAN, SyncSpeedsPayload::isOperator,
        SyncSpeedsPayload::new
    );
    
    public SyncSpeedsPayload(MinecartSpeedConfig config, boolean isOperator) {
        this(createSpeedsMap(config), isOperator);
    }
    
    private static Map<String, Double> createSpeedsMap(MinecartSpeedConfig config) {
        Map<String, Double> speeds = new HashMap<>();
        for (MinecartSpeedConfig.MinecartType type : MinecartSpeedConfig.MinecartType.values()) {
            speeds.put(type.name(), config.getSpeed(type));
        }
        return speeds;
    }
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
