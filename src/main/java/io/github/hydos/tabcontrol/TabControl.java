package io.github.hydos.tabcontrol;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.server.PlayerConnectCallback;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.text.LiteralText;

public class TabControl implements DedicatedServerModInitializer {

	@Override
	public void onInitializeServer() {
		ServerPlayerEvents.CONNECT.register((connection, player) -> {
			connection.send(new PlayerListHeaderS2CPacket(new LiteralText("This is a cool test")));
		});
	}
}
