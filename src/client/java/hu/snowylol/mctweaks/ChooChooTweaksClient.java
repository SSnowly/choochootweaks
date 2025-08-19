package hu.snowylol.mctweaks;

import hu.snowylol.mctweaks.network.ClientNetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class ChooChooTweaksClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ChooChooTweaks.LOGGER.info("ChooChoo Tweaks Client initializing...");
		
		ClientNetworkHandler.registerClientPackets();
		
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			ClientNetworkHandler.onServerJoin();
		});
		
		ChooChooTweaks.LOGGER.info("ChooChoo Tweaks Client initialized!");
	}
}