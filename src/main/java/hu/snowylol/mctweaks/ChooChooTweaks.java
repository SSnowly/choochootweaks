package hu.snowylol.mctweaks;

import hu.snowylol.mctweaks.command.MinecartSpeedCommand;
import hu.snowylol.mctweaks.network.NetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChooChooTweaks implements ModInitializer {
	public static final String MOD_ID = "choochoo-tweaks";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("ChooChoo Tweaks initializing...");
		
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			MinecartSpeedCommand.register(dispatcher);
		});
		
		NetworkHandler.registerServerPackets();
		
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			NetworkHandler.onPlayerJoin(handler.getPlayer());
		});
		
		LOGGER.info("ChooChoo Tweaks initialized!");
	}
}