package hu.snowylol.mctweaks.mixin;

import hu.snowylol.mctweaks.MinecartSpeedManager;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public class AbstractMinecartEntityMixin {
    
    @Inject(method = "getMaxSpeed", at = @At("RETURN"), cancellable = true, require = 0)
    private void modifyMaxSpeed(CallbackInfoReturnable<Double> cir) {
        AbstractMinecartEntity minecart = (AbstractMinecartEntity) (Object) this;
        
        // Only modify on server side
        if (minecart.getWorld().isClient()) {
            return;
        }
        
        MinecraftServer server = minecart.getWorld().getServer();
        
        if (server != null) {
            double customSpeed = MinecartSpeedManager.getMinecartSpeed(minecart, server);
            cir.setReturnValue(customSpeed);
        }
    }
}
