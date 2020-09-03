package io.github.hydos.tabcontrol;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.server.PlayerConnectCallback;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.text.LiteralText;
import net.swifthq.swiftapi.callbacks.entity.player.PlayerJoinCallback;

public class TabControl implements DedicatedServerModInitializer {

	@Override
	public void onInitializeServer() {
		PlayerJoinCallback.EVENT.register((player) -> {
			player.networkHandler.sendPacket(new PlayerListHeaderS2CPacket(new LiteralText("This is a cool test")));
		});
	}
}
