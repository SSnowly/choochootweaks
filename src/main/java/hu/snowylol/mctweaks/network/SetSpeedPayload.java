package hu.snowylol.mctweaks.network;

import hu.snowylol.mctweaks.ChooChooTweaks;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SetSpeedPayload(String type, double speed) implements CustomPayload {
    public static final CustomPayload.Id<SetSpeedPayload> ID = new CustomPayload.Id<>(Identifier.of(ChooChooTweaks.MOD_ID, "set_speed"));
    
    public static final PacketCodec<RegistryByteBuf, SetSpeedPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, SetSpeedPayload::type,
        PacketCodecs.DOUBLE, SetSpeedPayload::speed,
        SetSpeedPayload::new
    );
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
